import CodeAdminClient from "@/app/admin/codes/CodeAdminClient";
import { getServerSessionCookieHeader } from "@/lib/server/sessionCookie";
import {
  fetchInitialCodeDetails,
  fetchInitialCodeGroups,
} from "@/lib/admin/codeAdminServerApi";
import type { CodeDetailItem, CodeGroupItem } from "@/lib/admin/codeAdminApi";

export const dynamic = "force-dynamic";

export default async function CodeAdminPage() {
  const sessionCookie = await getServerSessionCookieHeader();

  let initialGroups: CodeGroupItem[] = [];
  let initialDetails: CodeDetailItem[] = [];
  let initialError: string | null = null;

  if (sessionCookie) {
    try {
      [initialGroups, initialDetails] = await Promise.all([
        fetchInitialCodeGroups(sessionCookie, false),
        fetchInitialCodeDetails(sessionCookie, undefined, false),
      ]);
    } catch (error) {
      initialError =
        error instanceof Error ? error.message : "코드 관리 초기 조회에 실패했습니다.";
    }
  }

  return (
    <CodeAdminClient
      initialGroups={initialGroups}
      initialDetails={initialDetails}
      initialError={initialError}
    />
  );
}
