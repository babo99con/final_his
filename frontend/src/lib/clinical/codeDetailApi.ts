import { CLINICAL_API_BASE } from "./clinicalApiBase";

type ApiEnvelope<T> = { success?: boolean; message?: string | null; data?: T; result?: T };
type CodeDetailGroup = "DISEASE" | "DRUG" | "TREAT" | "INFUSION";

function unwrap<T>(body: ApiEnvelope<T> | T): T {
  if (
    body &&
    typeof body === "object" &&
    ("data" in (body as Record<string, unknown>) ||
      "result" in (body as Record<string, unknown>))
  ) {
    const env = body as ApiEnvelope<T>;
    return (env.result ?? env.data) as T;
  }
  return body as T;
}

export type CodeDetailOption = { code: string; name: string; note?: string | null };

export async function fetchCodeDetailOptions(group: CodeDetailGroup): Promise<CodeDetailOption[]> {
  const sp = new URLSearchParams();
  sp.set("group", group);
  const res = await fetch(`${CLINICAL_API_BASE}/api/code-details?${sp.toString()}`, { cache: "no-store" });
  const json = (await res.json().catch(() => ({}))) as ApiEnvelope<CodeDetailOption[]>;
  if (!res.ok) {
    throw new Error(json?.message ?? `코드 목록 조회 실패 (${res.status})`);
  }
  if (json.success === false) {
    throw new Error(json.message ?? "코드 목록 조회 실패");
  }
  const value = unwrap<CodeDetailOption[]>(json);
  return Array.isArray(value) ? value : [];
}

