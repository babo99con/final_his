import axios from "axios";
import type { ApiResponse } from "@/features/patients/patientTypes";
import { applyAuthInterceptors } from "@/lib/auth/apiInterceptors";
import { AUTH_API_BASE_URL } from "@/lib/common/env";

type LoginRequest = {
  username: string;
  password: string;
};

type PasswordResetPrepareRequest = {
  userIdentifier: string;
  name: string;
};

type PasswordResetConfirmRequest = {
  userIdentifier: string;
  phone: string;
};

export type AuthUser = {
  userId: string;
  username: string;
  fullName: string;
  role: string;
  authRole?: string | null;
  departmentId: string | null;
  departmentName: string | null;
};

export type LoginResult = {
  user: AuthUser;
  passwordChangeRequired: boolean;
};

export type PasswordResetPrepareResult = {
  userId: string;
  loginId: string;
  maskedPhone: string;
};

const api = axios.create({
  // In the browser we go through Next rewrites so auth cookies are issued for the app origin.
  baseURL: typeof window === "undefined" ? AUTH_API_BASE_URL : "",
});

applyAuthInterceptors(api, {
  skipRedirectPaths: [
    "/api/auth/login",
    "/api/auth/password/reset/prepare",
    "/api/auth/password/reset/confirm",
  ],
});

const extractApiMessage = (error: unknown): string | null => {
  if (!axios.isAxiosError(error)) {
    return null;
  }

  const responseData = error.response?.data;
  if (!responseData || typeof responseData !== "object") {
    return null;
  }

  const apiMessage = (responseData as Record<string, unknown>).message;
  return typeof apiMessage === "string" ? apiMessage : null;
};

const PASSWORD_CHANGE_MESSAGE_MAP: Record<string, string> = {
  AUTH_CURRENT_PASSWORD_INVALID: "?꾩옱 鍮꾨?踰덊샇媛 ?щ컮瑜댁? ?딆뒿?덈떎.",
  AUTH_PASSWORD_SAME_AS_CURRENT: "??鍮꾨?踰덊샇???꾩옱 鍮꾨?踰덊샇? ?щ씪???⑸땲??",
  AUTH_PASSWORD_TOO_SHORT: "??鍮꾨?踰덊샇??8???댁긽?댁뼱???⑸땲??",
  AUTH_CURRENT_PASSWORD_REQUIRED: "?꾩옱 鍮꾨?踰덊샇瑜??낅젰??二쇱꽭??",
  AUTH_NEW_PASSWORD_REQUIRED: "??鍮꾨?踰덊샇瑜??낅젰??二쇱꽭??",
  AUTH_PASSWORD_CHANGED: "鍮꾨?踰덊샇媛 蹂寃쎈릺?덉뒿?덈떎.",
};

const resolvePasswordChangeMessage = (
  codeOrMessage: string | null | undefined,
  fallbackMessage: string
) => {
  const normalized = (codeOrMessage ?? "").trim();
  if (!normalized) {
    return fallbackMessage;
  }

  return PASSWORD_CHANGE_MESSAGE_MAP[normalized] ?? normalized;
};

const normalizeApiError = (error: unknown, fallbackMessage: string) => {
  const apiMessage = extractApiMessage(error);
  if (apiMessage) {
    return new Error(apiMessage);
  }

  if (error instanceof Error) {
    return error;
  }

  return new Error(fallbackMessage);
};

export const loginApi = async (payload: LoginRequest): Promise<LoginResult> => {
  const res = await api.post<ApiResponse<LoginResult>>("/api/auth/login", payload);
  if (!res.data.success || !res.data.result) {
    throw new Error(res.data.message || "濡쒓렇?몄뿉 ?ㅽ뙣?덉뒿?덈떎.");
  }
  return res.data.result;
};

export const getMeApi = async (): Promise<AuthUser> => {
  const res = await api.get<ApiResponse<AuthUser>>("/api/auth/me");
  if (!res.data.success || !res.data.result) {
    throw new Error(res.data.message || "?몄쬆 ?뺣낫媛 ?좏슚?섏? ?딆뒿?덈떎.");
  }
  return res.data.result;
};

export const changeMyPasswordApi = async (
  currentPassword: string,
  newPassword: string
): Promise<string> => {
  try {
    const res = await api.patch<ApiResponse<void>>("/api/auth/me/password", {
      currentPassword,
      newPassword,
    });

    if (!res.data.success) {
      throw new Error(res.data.message || "鍮꾨?踰덊샇 蹂寃쎌뿉 ?ㅽ뙣?덉뒿?덈떎.");
    }

    return resolvePasswordChangeMessage(
      res.data.message,
      "鍮꾨?踰덊샇媛 蹂寃쎈릺?덉뒿?덈떎."
    );
  } catch (error) {
    throw new Error(
      resolvePasswordChangeMessage(
        extractApiMessage(error) ?? (error instanceof Error ? error.message : null),
        "鍮꾨?踰덊샇 蹂寃쎌뿉 ?ㅽ뙣?덉뒿?덈떎."
      )
    );
  }
};

export const logoutApi = async (): Promise<void> => {
  await api.post("/api/auth/logout");
};

export const preparePasswordReset = async (
  userIdentifier: string,
  name: string
): Promise<PasswordResetPrepareResult> => {
  const payload: PasswordResetPrepareRequest = {
    userIdentifier: userIdentifier.trim(),
    name: name.trim(),
  };

  try {
    const res = await api.post<ApiResponse<PasswordResetPrepareResult>>(
      "/api/auth/password/reset/prepare",
      payload
    );

    if (!res.data.success || !res.data.result) {
      throw new Error(res.data.message || "鍮꾨?踰덊샇 珥덇린??以鍮꾩뿉 ?ㅽ뙣?덉뒿?덈떎.");
    }

    return res.data.result;
  } catch (error) {
    throw normalizeApiError(error, "鍮꾨?踰덊샇 珥덇린??以鍮꾩뿉 ?ㅽ뙣?덉뒿?덈떎.");
  }
};

export const confirmPasswordReset = async (
  userIdentifier: string,
  phone: string
): Promise<void> => {
  const payload: PasswordResetConfirmRequest = {
    userIdentifier: userIdentifier.trim(),
    phone: phone.trim(),
  };

  try {
    const res = await api.post<ApiResponse<unknown>>(
      "/api/auth/password/reset/confirm",
      payload
    );

    if (!res.data.success) {
      throw new Error(res.data.message || "鍮꾨?踰덊샇 珥덇린?붿뿉 ?ㅽ뙣?덉뒿?덈떎.");
    }
  } catch (error) {
    throw normalizeApiError(error, "鍮꾨?踰덊샇 珥덇린?붿뿉 ?ㅽ뙣?덉뒿?덈떎.");
  }
};

