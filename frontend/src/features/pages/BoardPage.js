import { useState, useEffect } from "react";
import { Box, Typography, Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import {
  getBoards,
  createBoard,
  updateBoard,
  deleteBoard,
} from "../../api/boardApi";
import { getProjects } from "../../api/projectApi";
import BoardModal from "../../components/board/BoardModal";
import BoardCard from "../../components/board/BoardCard";

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

      const projectName = projectsData.find(
        (p) => p.id === resultBoard.projectId
      )?.name;
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
          <BoardCard
            boards={boards}
            handleEditStart={handleEditStart}
            handleDelete={handleDelete}
          />
        </Box>
      </Box>
      <BoardModal
        open={isModalOpen}
        onClose={handleCloseModal}
        boardName={boardName}
        setBoardName={setBoardName}
        boardDescription={boardDescription}
        setBoardDescription={setBoardDescription}
        selectedProjectId={selectedProjectId}
        setSelectedProjectId={setSelectedProjectId}
        projectsData={projectsData}
        onSubmit={handleSubmit}
      />
    </Box>
  );
};

export default BoardsPage;
