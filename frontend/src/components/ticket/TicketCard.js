import {
  Paper,
  Typography
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const TicketCard = ({ ticket }) => {
  const navigate = useNavigate();

  const handleOpenDetails = () => {
    navigate(`/tickets/${ticket.id}`);
  };

  return (
    <Paper
      onClick={handleOpenDetails}
      sx={{
        p: 2,
        mb: 1.5,
        borderRadius: 2,
        cursor: "pointer",
        transition: "0.2s",
        "&:hover": {
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          transform: "translateY(-2px)",
        },
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        border: "1px solid #bebebe",
      }}
    >
      <Typography variant="body2" sx={{ fontWeight: 500 }}>
        {ticket.title}
      </Typography>
    </Paper>
  );
};

export default TicketCard;
