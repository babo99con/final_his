export type SessionUser = {
  userId: string;
  username: string;
  fullName: string;
  role: string;
  authRole?: string | null;
  departmentId: string | null;
  departmentName: string | null;
};

export const getEffectiveSessionRole = (user: SessionUser | null | undefined) =>
  user?.authRole ?? user?.role ?? null;

const USER_KEY = "his.user";
const PASSWORD_CHANGE_REQUIRED_KEY = "his.passwordChangeRequired";
const FORCE_PASSWORD_COOKIE_KEY = "his_force_password_change";
const SESSION_CHANGED_EVENT = "his:session-changed";

const readStored = (key: string): string | null => {
  const sessionValue = sessionStorage.getItem(key);
  if (sessionValue !== null) return sessionValue;
  return localStorage.getItem(key);
};

const writeStored = (key: string, value: string, persist: boolean) => {
  if (persist) {
    localStorage.setItem(key, value);
    sessionStorage.removeItem(key);
    return;
  }

  sessionStorage.setItem(key, value);
  localStorage.removeItem(key);
};

const removeStored = (key: string) => {
  sessionStorage.removeItem(key);
  localStorage.removeItem(key);
};

const writeCookie = (name: string, value: string, options?: { maxAge?: number }) => {
  const maxAge = typeof options?.maxAge === "number" ? `; Max-Age=${options.maxAge}` : "";
  document.cookie = `${name}=${encodeURIComponent(value)}; Path=/; SameSite=Lax${maxAge}`;
};

const clearCookie = (name: string) => {
  document.cookie = `${name}=; Path=/; Max-Age=0; SameSite=Lax`;
};

const emitSessionChanged = () => {
  if (typeof window === "undefined") return;
  window.dispatchEvent(new Event(SESSION_CHANGED_EVENT));
};

export const getSessionChangedEventName = () => SESSION_CHANGED_EVENT;

const normalizeSessionUser = (
  value: Partial<SessionUser> & { staffId?: number | string }
): SessionUser | null => {
  const userId =
    typeof value.userId === "string" && value.userId.trim().length > 0
      ? value.userId.trim()
      : value.staffId != null
        ? String(value.staffId)
        : "";

  if (!userId || !value.username || !value.fullName || !value.role) {
    return null;
  }

  return {
    userId,
    username: value.username,
    fullName: value.fullName,
    role: value.role,
    authRole:
      typeof value.authRole === "string" && value.authRole.trim().length > 0
        ? value.authRole.trim()
        : null,
    departmentId:
      typeof value.departmentId === "string" && value.departmentId.trim().length > 0
        ? value.departmentId.trim()
        : null,
    departmentName:
      typeof value.departmentName === "string" && value.departmentName.trim().length > 0
        ? value.departmentName.trim()
        : null,
  };
};

export const getSessionUser = (): SessionUser | null => {
  if (typeof window === "undefined") return null;
  const raw = readStored(USER_KEY);
  if (!raw) return null;

  try {
    return normalizeSessionUser(JSON.parse(raw));
  } catch {
    return null;
  }
};

export const setPasswordChangeRequired = (required: boolean, persist = false) => {
  if (typeof window === "undefined") return;
  writeStored(PASSWORD_CHANGE_REQUIRED_KEY, required ? "1" : "0", persist);
  writeCookie(FORCE_PASSWORD_COOKIE_KEY, required ? "1" : "0");
};

export const isPasswordChangeRequired = (): boolean => {
  if (typeof window === "undefined") return false;
  return readStored(PASSWORD_CHANGE_REQUIRED_KEY) === "1";
};

export const saveSession = (
  user: SessionUser,
  options?: { passwordChangeRequired?: boolean; persist?: boolean }
) => {
  if (typeof window === "undefined") return;

  const persist = Boolean(options?.persist);
  writeStored(USER_KEY, JSON.stringify(user), persist);
  setPasswordChangeRequired(Boolean(options?.passwordChangeRequired), persist);
  emitSessionChanged();
};

const getExistingPersistPreference = () => {
  if (typeof window === "undefined") return false;
  return localStorage.getItem(USER_KEY) !== null;
};

export const saveSessionUserOnly = (
  user: SessionUser,
  options?: { passwordChangeRequired?: boolean; persist?: boolean }
) => {
  if (typeof window === "undefined") return;

  const persist = options?.persist ?? getExistingPersistPreference();
  writeStored(USER_KEY, JSON.stringify(user), persist);
  setPasswordChangeRequired(Boolean(options?.passwordChangeRequired), persist);
  emitSessionChanged();
};

export const clearSession = () => {
  if (typeof window === "undefined") return;
  removeStored(USER_KEY);
  removeStored(PASSWORD_CHANGE_REQUIRED_KEY);
  clearCookie(FORCE_PASSWORD_COOKIE_KEY);
  emitSessionChanged();
};

export const getCookieValue = (name: string): string | null => {
  if (typeof window === "undefined") return null;

  const target = `${name}=`;
  const pieces = document.cookie.split(";");
  for (const piece of pieces) {
    const trimmed = piece.trim();
    if (trimmed.startsWith(target)) {
      return decodeURIComponent(trimmed.substring(target.length));
    }
  }
  return null;
};
