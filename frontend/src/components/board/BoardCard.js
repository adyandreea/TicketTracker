import {
  Typography,
  Card,
  CardContent,
  CardActions,
  IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

const BoardCard = ({ board, handleEditStart, handleDelete }) => {
  return (
    <Card
      key={board.id}
      sx={{
        borderRadius: 2,
        boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
        bgcolor: "white",
      }}
    >
      <CardContent>
        <Typography variant="h6" component="div" fontWeight="bold">
          {board.name}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          ID: {board.id}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          Project: {board.project}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Description: {board.description}
        </Typography>
      </CardContent>
      <CardActions sx={{ justifyContent: "flex-end" }}>
        <IconButton
          color="primary"
          aria-label="edit"
          onClick={() => handleEditStart(board)}
        >
          <EditIcon />
        </IconButton>

        <IconButton
          color="error"
          aria-label="delete"
          onClick={() => handleDelete(board.id)}
        >
          <DeleteIcon />
        </IconButton>
      </CardActions>
    </Card>
  );
};

export default BoardCard;
