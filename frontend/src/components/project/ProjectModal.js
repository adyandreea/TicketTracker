import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Button,
} from "@mui/material";

const ProjectModal = ({
  open,
  onClose,
  projectName,
  setProjectName,
  projectDescription,
  setProjectDescription,
  onSubmit,
  isEditing,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>
        {isEditing ? "Edit Project" : "Create New Project"}
      </DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          label="Project Name"
          type="text"
          fullWidth
          variant="outlined"
          value={projectName}
          onChange={(e) => setProjectName(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") onSubmit();
          }}
        />

        <TextField
          margin="dense"
          label="Descriere"
          type="text"
          fullWidth
          multiline
          rows={3}
          variant="outlined"
          value={projectDescription}
          onChange={(e) => setProjectDescription(e.target.value)}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Cancel
        </Button>
        <Button onClick={onSubmit} color="primary" variant="contained">
          Save changes
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ProjectModal;
