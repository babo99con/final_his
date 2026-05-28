import StaffPositionClient from "@/app/staff/position/StaffPositionClient";
import { fetchInitialPositionSummary } from "@/lib/staff/staffServerApi";
import { getServerSessionCookieHeader } from "@/lib/server/sessionCookie";
import type { PositionResponse } from "@/features/staff/position/positiontypes";

export const dynamic = "force-dynamic";

export default async function StaffPositionPage() {
  const sessionCookie = await getServerSessionCookieHeader();

  let initialRows: PositionResponse[] = [];
  let initialError: string | null = null;

  if (sessionCookie) {
    try {
      initialRows = await fetchInitialPositionSummary(sessionCookie);
    } catch (error) {
      initialError = error instanceof Error ? error.message : "직책 목록 조회에 실패했습니다.";
    }
  }

  return <StaffPositionClient initialRows={initialRows} initialError={initialError} />;
}
