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
    <Box sx={{ display: "flex", height: "100vh", backgroundColor: "#f0f0f0" }}>
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <Navbar onMenuClick={handleSidebarToggle} />
        <Box sx={{ flexGrow: 1, p: 3 }}>
          <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
            {!loadingProject && !errorProject && (
              <FormControl
                variant="filled"
                sx={{ m: 1, minWidth: 250, bgcolor: "white", borderRadius: 1 }}
              >
                <Select
                  value={selectedProjectId}
                  onChange={handleProjectChange}
                  sx={{
                    "& .MuiSelect-select": {
                      paddingTop: "12px",
                      paddingBottom: "12px",
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
          <DashboardBoard selectedBoardId={selectedBoardId} />
          {!loadingBoard && !errorBoard && (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
              <Button
                variant="outlined"
                culor="primary"
                onClick={() => {
                  setModalOpen(true);
                }}
              >
                Switch boards
              </Button>
              <Dialog open={isModalOpen} onClose={handleCloseModal}>
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
