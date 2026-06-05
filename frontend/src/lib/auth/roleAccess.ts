export const normalizeRole = (role?: string | null) => {
  const normalized = (role || "").toUpperCase();
  if (normalized.includes("ADMIN") || normalized.includes("관리자")) return "ADMIN";
  if (normalized.includes("DOCTOR") || normalized.includes("의사")) return "DOCTOR";
  if (normalized.includes("NURSE") || normalized.includes("간호")) return "NURSE";
  if (normalized.includes("RECEPTION") || normalized.includes("원무")) return "RECEPTION";
  if (normalized.includes("STAFF") || normalized.includes("직원") || normalized.includes("스탭")) return "STAFF";
  return "UNKNOWN";
};

export const deriveOperationalRole = (
  authRole?: string | null,
  domainRole?: string | null,
  positionName?: string | null,
  departmentName?: string | null
) => {
  const base = normalizeRole(authRole);
  if (base !== "STAFF") return base;

  const fromDomain = normalizeRole(domainRole);
  if (fromDomain !== "STAFF" && fromDomain !== "UNKNOWN") return fromDomain;

  const position = (positionName || "").toLowerCase();
  if (position.includes("의사") || position.includes("doctor")) return "DOCTOR";
  if (position.includes("간호") || position.includes("nurse")) return "NURSE";
  if (
    position.includes("원무") ||
    position.includes("접수") ||
    position.includes("reception")
  ) {
    return "RECEPTION";
  }

  const department = (departmentName || "").toLowerCase();
  if (department.includes("진료") || department.includes("의국") || department.includes("doctor")) {
    return "DOCTOR";
  }
  if (department.includes("간호") || department.includes("nurse")) {
    return "NURSE";
  }
  if (department.includes("원무") || department.includes("접수") || department.includes("reception")) {
    return "RECEPTION";
  }

  return "STAFF";
};
