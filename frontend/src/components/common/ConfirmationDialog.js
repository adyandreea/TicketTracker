import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from "@mui/material";

const ConfirmationDialog = ({
  title,
  description,
  open,
  onClose,
  buttonOneText,
  buttonTwoText,
  buttonOneHandle,
  buttonTwoHandle,
}) => {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle
        sx={{ fontWeight: "bold", textAlign: { xs: "center", sm: "left" } }}
      >
        {title}
      </DialogTitle>
      <DialogContent>
        <DialogContentText sx={{ textAlign: { xs: "center", sm: "left" } }}>
          {description}
        </DialogContentText>
      </DialogContent>
      <DialogActions
        sx={{ px: 3, pb: 2, justifyContent: { xs: "center", sm: "flex-end" } }}
      >
        <Button 
          onClick={buttonOneHandle} 
          variant="outlined" 
          color="inherit"
          sx={{ minWidth: 100 }}
        >
          {buttonOneText}
        </Button>
        <Button 
          onClick={buttonTwoHandle} 
          variant="contained" 
          color="error" 
          autoFocus
          sx={{ minWidth: 100 }}
        >
          {buttonTwoText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmationDialog;
