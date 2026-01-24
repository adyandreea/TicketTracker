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

const RegisterPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
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
    if (!formData.firstname) tempErrors.firstname = "First name is required";
    if (!formData.lastname) tempErrors.lastname = "Last name is required";
    if (!formData.username) tempErrors.username = "Username is required";
    if (!formData.email) tempErrors.email = "Email is required";
    if (!formData.password) tempErrors.password = "Password is required";

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

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "#f5f5f5" }}>
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <Box
        sx={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
          height: "100vh",
        }}
      >
        <Navbar onMenuClick={handleSidebarToggle} />

        <Box
          sx={{
            flexGrow: 1,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            p: 2,
          }}
        >
          <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: { xs: 3, sm: 4 }, borderRadius: 2 }}>
              <Typography
                variant="h4"
                align="center"
                gutterBottom
                sx={{ fontWeight: "bold", mb: 3 }}
              >
                Create User Account
              </Typography>

              {serverMessage.text && (
                <Alert
                  severity={serverMessage.type}
                  sx={{ mb: 3, width: "100%" }}
                >
                  {serverMessage.text}
                </Alert>
              )}

              <form onSubmit={handleSubmit} noValidate>
                <Stack spacing={2}>
                  <TextField
                    fullWidth
                    label="First Name"
                    name="firstname"
                    value={formData.firstname}
                    onChange={handleChange}
                    error={!!errors.firstname}
                    helperText={errors.firstname}
                  />
                  <TextField
                    fullWidth
                    label="Last Name"
                    name="lastname"
                    value={formData.lastname}
                    onChange={handleChange}
                    error={!!errors.lastname}
                    helperText={errors.lastname}
                  />
                  <TextField
                    fullWidth
                    label="Username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    error={!!errors.username}
                    helperText={errors.username}
                  />
                  <TextField
                    fullWidth
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
                    select
                    name="role"
                    label="User Role"
                    value={formData.role}
                    onChange={handleChange}
                  >
                    <MenuItem value="USER">User</MenuItem>
                    <MenuItem value="MANAGER">Manager</MenuItem>
                  </TextField>

                  <Button
                    fullWidth
                    variant="contained"
                    type="submit"
                    size="large"
                    sx={{ py: 1.5, fontWeight: "bold", mt: 1 }}
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
