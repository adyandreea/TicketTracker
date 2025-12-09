import {
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  Toolbar,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const drawerWidth = 450;

const Sidebar = ({ open, onClose }) => {
  const navigate = useNavigate();

  const handleNavigate = (path) => {
        navigate(path);
        onClose(); 
    };
  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      slotProps={{
        backdrop: {
          sx: {
            backgroundColor: "transparent",
          },
        },
      }}
      sx={{
        width: drawerWidth,
        "& .MuiDrawer-paper": {
          width: drawerWidth,
          height: "80vh",
          top: "10vh",
          left: "16px",
          bgcolor: "#E0E0E0",
          color: "black",
          borderRadius: "16px",
          boxShadow: "0 4px 16px rgba(0,0,0,0.2)",
        },
      }}
    >
      <Toolbar>
        <Typography variant="h6" sx={{ fontWeight: "bold" }}>
          Kanban App
        </Typography>
      </Toolbar>

      <List>
        <ListItemButton onClick={() => handleNavigate('/dashboard')}>
          <ListItemText
            primary="Dashboard"
            sx={{
              "& .MuiListItemText-primary": {
                fontSize: "18px",
                fontWeight: "bold",
                color: "black",
              },
            }}
          />
        </ListItemButton >

        <ListItemButton onClick={() => handleNavigate('/projects')}>
          <ListItemText
            primary="Projects"
            sx={{
              "& .MuiListItemText-primary": {
                fontSize: "18px",
                fontWeight: "bold",
                color: "black",
              },
            }}
          />
        </ListItemButton>

        <ListItemButton onClick={() => handleNavigate('/boards')}>
          <ListItemText
            primary="Boards"
            sx={{
              "& .MuiListItemText-primary": {
                fontSize: "18px",
                fontWeight: "bold",
                color: "black",
              },
            }}
          />
        </ListItemButton>

        <ListItemButton>
          <ListItemText
            primary="Administration"
            sx={{
              "& .MuiListItemText-primary": {
                fontSize: "18px",
                fontWeight: "bold",
                color: "black",
              },
            }}
          />
        </ListItemButton>
      </List>
    </Drawer>
  );
};

export default Sidebar;
