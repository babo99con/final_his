const requireEnv = (name: string, value: string | undefined) => {
  const trimmed = value?.trim();
  if (!trimmed) {
    throw new Error(`[env] Missing required environment variable: ${name}`);
  }
  return trimmed;
};

export const AUTH_API_BASE_URL = requireEnv(
  "NEXT_PUBLIC_AUTH_API_BASE_URL",
  process.env.NEXT_PUBLIC_AUTH_API_BASE_URL
);

export const MENU_API_BASE_URL = requireEnv(
  "NEXT_PUBLIC_MENU_API_BASE_URL",
  process.env.NEXT_PUBLIC_MENU_API_BASE_URL
);

export const BILLING_API_BASE_URL = requireEnv(
  "NEXT_PUBLIC_BILLING_API_BASE_URL",
  process.env.NEXT_PUBLIC_BILLING_API_BASE_URL
);

export const STAFF_API_BASE_URL = requireEnv(
  "NEXT_PUBLIC_STAFF_API_BASE_URL",
  process.env.NEXT_PUBLIC_STAFF_API_BASE_URL
);

export const NOTIFICATION_API_BASE_URL = requireEnv(
  "NEXT_PUBLIC_NOTIFICATION_API_BASE_URL",
  process.env.NEXT_PUBLIC_NOTIFICATION_API_BASE_URL
);
