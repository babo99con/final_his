import "server-only";

import { cookies } from "next/headers";

export const getServerSessionCookieHeader = async () => {
  const cookieStore = await cookies();
  return cookieStore
    .getAll()
    .map((cookie) => `${cookie.name}=${encodeURIComponent(cookie.value)}`)
    .join("; ");
};
