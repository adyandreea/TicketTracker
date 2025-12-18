import { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Button,
  TextField,
  Card,
  CardContent,
  CardActions,
  IconButton,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import {
  getProjects,
  createProject,
  updateProject,
  deleteProject,
} from "../../api/projectApi";

const ProjectsPage = () => {
  const [projects, setProjects] = useState([]);
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);

  const [newProjectName, setNewProjectName] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingProject, setEditingProject] = useState(null);

  const [newProjectDescription, setNewProjectDescription] = useState("");

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

  const handleDelete = async (id) => {
    try {
      await deleteProject(id);
      setProjects(projects.filter((project) => project.id !== id));
    } catch (error) {
      console.error("Delete project error:", error.message || error);
    }
  };

  const handleSubmit = () => {
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
              <Card
                key={project.id}
                sx={{
                  borderRadius: 2,
                  boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
                  bgcolor: "white",
                }}
              >
                <CardContent>
                  <Typography variant="h6" component="div" fontWeight="bold">
                    {project.name}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ mt: 1 }}
                  >
                    ID: {project.id}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ mt: 1 }}
                  >
                    Description: {project.description}
                  </Typography>
                </CardContent>
                <CardActions sx={{ justifyContent: "flex-end" }}>
                  <IconButton
                    color="primary"
                    aria-label="edit"
                    onClick={() => handleEditStart(project)}
                  >
                    <EditIcon />
                  </IconButton>

                  <IconButton
                    color="error"
                    aria-label="delete"
                    onClick={() => handleDelete(project.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </CardActions>
              </Card>
            ))}
          </Box>
        </Box>
      </Box>

      <Dialog open={isModalOpen} onClose={handleCloseModal}>
        <DialogTitle>Edit project</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Project Name"
            type="text"
            fullWidth
            variant="outlined"
            value={newProjectName}
            onChange={(e) => setNewProjectName(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleSubmit();
            }}
          />

          <TextField
            margin="dense"
            label="Descriere"
            type="text"
            fullWidth
            multiline
            rows={3}
            variant="outlined"
            value={newProjectDescription}
            onChange={(e) => setNewProjectDescription(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseModal} color="primary">
            Cancel
          </Button>
          <Button onClick={handleSubmit} color="primary" variant="contained">
            Save changes
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProjectsPage;
