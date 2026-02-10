import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "../features/auth/LoginPage";
import DashboardPage from "../features/dashboard/DashboardPage";
import ProjectPage from "../features/pages/ProjectPage.js";
import BoardPage from "../features/pages/BoardPage.js";
import RegisterPage from "../features/pages/RegisterPage.js";
import EditUserPage from "../features/pages/EditUserPage.js";
import ProtectedRoute from "../utils/ProtectedRoute";
import LogoutHandler from "../utils/LogoutHandler";

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/projects"
          element={
            <ProtectedRoute>
              <ProjectPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/boards"
          element={
            <ProtectedRoute>
              <BoardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/register"
          element={
            <ProtectedRoute>
              <RegisterPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/edit-user"
          element={
            <ProtectedRoute>
              <EditUserPage />
            </ProtectedRoute>
          }
        />
        <Route path="/logout" element={<LogoutHandler />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;
