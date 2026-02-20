import React, { useState } from "react";
import {
  Typography,
  Card,
  CardContent,
  CardActions,
  IconButton,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { deleteBoard } from "../../api/boardApi";
import ConfirmationDialog from "../common/ConfirmationDialog";
import { useLanguage } from "../../i18n/LanguageContext";
import HasRole from "../../features/auth/HasRole";

const BoardCard = ({ boards, board, setBoards, handleEditStart }) => {
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
  const { translate } = useLanguage();

  const handleDelete = async (id) => {
    try {
      await deleteBoard(id);
      setBoards(boards.filter((board) => board.id !== id));
    } catch (error) {
      console.error("Delete board error:", error.message || error);
    }
  };

  return (
    <Card
      key={board.id}
      sx={{
        borderRadius: 3,
        boxShadow: "0 8px 24px rgba(0,0,0,0.12)",
        bgcolor: "white",
        display: "flex",
        flexDirection: "column",
        height: "100%",
        transition: "all 0.3s ease",
        "&:hover": {
          transform: "translateY(-4px)",
          boxShadow: "0 12px 36px rgba(0,0,0,0.18)",
        },
      }}
    >
      <CardContent sx={{ flexGrow: 1, p: { xs: 2, sm: 3 } }}>
        <Typography
          variant="h6"
          component="div"
          fontWeight="bold"
          sx={{ fontSize: { xs: "1.1rem", sm: "1.25rem" } }}
        >
          {board.name}
        </Typography>
        <Typography
          variant="caption"
          color="text.secondary"
          sx={{ display: "block", mt: 1 }}
        >
          ID: {board.id}
        </Typography>
        <Typography
          variant="body2"
          color="text.secondary"
          sx={{ display: "block", mt: 1 }}
        >
          {translate("board_project_label")} {board.project}
        </Typography>
        <Typography
          variant="body2"
          color="text.secondary"
          sx={{
            mt: 1.5,
            display: "-webkit-box",
            WebkitLineClamp: 3,
            WebkitBoxOrient: "vertical",
            overflow: "hidden",
          }}
        >
          {translate("board_description_label")}: {board.description}
        </Typography>
      </CardContent>
      <HasRole allowedRoles={["ADMIN", "MANAGER"]}>
        <CardActions
          sx={{
            justifyContent: "flex-end",
            p: 1,
            borderTop: "1px solid #f0f0f0",
          }}
        >
          <IconButton
            color="primary"
            size={isMobile ? "medium" : "small"}
            aria-label="edit"
            onClick={() => handleEditStart(board)}
          >
            <EditIcon />
          </IconButton>

          <IconButton
            color="error"
            size={isMobile ? "medium" : "small"}
            aria-label="delete"
            onClick={() => setShowConfirmationDialog(true)}
          >
            <DeleteIcon />
          </IconButton>
        </CardActions>
      </HasRole>
      {showConfirmationDialog && (
        <ConfirmationDialog
          title={translate("confirm_deletion_title")}
          description={translate("confirm_board_deletion_message")}
          open={showConfirmationDialog}
          onClose={() => setShowConfirmationDialog(false)}
          buttonOneText={translate("cancel_button")}
          buttonTwoText={translate("delete_button")}
          buttonOneHandle={() => setShowConfirmationDialog(false)}
          buttonTwoHandle={() => {
            handleDelete(board.id);
            setShowConfirmationDialog(false);
          }}
        />
      )}
    </Card>
  );
};

export default BoardCard;
