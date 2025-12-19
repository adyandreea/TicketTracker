import { useEffect, useState } from "react";
import {
  Box,
  Paper,
  Typography,
  Button,
  TextField,
  IconButton,
} from "@mui/material";
import TicketCard from "../../components/ticket/TicketCard";
import CheckIcon from "@mui/icons-material/Check";
import AddIcon from "@mui/icons-material/Add";
import {
  getTicketsByBoardId,
  updateTicket,
  createTicket,
  deleteTicket,
} from "../../api/ticketApi";

const BoardCard = ({ selectedBoardId }) => {
  const columns = ["To Do", "In Progress", "Done"];
  const statusMap = {
    "To Do": "TODO",
    "In Progress": "IN_PROGRESS",
    Done: "DONE",
  };

  const [tickets, setTickets] = useState({
    "To Do": [],
    "In Progress": [],
    Done: [],
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const reverseStatusMap = {
      TODO: "To Do",
      IN_PROGRESS: "In Progress",
      DONE: "Done",
    };

    const fetchTickets = async () => {
      if (!selectedBoardId) {
        setTickets({ "To Do": [], "In Progress": [], Done: [] });
        return;
      }
      setLoading(true);
      setError(null);

      try {
        const data = await getTicketsByBoardId(selectedBoardId);
        const groupedTickets = { "To Do": [], "In Progress": [], Done: [] };

        data.forEach((ticket) => {
          const columnName = reverseStatusMap[ticket.status];
          if (columnName) {
            groupedTickets[columnName].push(ticket);
          }
        });

        setTickets(groupedTickets);
      } catch (err) {
        setError("Could not load tickets for this board.");
        console.error("Error loading tickets:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchTickets();
  }, [selectedBoardId]);

  const [editingTicketId, setEditingTicketId] = useState(null);
  const [editingText, setEditingText] = useState("");

  const [isAdding, setIsAdding] = useState({
    "To Do": false,
    "In Progress": false,
    Done: false,
  });

  const [newCardText, setNewCardText] = useState({
    "To Do": "",
    "In Progress": "",
    Done: "",
  });

  const [draggedTicket, setDraggedTicket] = useState(null);
  const [draggedFromCol, setDraggedFromCol] = useState(null);

  const handleAddClick = (col) => {
    setIsAdding({ ...isAdding, [col]: true });
  };

  const handleSaveClick = (col) => {
    if (newCardText[col].trim() !== "") {
      const newTicketData = {
        title: newCardText[col],
        description: "",
        position: tickets[col].length,
        status: statusMap[col],
        boardId: selectedBoardId,
      };

      createTicket(newTicketData)
        .then((createdTicket) => {
          const ticketId = createdTicket.id;
          const completeTicket = {
            ...createdTicket,
            ...newTicketData,
            id: ticketId,
          };
          setTickets((prevTickets) => ({
            ...prevTickets,
            [col]: [...prevTickets[col], completeTicket],
          }));
        })
        .catch((err) => {
          console.error("Failed to create ticket:", err);
          setError("Failed to create ticket.");
        });
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

    const newStatusJava = statusMap[col];
    const destTickets = [...tickets[col]];
    const newPosition = destTickets.length;

    const ticketToUpdate = {
      ...draggedTicket,
      boardId: selectedBoardId,
      status: newStatusJava,
      position: newPosition,
    };

    const sourceTickets = tickets[draggedFromCol].filter(
      (t) => t.id !== draggedTicket.id
    );

    const updatedDestTickets = [...destTickets, ticketToUpdate];

    setTickets((prevTickets) => ({
      ...prevTickets,
      [draggedFromCol]: sourceTickets,
      [col]: updatedDestTickets,
    }));

    updateTicket(draggedTicket.id, ticketToUpdate)
      .then((response) => {
        console.log(
          `Ticket ${draggedTicket.id} updated successfully with status ${newStatusJava}`
        );
      })
      .catch((error) => {
        console.error(
          "Failed to update status in DB. Rolling back local changes.",
          error
        );

        setTickets((prevTickets) => {
          const revertedSourceTickets = [...sourceTickets, draggedTicket];
          const revertedDestTickets = updatedDestTickets.filter(
            (t) => t.id !== draggedTicket.id
          );

          return {
            ...prevTickets,
            [draggedFromCol]: revertedSourceTickets,
            [col]: revertedDestTickets,
          };
        });
        setError("Failed to move ticket. Please try again.");
      });

    setDraggedTicket(null);
    setDraggedFromCol(null);
  };

  const handleEditStart = (ticket) => {
    setEditingTicketId(ticket.id);
    setEditingText(ticket.title);
  };

  const handleEditSave = (col) => {
    const ticketToEdit = tickets[col].find((t) => t.id === editingTicketId);

    if (!ticketToEdit || editingText.trim() === "") {
      setEditingTicketId(null);
      setEditingText("");
      return;
    }

    const updatedData = {
      ...ticketToEdit,
      title: editingText.trim(),
      boardId: selectedBoardId,
    };

    updateTicket(editingTicketId, updatedData)
      .then(() => {
        setTickets((prevTickets) => {
          const newTickets = { ...prevTickets };
          newTickets[col] = newTickets[col].map((t) =>
            t.id === editingTicketId ? { ...t, title: updatedData.title } : t
          );
          return newTickets;
        });
      })
      .catch((error) => {
        console.error("Failed to update ticket title:", error);
        setError("Failed to update ticket title.");
      });

    setEditingTicketId(null);
    setEditingText("");
  };

  const handleDeleteTicket = (ticketId, col) => {
    deleteTicket(ticketId)
      .then(() => {
        setTickets((prevTickets) => {
          const newTickets = { ...prevTickets };
          newTickets[col] = newTickets[col].filter((t) => t.id !== ticketId);
          return newTickets;
        });
      })
      .catch((error) => {
        console.error("Failed to delete ticket:", error);
        setError("Failed to delete ticket.");
      });
  };

  if (loading) {
    return (
      <Box sx={{ p: 3, textAlign: "center" }}>
        <Typography variant="h6">Loading tickets...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3, textAlign: "center", color: "error.main" }}>
        <Typography variant="h6">Error: {error}</Typography>
      </Box>
    );
  }

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
                  onDelete={() => handleDeleteTicket(ticket.id, col)}
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
