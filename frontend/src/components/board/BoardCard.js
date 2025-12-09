import { useState } from "react";
import {
  Box,
  Paper,
  Typography,
  Button,
  TextField,
  IconButton,
} from "@mui/material";
import TicketCard from "../ticket/TicketCard";
import CheckIcon from "@mui/icons-material/Check";
import AddIcon from "@mui/icons-material/Add";

const BoardCard = () => {
  const columns = ["To Do", "In Progress", "Done"];
  const [nextId, setNextId] = useState(1);

  const [tickets, setTickets] = useState({
    "To Do": [],
    "In Progress": [],
    Done: [],
  });

  const [editingTicketId, setEditingTicketId] = useState(null);
  const [editingText, setEditingText] = useState("");

  const [isAdding, setIsAdding] = useState({
    "To Do": false,
    "In Progress": false,
    "Done": false,
  });

  const [newCardText, setNewCardText] = useState({
    "To Do": "",
    "In Progress": "",
    "Done": "",
  });

  const [draggedTicket, setDraggedTicket] = useState(null);
  const [draggedFromCol, setDraggedFromCol] = useState(null);

  const handleAddClick = (col) => {
    setIsAdding({ ...isAdding, [col]: true });
  };

  const handleSaveClick = (col) => {
    if (newCardText[col].trim() !== "") {
      const newTicket = {
        id: nextId,
        title: newCardText[col],
      };
      setTickets((prevTickets) => ({
        ...prevTickets,
        [col]: [...prevTickets[col], newTicket],
      }));
      setNextId(nextId + 1);
    }
    setNewCardText({ ...newCardText, [col]: "" });
    setIsAdding({ ...isAdding, [col]: false });
  };

  const handleDragStart = (ticket, col) => {
    setDraggedTicket(ticket);
    setDraggedFromCol(col);
  };

  const handleDrop = (col) => {
    if (!draggedTicket) return;

    if (col === draggedFromCol) {
      setDraggedTicket(null);
      setDraggedFromCol(null);
      return;
    }

    const sourceTickets = tickets[draggedFromCol].filter(
      (t) => t.id !== draggedTicket.id
    );

    const destTickets = [...tickets[col], draggedTicket];

    setTickets({
      ...tickets,
      [draggedFromCol]: sourceTickets,
      [col]: destTickets,
    });

    setDraggedTicket(null);
    setDraggedFromCol(null);
  };

  const handleEditStart = (ticket) => {
    setEditingTicketId(ticket.id);
    setEditingText(ticket.title);
  };

  const handleEditSave = (col) => {
    setTickets((prevTickets) => {
      const newTickets = { ...prevTickets };

      newTickets[col] = newTickets[col].map((t) =>
        t.id === editingTicketId ? { ...t, title: editingText } : t
      );

      return newTickets;
    });

    setEditingTicketId(null);
    setEditingText("");
  };

  return (
    <Box sx={{ display: "flex", gap: 2, flexGrow: 1 }}>
      {columns.map((col) => (
        <Paper
          key={col}
          sx={{
            flex: 1,
            p: 2,
            borderRadius: 2,
            backgroundColor: "white",
            height: 400,
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
            boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
          }}
          onDragOver={(e) => e.preventDefault()}
          onDrop={() => handleDrop(col)}
        >
          <Typography variant="body2" sx={{ fontWeight: "bold", mb: 1 }}>
            {col}
          </Typography>

          <Box
            sx={{
              flexGrow: 1,
              display: "flex",
              flexDirection: "column",
              overflowY: "auto",
              pr: 1,
            }}
          >
            {tickets[col].map((ticket) => (
              <Box
                key={ticket.id}
                draggable
                onDragStart={() => handleDragStart(ticket, col)}
              >
                <TicketCard
                  ticket={ticket}
                  isEditing={editingTicketId === ticket.id}
                  onEditStart={() => handleEditStart(ticket)}
                  onEditChange={(e) => setEditingText(e.target.value)}
                  onEditSave={() => handleEditSave(col)}
                  editingText={editingText}
                />
              </Box>
            ))}
          </Box>

          <Box>
            {isAdding[col] ? (
              <Box sx={{ display: "flex", gap: 1, mt: 1 }}>
                <TextField
                  value={newCardText[col]}
                  onChange={(e) =>
                    setNewCardText({ ...newCardText, [col]: e.target.value })
                  }
                  size="small"
                  variant="outlined"
                  fullWidth
                  onKeyDown={(e) => {
                    if (e.key === "Enter") handleSaveClick(col);
                  }}
                />
                <IconButton
                  color="primary"
                  onClick={() => handleSaveClick(col)}
                >
                  <CheckIcon />
                </IconButton>
              </Box>
            ) : (
              <Button
                startIcon={<AddIcon />}
                size="small"
                onClick={() => handleAddClick(col)}
                sx={{
                  mt: 1,
                  textTransform: "none",
                }}
              >
                Add a card
              </Button>
            )}
          </Box>
        </Paper>
      ))}
    </Box>
  );
};

export default BoardCard;
