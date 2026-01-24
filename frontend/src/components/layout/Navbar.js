import { AppBar, Toolbar, Box, IconButton, TextField } from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import MenuIcon from "@mui/icons-material/Menu";

const Navbar = ({ onMenuClick }) => {
  return (
    <AppBar
      position="static"
      elevation={0}
      sx={{
        bgcolor: "#DCDCDC",
        color: "black",
        mt: 0,
        width: "100%",
      }}
    >
      <Toolbar
        sx={{ display: "flex", justifyContent: "space-between", gap: 2 }}
      >
        <IconButton edge="start" color="inherit" onClick={onMenuClick}>
          <MenuIcon />
        </IconButton>

        <Box sx={{ flexGrow: 1, display: "flex", justifyContent: "center" }}>
          <TextField
            size="small"
            placeholder="Search"
            variant="outlined"
            sx={{
              bgcolor: "#DCDCDC",
              borderRadius: 2,
              width: { xs: "100%", sm: "80%", md: 600 },
              maxWidth: "900px",
              "& .MuiOutlinedInput-root": {
                boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                "& fieldset": {
                  borderColor: "white",
                  borderWidth: 1,
                },
                "&:hover fieldset": {
                  borderColor: "white",
                },
                "&.Mui-focused fieldset": {
                  borderColor: "white",
                },
              },
            }}
          />
        </Box>
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <IconButton>
            <AccountCircleIcon />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
