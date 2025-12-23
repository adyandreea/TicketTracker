import { useState } from "react";
import {
  Avatar,
  Box,
  Container,
  Paper,
  Typography,
  TextField,
  Checkbox,
  FormControlLabel,
  Button,
  Alert,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { login } from "../../api/authApi";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const [serverError, setServerError] = useState("");
  const [errors, setErrors] = useState({
    username: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setServerError("");

    const data = new FormData(event.currentTarget);
    const username = data.get("username").toString() || "";
    const password = data.get("password").toString() || "";

    const validation = { username: "", password: "" };

    if (!username) {
      validation.username = "Username is required";
    }

    if (!password) {
      validation.password = "Password is required";
    }

    setErrors(validation);

    const hasErrors = validation.username !== "" || validation.password !== "";
    if (hasErrors) {
      return;
    }

    try {
      const response = await login(username, password);
      console.log(response);
      console.log("Login successful", response.data);
      localStorage.setItem("token", response.token);
      navigate("/dashboard");
    } catch (err) {
      if (err.status === 401 || err.statusCode === 401 || err.message === "Unauthorized") {
        setServerError("Invalid username or password");
      } else {
        setServerError("Something went wrong. Please try again later.");
      }
    }
  };

  return (
    <Container maxWidth="xs">
      <Paper
        elevation={10}
        sx={{ marginTop: "40%", marginBottom: "40%", padding: 2 }}
      >
        <Avatar
          sx={{
            mx: "auto",
            bgcolor: "secondary.main",
            textAlign: "center",
            marginBottom: 1,
          }}
        >
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5" sx={{ textAlign: "center" }}>
          Sign In
        </Typography>
        {serverError && (
          <Alert severity="error" sx={{ mt: 2}}>
            {serverError}
          </Alert>
        )}
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <TextField
            name="username"
            placeholder="Enter username"
            fullWidth
            required
            autoFocus
            error={errors.username !== ""}
            helperText={errors.username}
            sx={{ mb: 2 }}
          />
          <TextField
            name="password"
            placeholder="Enter password"
            fullWidth
            required
            error={errors.password !== ""}
            helperText={errors.password}
            type="password"
          />
          <FormControlLabel
            control={<Checkbox value="remember" color="primary" />}
            label="Remember me"
          />
          <Button type="submit" variant="contained" fullWidth sx={{ mt: 1 }}>
            Sign In
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default LoginPage;
