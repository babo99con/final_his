"use client";

import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Stack,
  Button,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemText,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import MedicalServicesOutlinedIcon from "@mui/icons-material/MedicalServicesOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import Link from "next/link";
import { usePathname } from "next/navigation";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import Sidebar from "./Sidebar";
import type { MenuNode } from "@/types/menu";
import * as React from "react";
import { logoutApi } from "@/lib/auth/authApi";
import {
  clearSession,
  getSessionChangedEventName,
  getSessionUser,
  type SessionUser,
} from "@/lib/auth/session";

export default function Navbar({ menus }: { menus?: MenuNode[] }) {
  const [menuOpen, setMenuOpen] = React.useState(false);
  const pathname = usePathname();
  const theme = useTheme();
  const desktop = useMediaQuery(theme.breakpoints.up("md"));

  React.useEffect(() => {
    setMenuOpen(false);
  }, [pathname, desktop]);
  const [sessionUser, setSessionUser] = React.useState<SessionUser | null>(null);

  React.useEffect(() => {
    const syncSessionUser = () => {
      setSessionUser(getSessionUser());
    };

    syncSessionUser();

    const eventName = getSessionChangedEventName();
    window.addEventListener(eventName, syncSessionUser);
    return () => window.removeEventListener(eventName, syncSessionUser);
  }, []);

  const handleLogout = async () => {
    try {
      await logoutApi();
    } catch {
      // 서버 로그아웃 실패 시에도 로컬 세션은 정리
    } finally {
      clearSession();
      window.location.replace("/login");
    }
  };

  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: "transparent",
        background:
          "linear-gradient(90deg, rgba(11, 91, 143, 0.96) 0%, rgba(10, 62, 98, 0.96) 70%)",
        borderBottom: "1px solid rgba(255,255,255,0.16)",
        backdropFilter: "blur(8px)",
        zIndex: 1200,
      }}
    >
      <Toolbar
        sx={{
          minHeight: { xs: 64, md: 76 },
        }}
      >
        <Stack
          direction="row"
          spacing={1.5}
          alignItems="center"
          sx={{ minWidth: 0, mr: { xs: 1, md: 3 }, textDecoration: "none" }}
          component={Link}
          href="/"
        >
          <Box
            sx={{
              flexShrink: 0,
              width: 38,
              height: 38,
              borderRadius: 1.5,
              bgcolor: "rgba(255,255,255,0.16)",
              display: "grid",
              placeItems: "center",
              border: "1px solid rgba(255,255,255,0.2)",
            }}
          >
            <MedicalServicesOutlinedIcon sx={{ color: "#fff" }} />
          </Box>

          <Box sx={{ minWidth: 0 }}>
            <Typography
              variant="h6"
              noWrap
              sx={{ color: "#fff", fontWeight: 800, letterSpacing: 0.4, fontSize: { xs: 16, md: 20 } }}
            >
              HMS Workspace
            </Typography>
            <Typography sx={{ display: { xs: "none", md: "block" }, color: "rgba(255,255,255,0.7)", fontSize: 12 }}>
              역할 기반 병원관리 시스템
            </Typography>
          </Box>
        </Stack>

        <Box sx={{ flexGrow: 1 }} />

        <Button
          color="inherit"
          startIcon={<MenuIcon />}
          aria-expanded={menuOpen}
          aria-controls={menuOpen ? "mobile-navigation" : undefined}
          onClick={() => setMenuOpen(true)}
          sx={{ display: { xs: "inline-flex", md: "none" }, flexShrink: 0, minHeight: 44 }}
        >
          메뉴
        </Button>

        <Stack direction="row" spacing={2} alignItems="center" sx={{ display: { xs: "none", md: "flex" } }}>
          <Stack direction="row" spacing={1} alignItems="center">
            <PersonOutlineOutlinedIcon sx={{ color: "#dbe8ff" }} />
            <Typography sx={{ color: "#e8f1ff", fontSize: 14, fontWeight: 600 }}>
              {sessionUser?.fullName || sessionUser?.username || "내 계정"}
            </Typography>
            {sessionUser?.departmentName ? (
              <Typography sx={{ color: "#cbd9f5", fontSize: 12 }}>
                {sessionUser.departmentName}
              </Typography>
            ) : null}
          </Stack>

          <Button
            component={Link}
            href="/mypage"
            variant="outlined"
            sx={{
              color: "#e8f1ff",
              borderColor: "rgba(232, 241, 255, 0.32)",
              fontWeight: 700,
              minWidth: 88,
              "&:hover": {
                borderColor: "#e8f1ff",
                backgroundColor: "rgba(255,255,255,0.08)",
              },
            }}
          >
            내 정보
          </Button>

          <Button
            onClick={handleLogout}
            variant="outlined"
            sx={{
              color: "#e8f1ff",
              borderColor: "rgba(232, 241, 255, 0.45)",
              fontWeight: 700,
              minWidth: 88,
              "&:hover": {
                borderColor: "#e8f1ff",
                backgroundColor: "rgba(255,255,255,0.08)",
              },
            }}
          >
            로그아웃
          </Button>
        </Stack>
      </Toolbar>
      <Drawer
        anchor="right"
        open={menuOpen && !desktop}
        onClose={() => setMenuOpen(false)}
        slotProps={{
          paper: {
            id: "mobile-navigation",
            role: "dialog",
            "aria-modal": true,
            "aria-labelledby": "mobile-navigation-title",
            sx: { width: "min(320px, 90vw)", overflowY: "auto" },
          },
        }}
      >
        <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ px: 2, minHeight: 64 }}>
          <Typography id="mobile-navigation-title" fontWeight={700}>메뉴</Typography>
          <IconButton aria-label="메뉴 닫기" onClick={() => setMenuOpen(false)} sx={{ minWidth: 44, minHeight: 44 }}>
            <CloseIcon />
          </IconButton>
        </Stack>
        <Divider />
        <List aria-label="계정">
          <ListItemButton component={Link} href="/mypage" onClick={() => setMenuOpen(false)}>
            <ListItemText primary="내 정보" secondary={sessionUser?.fullName || sessionUser?.username} />
          </ListItemButton>
          <ListItemButton onClick={handleLogout}>
            <ListItemText primary="로그아웃" />
          </ListItemButton>
        </List>
        {menus ? (
          <>
            <Divider />
            <Box component="nav" aria-label="병원 운영 메뉴">
              <Sidebar menus={menus} width="100%" onNavigate={() => setMenuOpen(false)} />
            </Box>
          </>
        ) : null}
      </Drawer>
    </AppBar>
  );
}
