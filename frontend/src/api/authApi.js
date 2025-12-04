import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/auth";

export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}/authenticate`, {
      username,
      password,
    });
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Login failed" };
  }
};
