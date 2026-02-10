import axios from "axios";
import API_URL from "../config";

export const register = async (userData) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.post(`${API_URL}/auth/register`, userData, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Registration failed" };
  }
};
