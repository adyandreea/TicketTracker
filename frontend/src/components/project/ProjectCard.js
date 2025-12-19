import {
  Box,
  Card,
  CardContent,
  CardActions,
  Typography,
  IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

const ProjectCard = ({ projects, handleEditStart, handleDelete }) => {
  return (
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
      ))}
    </Box>
  );
};

export default ProjectCard;
