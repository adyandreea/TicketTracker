import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Button,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import { useLanguage } from "../../i18n/LanguageContext";

const ProjectModal = ({
  open,
  onClose,
  projectName,
  setProjectName,
  projectDescription,
  setProjectDescription,
  onSubmit,
  isEditing,
  errors,
}) => {
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("sm"));
  const { translate } = useLanguage();

  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullScreen={fullScreen}
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle>
        {isEditing
          ? translate("edit_project")
          : translate("create_new_project")}
      </DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          label={translate("project_name_label")}
          type="text"
          fullWidth
          variant="outlined"
          value={projectName}
          onChange={(e) => setProjectName(e.target.value)}
          error={errors.name !== ""}
          helperText={errors.name}
          onKeyDown={(e) => {
            if (e.key === "Enter") onSubmit();
          }}
        />

        <TextField
          margin="dense"
          label={translate("project_description_label")}
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
          {translate("cancel_button")}
        </Button>
        <Button onClick={onSubmit} color="primary" variant="contained">
          {isEditing
            ? translate("save_changes_button")
            : translate("create_project_button")}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ProjectModal;
