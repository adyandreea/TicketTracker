import axios from "axios";
import { API_URL } from "../config";

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
