import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { fetchServerMenus } from "@/lib/admin/menuServer";

const extractBearerToken = (request: NextRequest) => {
  const header = request.headers.get("authorization")?.trim() ?? "";
  if (!header.toLowerCase().startsWith("bearer ")) {
    return undefined;
  }
  return header.slice("bearer ".length).trim() || undefined;
};

export async function GET(request: NextRequest) {
  const menus = await fetchServerMenus(extractBearerToken(request));

  return NextResponse.json({
    menus,
  });
}
