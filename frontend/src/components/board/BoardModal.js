import {
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";

const BoardModal = ({
  open,
  onClose,
  boardName,
  setBoardName,
  boardDescription,
  setBoardDescription,
  selectedProjectId,
  setSelectedProjectId,
  projectsData,
  onSubmit,
  isEditing,
  errors,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{isEditing ? "Edit Board" : "Create New Board"}</DialogTitle>
      <DialogContent sx={{ minWidth: 400 }}>
        <TextField
          autoFocus
          margin="dense"
          label="Board Name"
          type="text"
          fullWidth
          variant="outlined"
          value={boardName}
          onChange={(e) => setBoardName(e.target.value)}
          error={errors.name !== ""}
          helperText={errors.name}
          sx={{ mb: 2 }}
        />

        <TextField
          margin="dense"
          label="Description"
          type="text"
          fullWidth
          multiline
          rows={2}
          variant="outlined"
          value={boardDescription}
          onChange={(e) => setBoardDescription(e.target.value)}
          sx={{ mb: 2 }}
        />
        <FormControl fullWidth margin="dense">
          <InputLabel id="project-select-label">Project</InputLabel>
          <Select
            labelId="project-select-label"
            value={selectedProjectId}
            label="Project"
            error={errors.projectId !== ""}
            onChange={(e) => setSelectedProjectId(e.target.value)}
          >
            {projectsData.map((project) => (
              <MenuItem key={project.id} value={project.id}>
                {project.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Cancel
        </Button>
        <Button onClick={onSubmit} color="primary" variant="contained">
          {isEditing ? "Save changes" : "Create Board"}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default BoardModal;
