import { useState, useEffect } from "react";
import {
  Box,
  FormControl,
  Select,
  MenuItem,
  Dialog,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import DashboardBoard from "./DashboardBoard";
import Button from "@mui/material/Button";
import { getProjects } from "../../api/projectApi";
import { getBoardsByProjectId } from "../../api/boardApi";
import ProfileSidebar from "../../components/layout/ProfileSidebar";

const DashboardPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isProfileSidebarOpen, setProfileSidebarOpen] = useState(false);
  const [projects, setProjects] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState("");
  const [loadingProject, setLoadingProject] = useState(true);
  const [errorProject, setErrorProject] = useState(null);

  const [boards, setBoards] = useState(null);
  const [selectedBoardId, setSelectedBoardId] = useState("");
  const [isModalOpen, setModalOpen] = useState(false);
  const [loadingBoard, setLoadingBoard] = useState(false);
  const [errorBoard, setErrorBoard] = useState(null);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const handleProfileClick = () => {
    setProfileSidebarOpen(!isProfileSidebarOpen);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
  };

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        setLoadingProject(true);
        const data = await getProjects();
        setProjects(data);
        if (data && data.length > 0) {
          setSelectedProjectId(data[0].id);
        }
      } catch (err) {
        console.error("Error loading projects:", err);
        setErrorProject("Could not load projects.");
      } finally {
        setLoadingProject(false);
      }
    };
    fetchProjects();
  }, []);

  useEffect(() => {
    const fetchBoards = async () => {
      if (!selectedProjectId) {
        setBoards([]);
        setSelectedBoardId("");
        return;
      }
      try {
        setLoadingBoard(true);
        const data = await getBoardsByProjectId(selectedProjectId);
        setBoards(data);
        if (data && data.length > 0) {
          setSelectedBoardId(data[0].id);
        } else {
          setSelectedBoardId("");
        }
      } catch (err) {
        console.error("Error loading boards:", err);
        setErrorBoard("Could not load boards.");
        setBoards([]);
      } finally {
        setLoadingBoard(false);
      }
    };
    fetchBoards();
  }, [selectedProjectId]);

  const handleProjectChange = (event) => {
    setSelectedProjectId(event.target.value);
  };

  const handleBoardChange = (event) => {
    setSelectedBoardId(event.target.value);
    handleCloseModal();
  };

  return (
    <Box
      sx={{
        display: "flex",
        minHeight: "100vh",
        backgroundColor: "#f5f7fa",
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
          display: "flex",
          flexDirection: "column",
          minWidth: 0,
          height: "100vh",
          overflow: "hidden",
        }}
      >
        <Navbar
          onMenuClick={handleSidebarToggle}
          onProfileClick={handleProfileClick}
        />

        <Box
          sx={{
            p: { xs: 2, md: 3 },
            display: "flex",
            flexDirection: "column",
            flexGrow: 1,
            mt: { xs: "64px", sm: "70px" },
            overflow: "hidden",
          }}
        >
          <Box sx={{ mb: 2, flexShrink: 0 }}>
            {!loadingProject && !errorProject && (
              <FormControl
                variant="outlined"
                size="small"
                sx={{
                  minWidth: { xs: "100%", sm: 250 },
                  bgcolor: "white",
                  borderRadius: 2,
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
                  },
                }}
              >
                <Select
                  value={selectedProjectId}
                  onChange={handleProjectChange}
                  sx={{ fontWeight: "bold" }}
                >
                  {projects.map((project) => (
                    <MenuItem key={project.id} value={project.id}>
                      {project.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}
          </Box>

          <Box
            sx={{
              flexGrow: 1,
              minHeight: 0,
              display: "flex",
              borderRadius: 3,
            }}
          >
            <DashboardBoard selectedBoardId={selectedBoardId} />
          </Box>

          {!loadingBoard && !errorBoard && (
            <Box
              sx={{
                mt: 2,
                py: 0,
                flexShrink: 0,
                display: "flex",
                justifyContent: "center",
              }}
            >
              <Button
                variant="outlined"
                onClick={() => setModalOpen(true)}
                sx={{
                  px: 3,
                  borderRadius: "10px",
                  fontWeight: 600,
                  textTransform: "none",
                  color: "primary.main",
                  borderColor: "primary.main",
                  bgcolor: "white",

                  "&:hover": {
                    bgcolor: "primary.main",
                    color: "white",
                  },
                }}
              >
                Switch Board
              </Button>
            </Box>
          )}
        </Box>
      </Box>

      <Dialog
        open={isModalOpen}
        onClose={handleCloseModal}
        fullWidth
        maxWidth="xs"
        PaperProps={{ sx: { borderRadius: 3 } }}
      >
        <DialogTitle sx={{ fontWeight: "bold" }}>Select a Board</DialogTitle>
        <DialogContent sx={{ pb: 3 }}>
          <FormControl fullWidth sx={{ mt: 1 }}>
            <Select
              value={selectedBoardId}
              onChange={handleBoardChange}
              displayEmpty
              sx={{ borderRadius: 2 }}
            >
              {boards?.map((board) => (
                <MenuItem key={board.id} value={board.id}>
                  {board.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default DashboardPage;
