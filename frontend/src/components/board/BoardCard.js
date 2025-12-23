import React, { useState } from "react";
import {
  Typography,
  Card,
  CardContent,
  CardActions,
  IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { deleteBoard } from "../../api/boardApi";
import ConfirmationDialog from "../common/ConfirmationDialog";

const BoardCard = ({ boards, board, setBoards, handleEditStart }) => {
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);

  const handleDelete = async (id) => {
    try {
      await deleteBoard(id);
      setBoards(boards.filter((board) => board.id !== id));
    } catch (error) {
      console.error("Delete board error:", error.message || error);
    }
  };

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
          onClick={() => setShowConfirmationDialog(true)}
        >
          <DeleteIcon />
        </IconButton>
      </CardActions>
      {showConfirmationDialog && (
        <ConfirmationDialog
          title={"Confirm Deletion"}
          description={"Are you sure you want to delete the board?"}
          open={showConfirmationDialog}
          onClose={() => setShowConfirmationDialog(false)}
          buttonOneText={"Cancel"}
          buttonTwoText={"Delete"}
          buttonOneHandle={() => setShowConfirmationDialog(false)}
          buttonTwoHandle={() => {
            handleDelete(board.id);
            setShowConfirmationDialog(false);
          }}
        />
      )}
    </Card>
  );
};

export default BoardCard;
