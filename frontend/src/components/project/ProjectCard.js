import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import {
  deleteProject,
} from "../../api/projectApi";

const ProjectCard = ({ project, projects, setProjects, handleEditStart }) => {
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);

  const handleDelete = async (id) => {
    try {
      await deleteProject(id);
      setProjects(projects.filter((project) => project.id !== id));
    } catch (error) {
      console.error("Delete project error:", error.message || error);
    }
  };

  return (
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
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          ID: {project.id}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
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
          onClick={() => setShowConfirmationDialog(true)}
        >
          <DeleteIcon />
        </IconButton>
      </CardActions>
      {showConfirmationDialog && (
      <Dialog
        open={showConfirmationDialog}
        keepMounted
        onClose={() => setShowConfirmationDialog(false)}
        aria-describedby="alert-dialog-slide-description"
      >
        <DialogTitle>{"Delete confirmation"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-slide-description">
            Are you sure you want to delete the project? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowConfirmationDialog(false)}>Cancel</Button>
          <Button onClick={() => handleDelete(project.id)}>Delete</Button>
        </DialogActions>
      </Dialog>
      )}
    </Card>
  );
};

export default ProjectCard;
