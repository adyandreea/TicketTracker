import React, { useState } from "react";
import {
  Paper,
  Typography,
  TextField,
  IconButton,
  useTheme,
  useMediaQuery,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import DeleteIcon from "@mui/icons-material/Delete";
import { deleteTicket } from "../../api/ticketApi";
import ConfirmationDialog from "../common/ConfirmationDialog";

const TicketCard = ({
  ticket,
  isEditing,
  onEditStart,
  onEditChange,
  onEditSave,
  editingText,
  setTickets,
  setError,
}) => {
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const handleDeleteTicket = (ticketId) => {
    deleteTicket(ticketId)
      .then(() => {
        setTickets((prevTickets) => {
          const newTickets = { ...prevTickets };
          Object.keys(newTickets).forEach((columnName) => {
            newTickets[columnName] = newTickets[columnName].filter(
              (t) => t.id !== ticketId,
            );
          });
          return newTickets;
        });
      })
      .catch((error) => {
        console.error("Failed to delete ticket:", error);
        setError("Failed to delete ticket.");
      });
  };

  if (isEditing) {
    return (
      <Paper
        sx={{
          p: 1,
          mb: 1,
          borderRadius: 2,
          backgroundColor: "#E8E8E8",
          boxShadow: "0 1px 4px rgba(0,0,0,0.1)",
          display: "flex",
          alignItems: "center",
        }}
      >
        <TextField
          value={editingText}
          onChange={onEditChange}
          size="small"
          variant="outlined"
          fullWidth
          onKeyDown={(e) => {
            if (e.key === "Enter") onEditSave();
          }}
        />
        <IconButton size="small" onClick={onEditSave} color="primary">
          <CheckIcon fontSize="small" />
        </IconButton>
        <IconButton onClick={() => setShowConfirmationDialog(true)}>
          <DeleteIcon fontSize="small" color="error" />
        </IconButton>
        {showConfirmationDialog && (
          <ConfirmationDialog
            title={"Confirm Deletion"}
            description={"Are you sure you want to delete the ticket?"}
            open={showConfirmationDialog}
            onClose={() => setShowConfirmationDialog(false)}
            buttonOneText={"Cancel"}
            buttonTwoText={"Delete"}
            buttonOneHandle={() => setShowConfirmationDialog(false)}
            buttonTwoHandle={() => {
              handleDeleteTicket(ticket.id);
              setShowConfirmationDialog(false);
            }}
          />
        )}
      </Paper>
    );
  }
  return (
    <Paper
      sx={{
        p: { xs: 1.5, md: 1 },
        mb: 1.5,
        borderRadius: 2,
        backgroundColor: "#f9f9f9",
        boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <Typography
        variant="body2"
        sx={{ flexGrow: 1, fontSize: { xs: "0.95rem", md: "0.875rem" } }}
      >
        {ticket.title}
      </Typography>
      <IconButton size={isMobile ? "medium" : "small"} onClick={onEditStart}>
        <EditIcon fontSize="small" />
      </IconButton>
    </Paper>
  );
};

export default TicketCard;
