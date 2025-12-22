import { useState, useEffect } from "react";
import { Box, Typography, Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import ProjectModal from "../../components/project/ProjectModal";
import ProjectCard from "../../components/project/ProjectCard";
import {
  getProjects,
  createProject,
  updateProject,
} from "../../api/projectApi";

const ProjectsPage = () => {
  const [projects, setProjects] = useState([]);
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);

  const [newProjectName, setNewProjectName] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingProject, setEditingProject] = useState(null);
  const [newProjectDescription, setNewProjectDescription] = useState("");

  const [errors, setErrors] = useState({name: ""});

  const fetchProjects = async () => {
    try {
      const data = await getProjects();
      setProjects(data);
    } catch (error) {
      console.error("Fetch projects error:", error.message || error);
    }
  };

  useEffect(() => {
    fetchProjects();
  }, []);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setIsEditing(false);
    setEditingProject(null);
    setNewProjectName("");
    setNewProjectDescription("");
    setErrors({name: ""});
  };

  const handleCreate = async () => {
    if (newProjectName.trim() === "") return;

    const newProjectRequest = {
      name: newProjectName,
      description: newProjectDescription || "",
    };

    try {
      const createdProject = await createProject(newProjectRequest);
      const completeProject = {
        ...createdProject,
        ...newProjectRequest,
        id: createdProject.id,
      };
      setProjects([...projects, completeProject]);
      handleCloseModal();
    } catch (error) {
      console.error("Create project error:", error.message || error);
    }
  };

  const handleEditStart = (project) => {
    setEditingProject(project);
    setNewProjectName(project.name);
    setNewProjectDescription(project.description || "");
    setIsEditing(true);
    setModalOpen(true);
    setErrors({name: ""});
  };

  const handleEditSave = async () => {
    const updateRequest = {
      name: newProjectName,
      description: newProjectDescription,
    };

    try {
      const updatedProject = await updateProject(
        editingProject.id,
        updateRequest
      );

      setProjects(
        projects.map((p) => (p.id === editingProject.id ? updatedProject : p))
      );
      handleCloseModal();
    } catch (error) {
      console.error("Update project error:", error.message || error);
    }
  };

  const handleSubmit = () => {
    if (newProjectName.trim() === "") {
      setErrors({name: "Project name is required"});
      return;
    }

    setErrors({name: ""});

    if (isEditing) {
      handleEditSave();
    } else {
      handleCreate();
    }
  };

  return (
    <Box sx={{ display: "flex", height: "100vh", backgroundColor: "#f0f0f0" }}>
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <Navbar onMenuClick={handleSidebarToggle} />

        <Box sx={{ p: 4, flexGrow: 1, overflowY: "auto" }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 4,
            }}
          >
            <Typography variant="h4" fontWeight="bold">
              Projects
            </Typography>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => {
                setModalOpen(true);
                setIsEditing(false);
                setNewProjectName("");
              }}
            >
              Create project
            </Button>
          </Box>
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
              gap: 3,
            }}
          >
            {projects.map((project) => (
              <ProjectCard
                project={project}
                projects={projects}
                setProjects={setProjects}
                handleEditStart={handleEditStart}
              />
            ))}
          </Box>
        </Box>
      </Box>
      <ProjectModal
        open={isModalOpen}
        onClose={handleCloseModal}
        projectName={newProjectName}
        setProjectName={setNewProjectName}
        projectDescription={newProjectDescription}
        setProjectDescription={setNewProjectDescription}
        onSubmit={handleSubmit}
        isEditing={isEditing}
        errors={errors}
      />
    </Box>
  );
};

export default ProjectsPage;
