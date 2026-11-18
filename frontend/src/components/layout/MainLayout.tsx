"use client";

import * as React from "react";
import { Box, CircularProgress, Link } from "@mui/material";
import { usePathname } from "next/navigation";
import RouteAccessGuard from "@/components/auth/RouteAccessGuard";
import { useMenus, useSetMenus } from "@/components/layout/MenuContext";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";
import { getMeApi } from "@/lib/auth/authApi";
import { clearSession, getAccessToken, getSessionUser, saveSessionUserOnly } from "@/lib/auth/session";
import type { MenuNode } from "@/types/menu";

type AuthStatus = "checking" | "ready" | "redirecting";

const redirectToLogin = () => {
  if (typeof window === "undefined") return;
  if (window.location.pathname.startsWith("/login")) return;

  const nextPath = `${window.location.pathname}${window.location.search}`;
  window.location.replace(`/login?next=${encodeURIComponent(nextPath)}`);
};

const getInitialAuthStatus = (pathname: string | null): AuthStatus => {
  const currentPath = pathname || "/";
  if (currentPath.startsWith("/login")) {
    return "ready";
  }

  return "checking";
};

export default function MainLayout({
  children,
  showSidebar = true,
}: {
  children: React.ReactNode;
  showSidebar?: boolean;
}) {
  const pathname = usePathname();
  const NAV_H = { xs: 64, md: 76 };
  const SIDEBAR_W = 240;
  const [authStatus, setAuthStatus] = React.useState<AuthStatus>(() =>
    getInitialAuthStatus(pathname)
  );
  const menus = useMenus();
  const setMenus = useSetMenus();

  React.useEffect(() => {
    let mounted = true;

    const bootstrap = async () => {
      if (mounted) {
        setAuthStatus(getInitialAuthStatus(pathname));
      }

      const currentPath = pathname || "/";
      if (currentPath.startsWith("/login")) {
        if (mounted) setAuthStatus("ready");
        return;
      }

      const token = getAccessToken();
      if (!token) {
        if (mounted) setAuthStatus("redirecting");
        clearSession();
        redirectToLogin();
        return;
      }

      const user = getSessionUser();
      if (!user || !user.authRole) {
        if (mounted) setAuthStatus("checking");
        try {
          const me = await getMeApi();
          if (!mounted) return;
          saveSessionUserOnly(me, { passwordChangeRequired: false });
        } catch {
          if (mounted) setAuthStatus("redirecting");
          clearSession();
          redirectToLogin();
          return;
        }
      }

      if (setMenus && currentPath.startsWith("/admin")) {
        try {
          const response = await fetch("/api/session/menus", {
            cache: "no-store",
            credentials: "same-origin",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          if (response.ok) {
            const payload = (await response.json()) as { menus?: MenuNode[] };
            if (mounted && Array.isArray(payload.menus)) {
              setMenus((prev) => (payload.menus!.length > 0 || prev.length === 0 ? payload.menus! : prev));
            }
          }
        } catch {
          // Keep the last known menu state when refresh fails.
        }
      }

      if (mounted) setAuthStatus("ready");
    };

    void bootstrap();

    return () => {
      mounted = false;
    };
  }, [pathname, setMenus]);

  return (
    <RouteAccessGuard menus={menus}>
      <Box
        sx={{
          minHeight: "100dvh",
          display: "flex",
          flexDirection: "column",
          overflowX: "hidden",
          backgroundImage: [
            "radial-gradient(circle at 12% 8%, rgba(11, 91, 143, 0.18) 0%, rgba(11, 91, 143, 0) 38%)",
            "radial-gradient(circle at 88% 12%, rgba(217, 119, 6, 0.16) 0%, rgba(217, 119, 6, 0) 32%)",
            "linear-gradient(180deg, #eef3f7 0%, #f7fafc 55%, #eef2f7 100%)",
          ].join(", "),
          backgroundAttachment: "fixed",
        }}
      >
        {showSidebar ? (
          <Box
            sx={{
              display: { xs: "none", md: "block" },
              position: "fixed",
              top: { xs: 0, md: `${NAV_H.md}px` },
              left: 0,
              width: { xs: "100%", md: `${SIDEBAR_W}px` },
              height: { xs: "auto", md: `calc(100vh - ${NAV_H.md}px)` },
              overflowY: { md: "auto" },
              zIndex: 1100,
            }}
          >
            <Sidebar width={SIDEBAR_W} menus={menus} />
          </Box>
        ) : null}

        <Navbar menus={showSidebar ? menus : undefined} />
        <Box sx={{ height: NAV_H, flexShrink: 0 }} />
        <Box
          sx={{
            ml: showSidebar ? { xs: 0, md: `${SIDEBAR_W}px` } : 0,
            flex: "1 0 auto",
            px: { xs: 2, md: 4 },
            py: 3,
          }}
        >
          <Box sx={{ width: "100%", maxWidth: "100%", mx: "auto" }}>
            {authStatus === "ready" ? (
              children
            ) : (
              <Box
                sx={{
                  minHeight: "calc(100vh - 180px)",
                  display: "grid",
                  placeItems: "center",
                }}
              >
                <CircularProgress size={28} />
              </Box>
            )}
          </Box>
        </Box>
        {authStatus === "ready" && !pathname?.startsWith("/login") ? (
          <Box
            component="footer"
            sx={{
              ml: showSidebar ? { xs: 0, md: SIDEBAR_W + "px" } : 0,
              px: { xs: 2, md: 4 },
              py: 1.25,
              borderTop: 1,
              borderColor: "divider",
              color: "text.secondary",
              typography: "body2",
            }}
          >
            <Link
              href="https://github.com/babo99con/final_his#readme"
              color="inherit"
              underline="always"
            >
              프로젝트 문서
            </Link>
          </Box>
        ) : null}
      </Box>
    </RouteAccessGuard>
  );
}
