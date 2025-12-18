import { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Button,
  TextField,
  Card,
  CardContent,
  CardActions,
  IconButton,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import {
  getBoards,
  createBoard,
  updateBoard,
  deleteBoard,
} from "../../api/boardApi";
import { getProjects } from "../../api/projectApi";

const BoardsPage = () => {
  const [boards, setBoards] = useState([]);
  const [projectsData, setProjectsData] = useState([]);

  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);

  const [boardName, setBoardName] = useState("");
  const [boardDescription, setBoardDescription] = useState("");
  const [selectedProjectId, setSelectedProjectId] = useState("");

  const [isEditing, setIsEditing] = useState(false);
  const [editingBoard, setEditingBoard] = useState(null);

  const fetchBoardsAndProjects = async () => {
    try {
      const projects = await getProjects();
      setProjectsData(projects);

      const projectsMap = projects.reduce((map, project) => {
        map[project.id] = project.name;
        return map;
      }, {});

      const boardsData = await getBoards();

      const mappedBoards = boardsData.map((board) => ({
        ...board,
        project: projectsMap[board.projectId],
      }));

      setBoards(mappedBoards);
    } catch (error) {
      console.error("Fetch data error:", error.message || error);
    }
  };

  useEffect(() => {
    fetchBoardsAndProjects();
  }, []);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setIsEditing(false);
    setEditingBoard(null);
    setBoardName("");
    setBoardDescription("");
    setSelectedProjectId("");
  };

  const handleDelete = async (id) => {
    try {
      await deleteBoard(id);
      setBoards(boards.filter((board) => board.id !== id));
    } catch (error) {
      console.error("Delete board error:", error.message || error);
    }
  };

  const handleEditStart = (board) => {
    setEditingBoard(board);
    setBoardName(board.name);
    setBoardDescription(board.description || "");
    setSelectedProjectId(board.projectId);
    setIsEditing(true);
    setModalOpen(true);
  };

  const handleSubmit = async () => {
    if (boardName.trim() === "" || selectedProjectId === "") return;

    const requestData = {
      name: boardName,
      description: boardDescription,
      projectId: selectedProjectId,
    };

    try {
      let resultBoard;

      if (isEditing) {
        resultBoard = await updateBoard(editingBoard.id, requestData);
      } else {
        resultBoard = await createBoard(requestData);
      }

      const projectName =
        projectsData.find((p) => p.id === resultBoard.projectId)?.name;
      const finalBoard = { ...resultBoard, project: projectName };

      if (isEditing) {
        setBoards(boards.map((b) => (b.id === finalBoard.id ? finalBoard : b)));
      } else {
        setBoards([...boards, finalBoard]);
      }

      handleCloseModal();
    } catch (error) {
      console.error("Submit board error:", error.message || error);
    }
  };
  return (
    <Box sx={{ display: "flex", height: "100vh", backgroundColor: "#f0f0f0" }}>
      <Sidebar open={isSidebarOpen} onClose={() => setSidebarOpen(false)} />

      <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <Navbar onMenuClick={handleSidebarToggle} />

        <Box sx={{ p: 4, flexGrow: 1, overflowY: "auto" }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 4,
            }}
          >
            <Typography variant="h4" fontWeight="bold">
              Boards
            </Typography>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => {
                setModalOpen(true);
                setIsEditing(false);
              }}
            >
              Create board
            </Button>
          </Box>
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
              gap: 3,
            }}
          >
            {boards.map((board) => (
              <Card
                key={board.id}
                sx={{
                  borderRadius: 2,
                  boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
                  bgcolor: "white",
                }}
              >
                <CardContent>
                  <Typography variant="h6" component="div" fontWeight="bold">
                    {board.name}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ mt: 1 }}
                  >
                    ID: {board.id}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{ mt: 1 }}
                  >
                    Project: {board.project}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Description: {board.description}
                  </Typography>
                </CardContent>
                <CardActions sx={{ justifyContent: "flex-end" }}>
                  <IconButton
                    color="primary"
                    aria-label="edit"
                    onClick={() => handleEditStart(board)}
                  >
                    <EditIcon />
                  </IconButton>

                  <IconButton
                    color="error"
                    aria-label="delete"
                    onClick={() => handleDelete(board.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </CardActions>
              </Card>
            ))}
          </Box>
        </Box>
      </Box>

      <Dialog open={isModalOpen} onClose={handleCloseModal}>
        <DialogTitle>Edit board</DialogTitle>
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
          <Button onClick={handleCloseModal} color="primary">
            Cancel
          </Button>
          <Button onClick={handleSubmit} color="primary" variant="contained">
            Save changes
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default BoardsPage;
