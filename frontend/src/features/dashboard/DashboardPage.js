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

const DashboardPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const [projects, setProjects] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState("");
  const [loadingProject, setLoadingProject] = useState(true);
  const [errorProject, setErrorProject] = useState(null);

  const [boards, setBoards] = useState(null);
  const [selectedBoardId, setSelectedBoardId] = useState("");
  const [isModalOpen, setModalOpen] = useState(false);
  const [loadingBoard, setLoadingBoard] = useState(false);
  const [errorBoard, setErrorBoard] = useState(null);

  useEffect(() => {
    const fetchBoards = async () => {
      if (!selectedProjectId) {
        setBoards([]);
        setSelectedBoardId("");
        setLoadingBoard(false);
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

  const handleProjectChange = (event) => {
    setSelectedProjectId(event.target.value);
  };

  const handleBoardChange = (event) => {
    setSelectedBoardId(event.target.value);
  };

  return (
    <Box
      sx={{
        display: "flex",
        height: "100vh",
        backgroundColor: "#f0f0f0",
        overflow: "hidden",
      }}
    >
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { xs: "100%", md: "calc(100% - 240px)" },
          display: "flex",
          flexDirection: "column",
          minWidth: 0,
          height: "100vh",
        }}
      >
        <Navbar onMenuClick={handleSidebarToggle} />

        <Box
          sx={{
            p: { xs: 2, md: 3 },
            flexGrow: 1,
            overflow: "hidden",
            display: "flex",
            flexDirection: "column",
          }}
        >
          <Box
            sx={{
              mb: 2,
              flexShrink: 0,
            }}
          >
            {!loadingProject && !errorProject && (
              <FormControl
                variant="filled"
                sx={{
                  minWidth: { xs: "100%", sm: 250 },
                  bgcolor: "white",
                  borderRadius: 1,
                }}
              >
                <Select
                  value={selectedProjectId}
                  onChange={handleProjectChange}
                  sx={{
                    "& .MuiSelect-select": {
                      py: "12px",
                      fontWeight: "bold",
                    },
                  }}
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
            }}
          >
            <DashboardBoard selectedBoardId={selectedBoardId} />
          </Box>

          {!loadingBoard && !errorBoard && (
            <Box
              sx={{
                mt: 4,
                pb: 2,
                flexShrink: 0,
                display: "flex",
                justifyContent: "center",
              }}
            >
              <Button
                variant="outlined"
                fullWidth={{ xs: true, sm: false }}
                color="primary"
                onClick={() => {
                  setModalOpen(true);
                }}
                sx={{
                  height: { sm: "40px" },
                  px: 3,
                  fontWeight: "bold",
                }}
              >
                Switch boards
              </Button>
              <Dialog
                open={isModalOpen}
                onClose={handleCloseModal}
                fullWidth
                maxWidth="xs"
              >
                <DialogTitle>Choose one board</DialogTitle>
                <DialogContent>
                  <FormControl
                    variant="filled"
                    sx={{
                      m: 1,
                      minWidth: 250,
                      bgcolor: "white",
                      borderRadius: 1,
                    }}
                  >
                    <Select
                      value={selectedBoardId}
                      onChange={handleBoardChange}
                      sx={{
                        "& .MuiSelect-select": {
                          paddingTop: "12px",
                          paddingBottom: "12px",
                          fontWeight: "bold",
                        },
                      }}
                    >
                      {boards &&
                        boards.map((board) => (
                          <MenuItem key={board.id} value={board.id}>
                            {board.name}
                          </MenuItem>
                        ))}
                    </Select>
                  </FormControl>
                </DialogContent>
              </Dialog>
            </Box>
          )}
        </Box>
      </Box>
    </Box>
  );
};

export default DashboardPage;
