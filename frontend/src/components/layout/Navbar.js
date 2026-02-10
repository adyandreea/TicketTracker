import {
  AppBar,
  Toolbar,
  Box,
  IconButton,
  TextField,
  InputAdornment,
} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";

const Navbar = ({ onMenuClick, onProfileClick }) => {
  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: "secondary",
        backdropFilter: "blur(10px)",
        width: "100%",
        borderBottom: "1px solid rgba(0, 0, 0, 0.08)",
        boxShadow: "0 8px 24px rgba(0,0,0,0.12)",
        zIndex: (theme) => theme.zIndex.drawer - 1,
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          justifyContent: "space-between",
          gap: 2,
          px: { xs: 1, sm: 3 },
        }}
      >
        <IconButton
          edge="start"
          color="inherit"
          onClick={onMenuClick}
          sx={{
            mr: 1,
            "&:hover": { bgcolor: "rgba(0,0,0,0.04)" },
          }}
        >
          <MenuIcon sx={{ color: "white" }} />
        </IconButton>

        <Box sx={{ flexGrow: 1, display: "flex", justifyContent: "center" }}>
          <TextField
            size="small"
            placeholder="Search tasks, projects..."
            variant="outlined"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" sx={{ color: "gray" }} />
                </InputAdornment>
              ),
            }}
            sx={{
              width: { xs: "100%", sm: "80%", md: 500 },
              maxWidth: "700px",
              "& .MuiOutlinedInput-root": {
                bgcolor: "#f5f7fa",
                borderRadius: "12px",
                transition: "all 0.3s ease",
                "& fieldset": {
                  borderColor: "transparent",
                },
                "&:hover": {
                  bgcolor: "#eeeeee",
                },
                "&:hover fieldset": {
                  borderColor: "transparent",
                },
                "&.Mui-focused": {
                  bgcolor: "#fff",
                  boxShadow: "0 4px 20px rgba(0,0,0,0.08)",
                  "& fieldset": {
                    borderColor: "primary.main",
                    borderWidth: "1px",
                  },
                },
              },
            }}
          />
        </Box>

        <Box sx={{ display: "flex", alignItems: "center" }}>
          <IconButton
            edge="start"
            color="inherit"
            onClick={onProfileClick}
            sx={{
              mr: 1,
              "&:hover": { bgcolor: "rgba(0,0,0,0.04)" },
            }}
          >
            <AccountCircleIcon sx={{ fontSize: 32, color: "white" }} />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
