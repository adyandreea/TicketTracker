import {
  Card,
  CardContent,
  CardActions,
  Typography,
  IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

const ProjectCard = ({ project, handleEditStart, handleDelete }) => {
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
          onClick={() => handleDelete(project.id)}
        >
          <DeleteIcon />
        </IconButton>
      </CardActions>
    </Card>
  );
};

export default ProjectCard;
