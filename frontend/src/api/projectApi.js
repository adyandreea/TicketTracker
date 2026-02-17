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
  },
);

const PROJECTS_URL = `/projects`;

export const getProjects = async () => {
  try {
    const response = await axiosInstance.get(PROJECTS_URL);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error viewing projects." };
  }
};

export const createProject = async (projectData) => {
  try {
    const response = await axiosInstance.post(PROJECTS_URL, projectData);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error creating project." };
  }
};

export const getProjectById = async (id) => {
  try {
    const response = await axiosInstance.get(`${PROJECTS_URL}/${id}`);
    return response.data;
  } catch (err) {
    throw (
      err.response?.data || {
        message: `Error taking over the project with ID ${id}.`,
      }
    );
  }
};

export const updateProject = async (id, projectData) => {
  try {
    const response = await axiosInstance.put(
      `${PROJECTS_URL}/${id}`,
      projectData,
    );
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error updating project." };
  }
};

export const deleteProject = async (id) => {
  try {
    await axiosInstance.delete(`${PROJECTS_URL}/${id}`);
  } catch (err) {
    throw err.response?.data || { message: "Error deleting project." };
  }
};

export const assignUserToProject = async (projectId, userId) => {
  try {
    const response = await axiosInstance.post(
      `${PROJECTS_URL}/${projectId}/users/${userId}`,
      { userId },
    );
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error assigning user to project." };
  }
};

export const getProjectMembers = async (id) => {
  try {
    const response = await axiosInstance.get(`${PROJECTS_URL}/${id}/members`);
    return response.data;
  } catch (err) {
    throw err.response?.data || { message: "Error fetching project members." };
  }
};

export const removeUserFromProject = async (projectId, userId) => {
  try {
    const response = await axiosInstance.delete(
      `${PROJECTS_URL}/${projectId}/users/${userId}`,
      { userId },
    );
    return response.data;
  } catch (err) {
    throw (
      err.response?.data || { message: "Error removing user from project." }
    );
  }
};
