import StaffDepartmentsClient from "@/app/staff/departments/StaffDepartmentsClient";
import { fetchInitialStaffDepartments } from "@/lib/staff/staffServerApi";
import { getServerSessionCookieHeader } from "@/lib/server/sessionCookie";
import type { StaffDepartmentSummaryItem } from "@/lib/staff/staffSummaryApi";

export const dynamic = "force-dynamic";

export default async function StaffDepartmentsPage() {
  const sessionCookie = await getServerSessionCookieHeader();

  let initialRows: StaffDepartmentSummaryItem[] = [];
  let initialError: string | null = null;

  if (sessionCookie) {
    try {
      initialRows = await fetchInitialStaffDepartments(sessionCookie);
    } catch (error) {
      initialError = error instanceof Error ? error.message : "부서 목록 조회에 실패했습니다.";
    }
  }

  return <StaffDepartmentsClient initialRows={initialRows} initialError={initialError} />;
}
