import "server-only";

import type { PositionResponse } from "@/features/staff/position/positiontypes";
import { STAFF_API_BASE_URL } from "@/lib/common/env";
import type {
  StaffDepartmentSummaryItem,
  StaffLocationSummaryItem,
  StaffSummaryItem,
} from "@/lib/staff/staffSummaryApi";

type StaffApiResponse<T> = {
  success?: boolean;
  message?: string;
  result?: T;
  data?: T;
};

type StaffDepartmentRaw = {
  id?: string | number | null;
  name?: string | null;
  deptId?: string | number | null;
  departmentId?: string | number | null;
  departmentName?: string | null;
  activeFlag?: string | null;
};

type PositionRaw = Partial<{
  title: string | null;
  positionCode: string | null;
  domain: string | null;
  isActive: string | null;
  sortOrder: string | number | null;
  doctorType: string | null;
  nurseType: string | null;
  receptionType: string | null;
  jobTitle: string | null;
  positionTitle: string | null;
}>;

export const ACCESS_TOKEN_COOKIE_NAME = "his_access_token";

const toBaseUrl = () => STAFF_API_BASE_URL.replace(/\/+$/, "");

const toUrl = (path: string) =>
  `${toBaseUrl()}${path.startsWith("/") ? path : `/${path}`}`;

const parseJson = async <T>(response: Response): Promise<T | null> => {
  try {
    return (await response.json()) as T;
  } catch {
    return null;
  }
};

const requestStaffApi = async <T>(path: string, accessToken: string): Promise<T> => {
  const response = await fetch(toUrl(path), {
    cache: "no-store",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  const payload = await parseJson<StaffApiResponse<T>>(response);

  if (!response.ok || !payload?.success) {
    throw new Error(
      payload?.message?.trim() ||
        `Staff API request failed. (${response.status})`
    );
  }

  return (payload.result ?? payload.data ?? []) as T;
};

export const fetchInitialStaffSummary = async (
  accessToken: string
): Promise<StaffSummaryItem[]> => {
  return requestStaffApi<StaffSummaryItem[]>("/api/staff/list", accessToken);
};

export const fetchInitialStaffDepartments = async (
  accessToken: string
): Promise<StaffDepartmentSummaryItem[]> => {
  const rows = await requestStaffApi<StaffDepartmentRaw[]>("/api/staff/list", accessToken);
  const seen = new Set<string>();

  return rows.reduce<StaffDepartmentSummaryItem[]>((acc, item) => {
    const departmentId =
      item.departmentId != null
        ? String(item.departmentId)
        : item.deptId != null
          ? String(item.deptId)
          : item.id != null
            ? String(item.id)
            : null;
    const departmentName = item.departmentName ?? item.name ?? departmentId;
    const key = `${departmentId ?? ""}|${departmentName ?? ""}`;

    if (!departmentId || seen.has(key)) {
      return acc;
    }

    seen.add(key);
    acc.push({
      departmentId,
      departmentName,
      activeFlag: item.activeFlag ?? "Y",
    });
    return acc;
  }, []);
};

export const fetchInitialStaffLocations = async (
  _accessToken: string
): Promise<StaffLocationSummaryItem[]> => {
  void _accessToken;
  return [];
};

export const fetchInitialPositionSummary = async (
  accessToken: string
): Promise<PositionResponse[]> => {
  const rows = await requestStaffApi<PositionRaw[]>("/api/staff/list", accessToken);
  const seen = new Set<string>();

  return rows.reduce<PositionResponse[]>((acc, item) => {
    const title =
      item.title ??
      item.positionTitle ??
      item.jobTitle ??
      item.doctorType ??
      item.nurseType ??
      item.receptionType ??
      null;

    if (!title || seen.has(title)) {
      return acc;
    }

    seen.add(title);
    acc.push({
      positionId: title,
      positionName: title,
      positionCode: item.positionCode ?? title,
      positionType: item.domain ?? "",
      positionLevel: item.sortOrder == null ? "" : String(item.sortOrder),
      managerYn: item.isActive ?? "Y",
      rmk: "",
    });
    return acc;
  }, []);
};
