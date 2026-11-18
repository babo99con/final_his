import Link from "next/link";

const messages: Record<string, string> = {
  approved: "카카오페이 테스트 결제가 승인되어 수납에 반영되었습니다.",
  cancel: "결제가 취소되었습니다.",
  fail: "카카오페이에서 결제를 완료하지 못했습니다.",
  "missing-parameters": "결제 승인 정보가 없습니다.",
  "not-found": "준비된 결제 요청을 찾을 수 없습니다.",
  unconfirmed: "승인 또는 수납 상태를 확인하지 못했습니다. 재결제 전에 청구 내역을 확인해 주세요.",
};

export default async function KakaoPayResult({
  searchParams,
}: {
  searchParams: Promise<{ state?: string; billId?: string }>;
}) {
  const { state, billId } = await searchParams;
  const hasBill = state === "approved" && /^\d+$/.test(billId ?? "");

  return (
    <main style={{ padding: 32 }}>
      <h1>카카오페이 테스트 결제</h1>
      <p role="status">{messages[state ?? ""] ?? messages.unconfirmed}</p>
      <Link href={hasBill ? "/billing/" + billId : "/billing/outstanding"}>
        {hasBill ? "청구 상세로 돌아가기" : "미수납 목록으로 돌아가기"}
      </Link>
    </main>
  );
}
