import { useState, useEffect } from "react";
import {
  Box,
  Paper,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
} from "@mui/material";
import BoardCard from "../board/BoardCard";
import Button from "@mui/material/Button";
import { getProjects } from "../../api/projectApi";

const ProjectCard = () => {
  const [projects, setProjects] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        setLoading(true);
        const data = await getProjects();
        setProjects(data);

        if (data && data.length > 0) {
          setSelectedProjectId(data[0].id);
        }
      } catch (err) {
        console.error("Error loading projects:", err);
        setError("Could not load projects.");
      } finally {
        setLoading(false);
      }
    };

    fetchProjects();
  }, []);

  const handleChange = (event) => {
    setSelectedProjectId(event.target.value);
  };

  const selectedProject = projects.find((p) => p.id === selectedProjectId);
  return (
    <Paper
      sx={{
        p: 3,
        borderRadius: 4,
        boxShadow: "0 9px 90px rgba(104, 86, 86, 0)",
        mb: 3,
        backgroundColor: "#DCDCDC",
        maxWidth: 1900,
        mx: "auto",
        height: "540px",
      }}
    >
      <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
        {loading && <CircularProgress size={24} sx={{ mr: 2 }} />}
        {error && (
          <Alert severity="error" sx={{ flexGrow: 1 }}>
            {error}
          </Alert>
        )}

        {!loading && !error && (
          <FormControl
            variant="filled"
            sx={{ m: 1, minWidth: 250, bgcolor: "white", borderRadius: 1 }}
          >
            <Select
              labelId="project-select-label"
              id="project-select"
              value={selectedProjectId}
              onChange={handleChange}
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
      <BoardCard />
      <Box sx={{ display: "flex", justifyContent: "center", mt: 0.8 }}>
        <Button variant="outlined" culor="primary">
          Switch boards
        </Button>
      </Box>
    </Paper>
  );
};

export default ProjectCard;
