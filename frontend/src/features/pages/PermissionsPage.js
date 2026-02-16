import { useEffect, useState } from "react";
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
} from "@mui/material";
import Sidebar from "../../components/layout/Sidebar";
import ProfileSidebar from "../../components/layout/ProfileSidebar";
import Navbar from "../../components/layout/Navbar";
import { useLanguage } from "../../i18n/LanguageContext";
import {
  getProjects,
  assignUserToProject,
  getProjectMembers,
} from "../../api/projectApi";
import { getAllUsers } from "../../api/editUserApi";

const PermissionsPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isProfileSidebarOpen, setProfileSidebarOpen] = useState(false);
  const { translate } = useLanguage();
  const handleSidebarToggle = () => setSidebarOpen(!isSidebarOpen);
  const handleProfileClick = () => setProfileSidebarOpen(!isProfileSidebarOpen);

  const [projectsData, setProjectsData] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState("");
  const [selectedUserId, setSelectedUserId] = useState("");
  const [allUsers, setAllUsers] = useState([]);
  const [projectMembers, setProjectMembers] = useState([]);

  const fetchInitialData = async () => {
    try {
      const projects = await getProjects();
      setProjectsData(projects);
      const allUsersData = await getAllUsers();
      setAllUsers(allUsersData);
    } catch (error) {
      console.error("Initial fetch error:", error);
    }
  };

  useEffect(() => {
    const fetchMembers = async () => {
      if (selectedProjectId) {
        try {
          const members = await getProjectMembers(selectedProjectId);
          setProjectMembers(members);
        } catch (error) {
          console.error("Error fetching members:", error);
        }
      }
    };
    fetchMembers();
  }, [selectedProjectId]);
  useEffect(() => {
    fetchInitialData();
  }, []);

  const handleAssignUser = async () => {
    if (!selectedProjectId || !selectedUserId) return;

    try {
      await assignUserToProject(selectedProjectId, selectedUserId);

      const updatedMembers = await getProjectMembers(selectedProjectId);
      setProjectMembers(updatedMembers);
      setSelectedUserId("");
    } catch (error) {
      console.error("Assign error:", error);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        minHeight: "100vh",
        bgcolor: "#f5f7fa",
      }}
    >
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />
      <ProfileSidebar
        open={isProfileSidebarOpen}
        onClose={() => setProfileSidebarOpen(false)}
      />
      <Navbar
        onMenuClick={handleSidebarToggle}
        onProfileClick={handleProfileClick}
      />

      <Box
        component="main"
        sx={{
          p: { xs: 2, sm: 4 },
          width: "100%",
          boxSizing: "border-box",
          pt: { xs: "64px", sm: "70px" },
          mt: 3,
        }}
      >
        <Typography
          variant="h4"
          sx={{
            fontWeight: 800,
            color: "#1a2027",
            fontSize: { xs: "1.8rem", sm: "2.4rem" },
            letterSpacing: "-0.5px",
          }}
        >
          {translate("permissions_title")}
        </Typography>

        <Box
          sx={{
            display: "flex",
            flexDirection: { xs: "column", sm: "row" },
            gap: 2,
            mb: 4,
            alignItems: { xs: "stretch", sm: "center" },
            mt: 3,
          }}
        >
          <FormControl fullWidth sx={{ minWidth: { sm: 200 } }}>
            <InputLabel>{translate("select_project")}</InputLabel>
            <Select
              value={selectedProjectId}
              label={translate("select_project")}
              onChange={(e) => setSelectedProjectId(e.target.value)}
            >
              {projectsData.map((proj) => (
                <MenuItem key={proj.id} value={proj.id}>
                  {proj.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth sx={{ minWidth: { sm: 200 } }}>
            <InputLabel>{translate("select_user")}</InputLabel>
            <Select
              value={selectedUserId}
              label={translate("select_user")}
              onChange={(e) => setSelectedUserId(e.target.value)}
            >
              {allUsers
                .filter((u) => !projectMembers.some((m) => m.id === u.id))
                .map((user) => (
                  <MenuItem key={user.id} value={user.id}>
                    {user.username}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>

          <Button
            variant="contained"
            size="large"
            onClick={handleAssignUser}
            disabled={!selectedProjectId || !selectedUserId}
            sx={{
              height: "56px",
              px: 4,
              textTransform: "none",
              fontWeight: "bold",
            }}
          >
            {translate("assign_button")}
          </Button>
        </Box>

        <TableContainer
          component={Paper}
          sx={{ boxShadow: 3, borderRadius: 2, overflowX: "auto" }}
        >
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>ID</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>
                  {translate("username_label")}
                </TableCell>
                <TableCell
                  sx={{
                    fontWeight: "bold",
                    display: { xs: "none", md: "table-cell" },
                  }}
                >
                  {translate("email_label")}
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>
                  {translate("user_role_label")}
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {projectMembers.map((member) => (
                <TableRow key={member.id} hover>
                  <TableCell>{member.id}</TableCell>
                  <TableCell>{member.username}</TableCell>
                  <TableCell sx={{ display: { xs: "none", md: "table-cell" } }}>
                    {member.email}
                  </TableCell>
                  <TableCell>{member.role}</TableCell>
                </TableRow>
              ))}
              {projectMembers.length === 0 && selectedProjectId && (
                <TableRow>
                  <TableCell
                    colSpan={4}
                    align="center"
                    sx={{ py: 3, color: "text.secondary" }}
                  >
                    {translate("no_members_found")}
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
};

export default PermissionsPage;
