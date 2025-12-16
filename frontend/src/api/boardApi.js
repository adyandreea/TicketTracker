import axios from "axios";
import API_URL from "../config";

const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

const BOARDS_URL = `/boards`;

export const getBoards = async () => {
  try {
    const response = await axiosInstance.get(BOARDS_URL);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error viewing boards." };
  }
};

export const createBoard = async (boardData) => {
  try {
    const response = await axiosInstance.post(BOARDS_URL, boardData);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error creating board." };
  }
};

export const getBoardById = async (id) => {
  try {
    const response = await axiosInstance.get(`${BOARDS_URL}/${id}`);
    return response.data;
  } catch (err) {
    throw (
      err.response?.data || {
        message: `Error taking over the board with ID ${id}.`,
      }
    );
  }
};

export const updateBoard = async (id, boardData) => {
  try {
    const response = await axiosInstance.put(`${BOARDS_URL}/${id}`, boardData);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error updating board." };
  }
};

export const deleteBoard = async (id) => {
  try {
    await axiosInstance.delete(`${BOARDS_URL}/${id}`);
  } catch (err) {
    throw err.response?.data || { message: "Error deleting board." };
  }
};

export const getBoardsByProjectId = async (projectId) => {
  try {
    const response = await axiosInstance.get(
      `${BOARDS_URL}/by-project/${projectId}`
    );
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error loading project boards." };
  }
};
