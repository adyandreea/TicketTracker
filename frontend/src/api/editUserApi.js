import axios from "axios";
import API_URL from "../config";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };
};

export const getAllUsers = async () => {
  try {
    const response = await axios.get(`${API_URL}/auth/users`, {
      headers: getAuthHeaders(),
    });
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Failed to fetch users" };
  }
};

export const updateUser = async (id, userData) => {
  try {
    const response = await axios.put(`${API_URL}/auth/users/${id}`, userData, {
      headers: getAuthHeaders(),
    });
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Update failed" };
  }
};

export const deleteUser = async (id) => {
  try {
    await axios.delete(`${API_URL}/auth/users/${id}`, {
      headers: getAuthHeaders(),
    });
    return true;
  } catch (err) {
    throw err.response?.data || { message: "Delete failed" };
  }
};

export const getMyProfile = async () => {
  try {
    const response = await axios.get(`${API_URL}/auth/me`, {
      headers: getAuthHeaders(),
    });
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Failed to fetch profile data." };
  }
};

export const updateProfilePicture = async (base64Image) => {
  try {
    const response = await axios.patch(
      `${API_URL}/auth/users/profile-picture`,
      base64Image,
      {
        headers: {
          ...getAuthHeaders(),
          "Content-Type": "text/plain",
        },
      },
    );
    return response.data;
  } catch (err) {
    throw (
      err.response?.data || { message: "Failed to upload profile picture." }
    );
  }
};
