import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  IconButton,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { deleteProject } from "../../api/projectApi";
import ConfirmationDialog from "../common/ConfirmationDialog";

const ProjectCard = ({ project, projects, setProjects, handleEditStart }) => {
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

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
        display: "flex",
        flexDirection: "column",
        height: "100%",
      }}
    >
      <CardContent sx={{ flexGrow: 1, p: { xs: 2, sm: 3 } }}>
        <Typography
          variant="h6"
          component="div"
          fontWeight="bold"
          sx={{ fontSize: { xs: "1.1rem", sm: "1.25rem" } }}
        >
          {project.name}
        </Typography>
        <Typography
          variant="caption"
          color="text.secondary"
          sx={{ display: "block", mt: 1 }}
        >
          ID: {project.id}
        </Typography>
        <Typography
          variant="body2"
          color="text.secondary"
          sx={{
            mt: 1.5,
            display: "-webkit-box",
            WebkitLineClamp: 3,
            WebkitBoxOrient: "vertical",
            overflow: "hidden",
          }}
        >
          Description: {project.description}
        </Typography>
      </CardContent>
      <CardActions
        sx={{
          justifyContent: "flex-end",
          p: 1,
          borderTop: "1px solid #f0f0f0",
        }}
      >
        <IconButton
          color="primary"
          size={isMobile ? "medium" : "small"}
          aria-label="edit"
          onClick={() => handleEditStart(project)}
        >
          <EditIcon />
        </IconButton>
        <IconButton
          color="error"
          size={isMobile ? "medium" : "small"}
          aria-label="delete"
          onClick={() => setShowConfirmationDialog(true)}
        >
          <DeleteIcon />
        </IconButton>
      </CardActions>
      {showConfirmationDialog && (
        <ConfirmationDialog
          title={"Confirm Deletion"}
          description={"Are you sure you want to delete the project?"}
          open={showConfirmationDialog}
          onClose={() => setShowConfirmationDialog(false)}
          buttonOneText={"Cancel"}
          buttonTwoText={"Delete"}
          buttonOneHandle={() => setShowConfirmationDialog(false)}
          buttonTwoHandle={() => {
            handleDelete(project.id);
            setShowConfirmationDialog(false);
          }}
        />
      )}
    </Card>
  );
};

export default ProjectCard;
