import { useState } from "react";
import Navbar from "../../components/layout/Navbar";
import Sidebar from "../../components/layout/Sidebar";
import {
  Box,
  Container,
  Paper,
  Typography,
  TextField,
  MenuItem,
  Button,
  Stack,
  Alert,
} from "@mui/material";
import { register } from "../../api/registerApi";
import ProfileSidebar from "../../components/layout/ProfileSidebar";

const RegisterPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isProfileSidebarOpen, setProfileSidebarOpen] = useState(false);
  const [serverMessage, setServerMessage] = useState({ type: "", text: "" });
  const [errors, setErrors] = useState({});

  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    username: "",
    email: "",
    password: "",
    role: "USER",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    if (errors[e.target.name]) {
      setErrors({ ...errors, [e.target.name]: "" });
    }
  };

  const validateForm = () => {
    let tempErrors = {};

    if (!formData.firstname?.trim())
      tempErrors.firstname = "First name is required";
    if (!formData.lastname?.trim())
      tempErrors.lastname = "Last name is required";

    if (!formData.username?.trim()) {
      tempErrors.username = "Username is required";
    } else if (formData.username.trim().length < 3) {
      tempErrors.username = "Username must be at least 3 characters long";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email?.trim()) {
      tempErrors.email = "Email is required";
    } else if (!emailRegex.test(formData.email)) {
      tempErrors.email =
        "Please enter a valid email address (ex: user@example.com)";
    }

    if (!formData.password) {
      tempErrors.password = "Password is required";
    } else if (formData.password.length < 6) {
      tempErrors.password = "Password must be at least 6 characters long";
    }

    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setServerMessage({ type: "", text: "" });

    if (!validateForm()) return;

    try {
      const result = await register(formData);
      setServerMessage({
        type: "success",
        text: `User ${result.username || formData.username} created successfully!`,
      });
      setFormData({
        firstname: "",
        lastname: "",
        username: "",
        email: "",
        password: "",
        role: "USER",
      });
    } catch (error) {
      setServerMessage({
        type: "error",
        text: error.message || "Registration failed. Please try again.",
      });
    }
  };

  const handleSidebarToggle = () => setSidebarOpen(!isSidebarOpen);
  const handleProfileClick = () => setProfileSidebarOpen(!isProfileSidebarOpen);

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "#f5f7fa" }}>
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <ProfileSidebar
        open={isProfileSidebarOpen}
        onClose={() => setProfileSidebarOpen(false)}
      />

      <Box
        sx={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Navbar
          onMenuClick={handleSidebarToggle}
          onProfileClick={handleProfileClick}
        />

        <Box
          sx={{
            flexGrow: 1,
            display: "flex",
            justifyContent: "center",
            alignItems: "flex-start",
            p: { xs: 2, sm: 4 },
            overflowY: "auto",
            pt: { xs: "64px", sm: "70px" },
            mt: 4,
          }}
        >
          <Container maxWidth="xs">
            <Paper
              elevation={4}
              sx={{
                p: { xs: 3, sm: 4 },
                borderRadius: 3,
                mt: { xs: 2, sm: 4 },
                mb: 4,
              }}
            >
              <Typography
                variant="h5"
                align="center"
                sx={{ fontWeight: 800, mb: 3, color: "primary.main" }}
              >
                Create Account
              </Typography>

              {serverMessage.text && (
                <Alert
                  severity={serverMessage.type}
                  sx={{ mb: 2, borderRadius: 2 }}
                >
                  {serverMessage.text}
                </Alert>
              )}

              <form onSubmit={handleSubmit} noValidate>
                <Stack spacing={1.5}>
                  <TextField
                    fullWidth
                    size="small"
                    label="First Name"
                    name="firstname"
                    value={formData.firstname}
                    onChange={handleChange}
                    error={!!errors.firstname}
                    helperText={errors.firstname}
                  />
                  <TextField
                    fullWidth
                    size="small"
                    label="Last Name"
                    name="lastname"
                    value={formData.lastname}
                    onChange={handleChange}
                    error={!!errors.lastname}
                    helperText={errors.lastname}
                  />
                  <TextField
                    fullWidth
                    size="small"
                    label="Username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    error={!!errors.username}
                    helperText={errors.username}
                  />
                  <TextField
                    fullWidth
                    size="small"
                    label="Email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    error={!!errors.email}
                    helperText={errors.email}
                  />
                  <TextField
                    fullWidth
                    size="small"
                    label="Password"
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    error={!!errors.password}
                    helperText={errors.password}
                  />
                  <TextField
                    fullWidth
                    size="small"
                    select
                    name="role"
                    label="User Role"
                    value={formData.role}
                    onChange={handleChange}
                  >
                    <MenuItem value="USER">USER</MenuItem>
                    <MenuItem value="MANAGER">MANAGER</MenuItem>
                  </TextField>

                  <Button
                    fullWidth
                    variant="contained"
                    type="submit"
                    size="large"
                    sx={{
                      py: 1.2,
                      fontWeight: "bold",
                      mt: 1,
                      borderRadius: 2,
                      textTransform: "none",
                      fontSize: "1rem",
                    }}
                  >
                    Create Account
                  </Button>
                </Stack>
              </form>
            </Paper>
          </Container>
        </Box>
      </Box>
    </Box>
  );
};

export default RegisterPage;
