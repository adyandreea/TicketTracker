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
    <Dialog
      open={open}
      keepMounted
      onClose={onClose}
      aria-describedby="alert-dialog-slide-description"
    >
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-slide-description">
          {description}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={buttonOneHandle}>{buttonOneText}</Button>
        <Button onClick={buttonTwoHandle}>{buttonTwoText}</Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmationDialog;
