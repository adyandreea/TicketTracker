import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "../features/auth/LoginPage";
import DashboardPage from "../features/dashboard/DashboardPage";
import ProjectPage from "../features/pages/ProjectPage.js";
import BoardPage from "../features/pages/BoardPage.js";

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/projects" element={<ProjectPage/>}/>
        <Route path="/boards" element={<BoardPage/>}/>
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;
