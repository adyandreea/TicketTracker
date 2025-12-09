import { Paper, Typography, TextField, IconButton } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close"; 

const TicketCard = ({ 
    ticket, 
    isEditing, 
    onEditStart, 
    onEditChange, 
    onEditSave,
    editingText
}) => {
    if (isEditing) {
        return (
            <Paper
                sx={{
                    p: 1,
                    mb: 1,
                    borderRadius: 2,
                    backgroundColor: "#E8E8E8",
                    boxShadow: "0 1px 4px rgba(0,0,0,0.1)",
                    display: 'flex',
                    alignItems: 'center',
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
                <IconButton 
                    size="small" 
                    onClick={onEditSave} 
                    color="primary"
                >
                    <CheckIcon fontSize="small" />
                </IconButton>
                <IconButton 
                    size="small" 
                    onClick={() => onEditChange({ target: { value: ticket.title } })} 
                >
                    <CloseIcon fontSize="small" />
                </IconButton>
            </Paper>
        );
    }
    return (
        <Paper
            sx={{
                p: 1,
                mb: 1,
                borderRadius: 2,
                backgroundColor: "#E8E8E8",
                boxShadow: "0 1px 4px rgba(0,0,0,0.1)",
                cursor: "grab",
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
            }}
        >
            <Typography variant="body2" sx={{ flexGrow: 1 }}>
                {ticket.title}
            </Typography>
            <IconButton 
                size="small" 
                onClick={onEditStart} 
                sx={{ ml: 1 }}
            >
                <EditIcon fontSize="small"/>
            </IconButton>
        </Paper>
    );
};

export default TicketCard;