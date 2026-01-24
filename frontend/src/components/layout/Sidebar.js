import { useState, useEffect } from "react";
import {
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  useMediaQuery,
  useTheme,
  Divider,
  Box,
  Collapse,
} from "@mui/material";
import {
  Dashboard as DashboardIcon,
  AccountTree as ProjectIcon,
  ViewKanban as BoardIcon,
  AdminPanelSettings as AdminIcon,
  ExpandLess,
  ExpandMore,
  PersonAdd as CreateUserIcon,
  PersonRemove as DeleteUserIcon,
} from "@mui/icons-material";
import { useNavigate, useLocation } from "react-router-dom";

const Sidebar = ({ open, onClose }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const [adminOpen, setAdminOpen] = useState(false);

  const isSubPageActive =
    location.pathname === "/register" || location.pathname === "/delete-user";

  useEffect(() => {
    if (isSubPageActive) {
      setAdminOpen(true);
    }
  }, [location.pathname, isSubPageActive]);

  const handleAdminClick = (e) => {
    e.stopPropagation();
    setAdminOpen(!adminOpen);
  };

  const handleNavigate = (path) => {
    navigate(path);
    if (isMobile) onClose();
  };

  const mainMenuItems = [
    { text: "Dashboard", icon: <DashboardIcon />, path: "/dashboard" },
    { text: "Projects", icon: <ProjectIcon />, path: "/projects" },
    { text: "Boards", icon: <BoardIcon />, path: "/boards" },
  ];

  const getButtonStyle = (path) => {
    const isActive = location.pathname === path;
    return {
      mb: 1,
      borderRadius: "12px",
      backgroundColor: isActive ? "rgba(25, 118, 210, 0.08)" : "transparent",
      color: isActive ? theme.palette.primary.main : "text.primary",
      "&:hover": {
        backgroundColor: "rgba(0, 0, 0, 0.04)",
        transform: "translateX(5px)",
      },
      transition: "all 0.2s ease",
    };
  };

  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      sx={{
        "& .MuiDrawer-paper": {
          width: { xs: "80%", sm: "320px", md: "340px" },
          height: isMobile ? "100vh" : "85vh",
          top: isMobile ? 0 : "7.5vh",
          left: isMobile ? 0 : "20px",
          borderRadius: isMobile ? 0 : "24px",
          bgcolor: "#ffffff",
          boxShadow: "0 10px 30px rgba(0,0,0,0.1)",
          border: "none",
          overflowX: "hidden",
        },
      }}
    >
      <Toolbar sx={{ my: 2, display: "flex", justifyContent: "center" }}>
        <Box sx={{ textAlign: "center" }}>
          <Typography
            variant="h5"
            sx={{
              fontWeight: 800,
              color: theme.palette.primary.main,
              letterSpacing: 1,
            }}
          >
            KANBAN
          </Typography>
          <Typography
            variant="caption"
            sx={{
              color: "text.secondary",
              fontWeight: 700,
              textTransform: "uppercase",
              letterSpacing: 0.5,
            }}
          >
            Project Management
          </Typography>
        </Box>
      </Toolbar>

      <Divider sx={{ mx: 3, mb: 2 }} />

      <List sx={{ px: 2 }}>
        {mainMenuItems.map((item) => (
          <ListItemButton
            key={item.text}
            onClick={() => handleNavigate(item.path)}
            sx={getButtonStyle(item.path)}
          >
            <ListItemIcon
              sx={{
                color:
                  location.pathname === item.path
                    ? theme.palette.primary.main
                    : "inherit",
                minWidth: "45px",
              }}
            >
              {item.icon}
            </ListItemIcon>
            <ListItemText
              primary={item.text}
              primaryTypographyProps={{
                fontSize: "16px",
                fontWeight: location.pathname === item.path ? 700 : 500,
              }}
            />
          </ListItemButton>
        ))}

        <ListItemButton
          onClick={handleAdminClick}
          sx={{
            borderRadius: "12px",
            mb: 1,

            color:
              isSubPageActive || adminOpen
                ? theme.palette.primary.main
                : "text.primary",
            backgroundColor:
              isSubPageActive || adminOpen
                ? "rgba(25, 118, 210, 0.08)"
                : "transparent",
            "&:hover": {
              backgroundColor:
                isSubPageActive || adminOpen
                  ? "rgba(25, 118, 210, 0.12)"
                  : "rgba(0, 0, 0, 0.04)",
              transform: "translateX(5px)",
            },
            transition: "all 0.2s ease",
          }}
        >
          <ListItemIcon
            sx={{
              minWidth: "45px",
              color: isSubPageActive ? theme.palette.primary.main : "inherit",
            }}
          >
            <AdminIcon />
          </ListItemIcon>
          <ListItemText
            primary="Administration"
            primaryTypographyProps={{
              fontSize: "16px",
              fontWeight: isSubPageActive ? 700 : 500,
            }}
          />
          {adminOpen ? <ExpandLess /> : <ExpandMore />}
        </ListItemButton>

        <Collapse in={adminOpen} timeout="auto" unmountOnExit>
          <List component="div" disablePadding sx={{ pl: 3 }}>
            <ListItemButton
              onClick={() => handleNavigate("/register")}
              sx={getButtonStyle("/register")}
            >
              <ListItemIcon
                sx={{
                  minWidth: "40px",
                  color:
                    location.pathname === "/register"
                      ? theme.palette.primary.main
                      : "inherit",
                }}
              >
                <CreateUserIcon fontSize="small" />
              </ListItemIcon>
              <ListItemText
                primary="Create User"
                primaryTypographyProps={{ fontSize: "14px" }}
              />
            </ListItemButton>

            <ListItemButton
              onClick={() => handleNavigate("/delete-user")}
              sx={getButtonStyle("/delete-user")}
            >
              <ListItemIcon
                sx={{
                  minWidth: "40px",
                  color:
                    location.pathname === "/delete-user"
                      ? theme.palette.primary.main
                      : "inherit",
                }}
              >
                <DeleteUserIcon fontSize="small" />
              </ListItemIcon>
              <ListItemText
                primary="Delete User"
                primaryTypographyProps={{ fontSize: "14px" }}
              />
            </ListItemButton>
          </List>
        </Collapse>
      </List>
    </Drawer>
  );
};

export default Sidebar;
