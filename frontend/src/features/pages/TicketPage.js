import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box,
  TextField,
  Button,
  Typography,
  Stack,
  Paper,
  Chip,
  Container,
  MenuItem,
  Divider,
} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { getTicketById, updateTicket, deleteTicket } from "../../api/ticketApi";
import { useLanguage } from "../../i18n/LanguageContext";
import LoadingScreen from "../../components/common/LoadingScreen";
import Navbar from "../../components/layout/Navbar";
import Sidebar from "../../components/layout/Sidebar";
import ProfileSidebar from "../../components/layout/ProfileSidebar";
import ConfirmationDialog from "../../components/common/ConfirmationDialog";
import HasRole from "../../features/auth/HasRole";
import ConfirmationNotification from "../../components/common/ConfirmationNotification";
import { getProjectMembers } from "../../api/projectApi";

const TicketPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { translate } = useLanguage();
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isProfileSidebarOpen, setProfileSidebarOpen] = useState(false);
  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [serverMessage, setServerMessage] = useState({
    type: "success",
    text: "",
  });
  const [errors, setErrors] = useState({});
  const [projectUsers, setProjectUsers] = useState([]);

  const handleProfileClick = () => {
    setProfileSidebarOpen(!isProfileSidebarOpen);
  };

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const [loading, setLoading] = useState(true);
  const [ticket, setTicket] = useState({
    title: "",
    description: "",
    status: "",
    boardId: null,
    storyPoints: "",
    assignedUserId: "",
  });

  const validate = () => {
    let tempErrors = {};

    if (!ticket.title || ticket.title.trim().length === 0) {
      tempErrors.title = translate("ticket_title_required");
    } else if (ticket.title.length < 3) {
      tempErrors.title = translate("ticket_title_too_short");
    } else if (ticket.title.length > 50) {
      tempErrors.title = translate("ticket_title_length_invalid");
    }

    if (ticket.description && ticket.description.length > 255) {
      tempErrors.description = translate("ticket_description_too_long");
    }

    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  useEffect(() => {
    getTicketById(id)
      .then((data) => {
        setTicket({
          ...data,
          storyPoints: data.storyPoints || "",
          assignedUserId: data.assignedUserId || "",
        });
        setLoading(false);

        if (data.projectId) {
          getProjectMembers(data.projectId)
            .then((users) => {
              setProjectUsers(users);
            })
            .catch((err) => {
              console.error("Error fetching project members:", err);
            });
        }
      })
      .catch(() => navigate("/dashboard"));
  }, [id, navigate]);

  const handleSave = async () => {
    if (!validate()) return;
    try {
      const payload = {
        ...ticket,
        storyPoints:
          ticket.storyPoints === "" ? null : Number(ticket.storyPoints),
        assignedUserId:
          ticket.assignedUserId === "" ? null : ticket.assignedUserId,
      };

      await updateTicket(id, payload);
      setServerMessage({
        type: "success",
        text: translate("ticket_updated_successfully"),
      });
      setNotificationOpen(true);
    } catch (err) {
      setServerMessage({
        type: "error",
        text: translate("error_updating_ticket"),
      });
      setNotificationOpen(true);
    }
  };

  const handleDelete = async () => {
    try {
      await deleteTicket(id);
      navigate(-1);
    } catch (err) {
      console.error(err);
      setShowConfirmationDialog(false);
      setServerMessage({
        type: "error",
        text: translate("error_deleting_ticket"),
      });
      setNotificationOpen(true);
    }
  };
  const getStatusStyle = (status) => {
    switch (status) {
      case "DONE":
        return { bg: "#e8f5e9", text: "#2e7d32", border: "#c8e6c9" };
      case "TODO":
        return { bg: "#e3f2fd", text: "#1565c0", border: "#bbdefb" };
      case "IN_PROGRESS":
        return { bg: "#fff3e0", text: "#ed6c02", border: "#ffe0b2" };
      default:
        return { bg: "#f3f4f6", text: "#4b5563", border: "#d1d5db" };
    }
  };

  const statusStyle = getStatusStyle(ticket.status);

  if (loading) return <LoadingScreen />;

  const assignedUser = projectUsers.find(
    (user) => user.id === ticket.assignedUserId,
  );
  const assignedUserName = assignedUser
    ? assignedUser.username
    : translate("unassigned_label");
  return (
    <Box
      sx={{
        p: { xs: 2, md: 5 },
        width: "100%",
        mx: "auto",
        minWidth: 0,
        bgcolor: "#f4f5f7",
        minHeight: "100vh",
      }}
    >
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <ProfileSidebar
        open={isProfileSidebarOpen}
        onClose={() => setProfileSidebarOpen(false)}
      />

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { xs: "100%", md: "calc(100% - 240px)" },
          display: "flex",
          flexDirection: "column",
          minWidth: 0,
          px: { xs: 2, md: 6 },
        }}
      >
        <Navbar
          onMenuClick={handleSidebarToggle}
          onProfileClick={handleProfileClick}
        />

        <Box
          sx={{ mt: 4, mb: 3, display: "flex", justifyContent: "flex-start" }}
        >
          <Button
            startIcon={<ArrowBackIcon />}
            onClick={() => navigate(-1)}
            sx={{
              px: 2,
              py: 1,
              borderRadius: "8px",
              fontWeight: 600,
              textTransform: "none",
              color: "text.secondary",
              bgcolor: "transparent",
              "&:hover": {
                bgcolor: "rgba(0,0,0,0.04)",
                color: "text.primary",
              },
            }}
          >
            {translate("back_to_dashboard_button")}
          </Button>
        </Box>

        <Container maxWidth="lg" disableGutters>
          <Paper
            elevation={0}
            sx={{
              p: { xs: 3, md: 6 },
              borderRadius: "20px",
              bgcolor: "#ffffff",
              border: "1px solid #e2e8f0",
              boxShadow: "0 4px 24px rgba(0,0,0,0.04)",
            }}
          >
            <Stack
              direction="row"
              justifyContent="space-between"
              alignItems="center"
              sx={{ mb: 3 }}
            >
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 800,
                  color: "text.secondary",
                  fontFamily: "'JetBrains Mono', monospace",
                  fontSize: "1.1rem",
                }}
              >
                {translate("ticket_id_label")} #{id}
              </Typography>
              <Chip
                label={ticket.status}
                sx={{
                  fontWeight: 700,
                  fontSize: "0.85rem",
                  px: 1,
                  py: 2,
                  borderRadius: "8px",
                  bgcolor: statusStyle.bg,
                  color: statusStyle.text,
                  border: "1px solid",
                  borderColor: statusStyle.border,
                }}
              />
            </Stack>

            <Box sx={{ mb: 4 }}>
              <Typography
                variant="caption"
                sx={{
                  color: "text.secondary",
                  fontWeight: 700,
                  ml: 1,
                  textTransform: "uppercase",
                }}
              >
                {translate("ticket_title_label")}
              </Typography>
              <TextField
                fullWidth
                placeholder={translate("ticket_title_label")}
                value={ticket.title}
                error={!!errors.title}
                helperText={errors.title}
                onChange={(e) => {
                  setTicket({ ...ticket, title: e.target.value });
                  if (errors.title) setErrors({ ...errors, title: null });
                }}
                InputProps={{
                  sx: {
                    fontSize: { xs: "1.8rem", md: "2.4rem" },
                    fontWeight: 800,
                    lineHeight: 1.2,
                    color: "#1a202c",
                    "& fieldset": { border: "none" },
                    "&:hover fieldset": { border: "1px solid #e2e8f0" },
                    "&.Mui-focused fieldset": {
                      border: "2px solid",
                      borderColor: "primary.main",
                    },
                    "& input": { p: 1 },
                  },
                }}
              />
            </Box>

            <Divider sx={{ mb: 4 }} />

            <Stack
              direction={{ xs: "column", sm: "row" }}
              spacing={4}
              sx={{ mb: 5 }}
            >
              <Box flex={1}>
                <Typography
                  variant="caption"
                  color="text.secondary"
                  sx={{
                    fontWeight: 700,
                    textTransform: "uppercase",
                    letterSpacing: "0.5px",
                  }}
                >
                  {translate("assign_button")}
                </Typography>

                <HasRole
                  allowedRoles={["ADMIN", "MANAGER"]}
                  fallback={
                    <Box
                      sx={{
                        mt: 1.5,
                        px: 2,
                        py: 1,
                        bgcolor: "#f8fafc",
                        borderRadius: "10px",
                        border: "1px solid #e2e8f0",
                      }}
                    >
                      <Typography
                        variant="body2"
                        sx={{ fontWeight: 600, color: "#475569" }}
                      >
                        {assignedUserName}
                      </Typography>
                    </Box>
                  }
                >
                  <TextField
                    select
                    fullWidth
                    size="small"
                    value={ticket.assignedUserId || ""}
                    onChange={(e) =>
                      setTicket({ ...ticket, assignedUserId: e.target.value })
                    }
                    sx={{
                      mt: 1,
                      "& .MuiOutlinedInput-root": {
                        borderRadius: "10px",
                        bgcolor: "#f8fafc",
                      },
                    }}
                  >
                    <MenuItem value="">
                      <em>{translate("unassigned_label")}</em>
                    </MenuItem>
                    {projectUsers.map((user) => (
                      <MenuItem key={user.id} value={user.id}>
                        {user.username}
                      </MenuItem>
                    ))}
                  </TextField>
                </HasRole>
              </Box>

              <Box flex={1}>
                <Typography
                  variant="caption"
                  color="text.secondary"
                  sx={{
                    fontWeight: 700,
                    textTransform: "uppercase",
                    letterSpacing: "0.5px",
                  }}
                >
                  {translate("story_points_label")}
                </Typography>
                <HasRole
                  allowedRoles={["ADMIN", "MANAGER"]}
                  fallback={
                    <Box
                      sx={{
                        mt: 1.5,
                        px: 2,
                        py: 1,
                        bgcolor: "#f8fafc",
                        borderRadius: "10px",
                        border: "1px solid #e2e8f0",
                      }}
                    >
                      <Typography
                        variant="body2"
                        sx={{ fontWeight: 600, color: "#475569" }}
                      >
                        {ticket.storyPoints || "-"}
                      </Typography>
                    </Box>
                  }
                >
                  <TextField
                    type="number"
                    fullWidth
                    size="small"
                    InputProps={{ inputProps: { min: 0 } }}
                    value={ticket.storyPoints}
                    onChange={(e) =>
                      setTicket({ ...ticket, storyPoints: e.target.value })
                    }
                    placeholder="e.g. 5, 8, 13..."
                    sx={{
                      mt: 1,
                      "& .MuiOutlinedInput-root": {
                        borderRadius: "10px",
                        bgcolor: "#f8fafc",
                      },
                    }}
                  />
                </HasRole>
              </Box>

              <Box flex={1}>
                <Typography
                  variant="caption"
                  color="text.secondary"
                  sx={{
                    fontWeight: 700,
                    textTransform: "uppercase",
                    letterSpacing: "0.5px",
                  }}
                >
                  {translate("board_location_label")}
                </Typography>
                <Box
                  sx={{
                    mt: 1.5,
                    px: 2,
                    py: 1,
                    bgcolor: "#f8fafc",
                    borderRadius: "10px",
                    border: "1px solid #e2e8f0",
                  }}
                >
                  <Typography
                    variant="body2"
                    sx={{ fontWeight: 600, color: "#475569" }}
                  >
                    {ticket.boardId || "-"}
                  </Typography>
                </Box>
              </Box>
            </Stack>

            <Box sx={{ mb: 5 }}>
              <Typography
                variant="caption"
                color="text.secondary"
                sx={{
                  fontWeight: 700,
                  ml: 1,
                  textTransform: "uppercase",
                  letterSpacing: "0.5px",
                }}
              >
                {translate("ticket_description_label")}
              </Typography>
              <TextField
                fullWidth
                multiline
                minRows={6}
                placeholder={translate("ticket_description_label")}
                value={ticket.description || ""}
                error={!!errors.description}
                helperText={errors.description}
                onChange={(e) =>
                  setTicket({ ...ticket, description: e.target.value })
                }
                sx={{
                  mt: 1,
                  "& .MuiOutlinedInput-root": {
                    bgcolor: "#f8fafc",
                    borderRadius: "12px",
                    fontSize: "1.05rem",
                    lineHeight: 1.6,
                    "& fieldset": { borderColor: "transparent" },
                    "&:hover fieldset": { borderColor: "#cbd5e1" },
                    "&.Mui-focused fieldset": {
                      borderColor: "primary.main",
                      borderWidth: "2px",
                    },
                  },
                }}
              />
            </Box>

            <Stack
              direction="row"
              spacing={2}
              alignItems="center"
              justifyContent="flex-end"
            >
              <HasRole allowedRoles={["ADMIN", "MANAGER"]}>
                <Button
                  color="error"
                  onClick={() => setShowConfirmationDialog(true)}
                  sx={{
                    textTransform: "none",
                    px: 3,
                    py: 1.2,
                    borderRadius: "10px",
                    fontSize: "0.95rem",
                    fontWeight: 600,
                    "&:hover": { bgcolor: "#fef2f2" },
                  }}
                >
                  {translate("delete_button")}
                </Button>
              </HasRole>
              <Button
                variant="contained"
                onClick={handleSave}
                sx={{
                  textTransform: "none",
                  px: 4,
                  py: 1.2,
                  borderRadius: "10px",
                  fontSize: "0.95rem",
                  fontWeight: 600,
                  boxShadow: "none",
                  "&:hover": { boxShadow: "0 4px 12px rgba(0,0,0,0.15)" },
                }}
              >
                {translate("save_changes_button")}
              </Button>
            </Stack>
          </Paper>
        </Container>

        <ConfirmationNotification
          open={notificationOpen}
          onClose={() => setNotificationOpen(false)}
          severity={serverMessage.type}
          message={serverMessage.text}
        />
      </Box>

      {showConfirmationDialog && (
        <ConfirmationDialog
          title={translate("confirm_deletion_title")}
          description={translate("confirm_ticket_deletion_message")}
          open={showConfirmationDialog}
          onClose={() => setShowConfirmationDialog(false)}
          buttonOneText={translate("cancel_button")}
          buttonTwoText={translate("delete_button")}
          buttonOneHandle={() => setShowConfirmationDialog(false)}
          buttonTwoHandle={() => {
            handleDelete(ticket.id);
            setShowConfirmationDialog(false);
          }}
        />
      )}
    </Box>
  );
};

export default TicketPage;
