import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { fetchServerMenusWithCookie } from "@/lib/admin/menuServer";

export const dynamic = "force-dynamic";
export const revalidate = 0;

export async function GET(request: NextRequest) {
  const sessionCookie = request.headers.get("cookie") ?? "";
  const menus = await fetchServerMenusWithCookie(sessionCookie);

  return NextResponse.json({
    menus,
  });
}
