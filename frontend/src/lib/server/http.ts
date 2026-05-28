import "server-only";

export const fetchJson = async <T>(
  url: string,
  sessionCookie: string,
  init?: RequestInit
): Promise<T> => {
  const response = await fetch(url, {
    cache: "no-store",
    ...init,
    headers: {
      Accept: "application/json",
      Cookie: sessionCookie,
      ...(init?.headers ?? {}),
    },
  });

  let payload: unknown = null;
  try {
    payload = await response.json();
  } catch {
    payload = null;
  }

  if (!response.ok) {
    const message =
      typeof payload === "object" &&
      payload &&
      "message" in payload &&
      typeof (payload as { message?: unknown }).message === "string"
        ? (payload as { message: string }).message
        : `API 호출 실패 (${response.status})`;
    throw new Error(message);
  }

  return payload as T;
};
