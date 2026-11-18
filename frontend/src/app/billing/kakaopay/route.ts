import type { NextRequest } from "next/server";

const resultPath = "/billing/kakaopay/result";

function showResult(state: string, billId?: number) {
  const params = new URLSearchParams({ state });
  if (billId !== undefined) params.set("billId", String(billId));

  // Strip payment credentials before rendering or following any links.
  return new Response(null, {
    status: 303,
    headers: {
      Location: resultPath + "?" + params.toString(),
      "Cache-Control": "no-store",
      "Referrer-Policy": "no-referrer",
    },
  });
}

export async function GET(request: NextRequest) {
  const params = request.nextUrl.searchParams;
  const order = params.get("order");
  const pgToken = params.get("pg_token");
  const state = params.get("state");

  console.info("[kakaopay] callback received", {
    hasOrder: Boolean(order),
    hasApprovalToken: Boolean(pgToken),
  });

  if (state === "cancel" || state === "fail") {
    console.info("[kakaopay] callback result", { state });
    return showResult(state);
  }
  if (!order || !pgToken) {
    console.warn("[kakaopay] callback result", { state: "missing-parameters" });
    return showResult("missing-parameters");
  }

  const billingBase = process.env.BILLING_API_INTERNAL_URL?.trim() || "http://localhost:8081";
  try {
    const response = await fetch(new URL("/api/billing/kakaopay/approve", billingBase), {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ order, pgToken }),
      cache: "no-store",
      signal: AbortSignal.timeout(15_000),
    });

    console.info("[kakaopay] approval response", { status: response.status });
    if (!response.ok) {
      return showResult(response.status === 404 ? "not-found" : "unconfirmed");
    }

    const { billId } = await response.json();
    if (!Number.isSafeInteger(billId) || billId <= 0) {
      console.warn("[kakaopay] callback result", { state: "invalid-response" });
      return showResult("unconfirmed");
    }
    return showResult("approved", billId);
  } catch {
    console.error("[kakaopay] callback result", { state: "approval-request-failed" });
    return showResult("unconfirmed");
  }
}
