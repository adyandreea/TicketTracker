import {
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  Toolbar,
  Typography,
  useMediaQuery,
  useTheme
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const Sidebar = ({ open, onClose }) => {
  const navigate = useNavigate();
  const theme = useTheme();

  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const handleNavigate = (path) => {
        navigate(path);
        onClose(); 
    };
  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      sx={{
        "& .MuiDrawer-paper": {
          width: { 
            xs: "80%",    
            sm: "300px",  
            md: "350px"   
          },
          height: isMobile ? "100vh" : "80vh", 
          top: isMobile ? 0 : "10vh",          
          left: isMobile ? 0 : "16px",         
          borderRadius: isMobile ? 0 : "16px", 
          bgcolor: "#E0E0E0",
          boxShadow: "0 4px 16px rgba(0,0,0,0.2)",
          transition: "all 0.3s ease", 
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
                fontSize: { xs: "16px", sm: "18px" },
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
