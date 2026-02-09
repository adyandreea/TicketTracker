import { useState, useEffect } from "react";
import Sidebar from "../../components/layout/Sidebar";
import Navbar from "../../components/layout/Navbar";
import {
  Box,
  Container,
  Table,
  Typography,
  TableContainer,
  Paper,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { getAllUsers, deleteUser, updateUser } from "../../api/editUserApi";
import ConfirmationDialog from "../../components/common/ConfirmationDialog";

const EditUserPage = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const [openEdit, setOpenEdit] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);

  const [errors, setErrors] = useState({});

  const handleSidebarToggle = () => setSidebarOpen(!isSidebarOpen);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (err) {
      console.error("Error fetching users:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenEdit = (user) => {
    setSelectedUser({ ...user });
    setOpenEdit(true);
  };

  const handleCloseEdit = () => {
    setOpenEdit(false);
    setSelectedUser(null);
    setErrors({});
  };

  const validate = () => {
    let tempErrors = {};

    if (!selectedUser?.firstname?.trim())
      tempErrors.firstname = "First name is required";
    if (!selectedUser?.lastname?.trim())
      tempErrors.lastname = "Last name is required";
    if (!selectedUser?.username?.trim())
      tempErrors.username = "Username is required";
    if (!selectedUser?.email?.trim()) tempErrors.email = "Email is required";

    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

  const handleSaveEdit = async () => {
    if (!validate()) return;

    try {
      const updated = await updateUser(selectedUser.id, selectedUser);
      setUsers(users.map((u) => (u.id === updated.id ? updated : u)));
      handleCloseEdit();
      setErrors({});
    } catch (err) {
      alert("Failed to update user");
    }
  };

  const handleConfirmDelete = async () => {
    if (!userToDelete) return;
    try {
      await deleteUser(userToDelete.id);
      setUsers(users.filter((user) => user.id !== userToDelete.id));
      setShowConfirmationDialog(false);
    } catch (err) {
      alert("Failed to delete user");
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
      <Navbar onMenuClick={handleSidebarToggle} />

      <Container
        maxWidth="lg"
        sx={{
          mt: { xs: 2, sm: 4 },
          mb: 4,
          px: { xs: 2, sm: 3 },
          flexGrow: 1,
          pt: { xs: "64px", sm: "70px" },
        }}
      >
        <Box sx={{ mb: { xs: 2, sm: 4 } }}>
          <Typography
            variant="h4"
            sx={{
              fontWeight: 800,
              fontSize: { xs: "1.5rem", sm: "2.125rem" },
            }}
          >
            User Administration
          </Typography>
        </Box>

        <TableContainer
          component={Paper}
          sx={{
            borderRadius: "24px",
            boxShadow: "0 12px 50px rgba(0,0,0,0.1)",
            overflowX: "auto",
            width: "100%",
            maxHeight: "70vh",
            transition: "all 0.3s ease",
            "&:hover": { boxShadow: "0 20px 60px rgba(0,0,0,0.15)" },
            "&::-webkit-scrollbar": { height: "6px" },
            "&::-webkit-scrollbar-thumb": {
              backgroundColor: "#ccc",
              borderRadius: "10px",
            },
          }}
        >
          {loading ? (
            <Box sx={{ p: 5, textAlign: "center" }}>
              <CircularProgress />
            </Box>
          ) : (
            <Table
              sx={{ minWidth: 700 }}
              stickyHeader
              aria-label="sticky table"
            >
              <TableHead sx={{ bgcolor: "#f3f4f6" }}>
                <TableRow>
                  <TableCell sx={{ fontWeight: 700, color: "#111827" }}>
                    First Name
                  </TableCell>
                  <TableCell sx={{ fontWeight: 700, color: "#111827" }}>
                    Last Name
                  </TableCell>
                  <TableCell sx={{ fontWeight: 700, color: "#111827" }}>
                    Username
                  </TableCell>
                  <TableCell sx={{ fontWeight: 700, color: "#111827" }}>
                    Email
                  </TableCell>
                  <TableCell sx={{ fontWeight: 700, color: "#111827" }}>
                    Role
                  </TableCell>
                  <TableCell
                    sx={{ fontWeight: 700, color: "#111827" }}
                    align="center"
                  >
                    Edit
                  </TableCell>
                  <TableCell
                    sx={{ fontWeight: 700, color: "#111827" }}
                    align="center"
                  >
                    Delete
                  </TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.id} hover>
                    <TableCell>{user.firstname}</TableCell>
                    <TableCell>{user.lastname}</TableCell>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.role}</TableCell>
                    <TableCell align="center">
                      <IconButton
                        onClick={() => handleOpenEdit(user)}
                        color="primary"
                      >
                        <EditIcon />
                      </IconButton>
                    </TableCell>
                    <TableCell align="center">
                      <IconButton
                        color="error"
                        onClick={() => {
                          setUserToDelete(user);
                          setShowConfirmationDialog(true);
                        }}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </TableContainer>
      </Container>

      <Dialog
        open={openEdit}
        onClose={handleCloseEdit}
        fullWidth
        maxWidth="sm"
        PaperProps={{
          sx: { borderRadius: { xs: "0", sm: "20px" }, m: { xs: 0, sm: 2 } },
        }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Edit User</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          {selectedUser && (
            <Box
              component="form"
              sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}
            >
              <TextField
                label="First Name"
                value={selectedUser.firstname}
                onChange={(e) => {
                  setSelectedUser({
                    ...selectedUser,
                    firstname: e.target.value,
                  });
                  if (errors.firstname) setErrors({ ...errors, firstname: "" });
                }}
                error={!!errors.firstname}
                helperText={errors.firstname}
                fullWidth
              />
              <TextField
                label="Last Name"
                value={selectedUser.lastname}
                onChange={(e) => {
                  setSelectedUser({
                    ...selectedUser,
                    lastname: e.target.value,
                  });
                  if (errors.lastname) setErrors({ ...errors, lastname: "" });
                }}
                error={!!errors.lastname}
                helperText={errors.lastname}
                fullWidth
              />
              <TextField
                label="Username"
                value={selectedUser.username}
                onChange={(e) => {
                  setSelectedUser({
                    ...selectedUser,
                    username: e.target.value,
                  });
                  if (errors.username) setErrors({ ...errors, username: "" });
                }}
                error={!!errors.username}
                helperText={errors.username}
                fullWidth
              />
              <TextField
                label="Email"
                value={selectedUser.email}
                onChange={(e) => {
                  setSelectedUser({ ...selectedUser, email: e.target.value });
                  if (errors.email) setErrors({ ...errors, email: "" });
                }}
                error={!!errors.email}
                helperText={errors.email}
                fullWidth
              />
              <FormControl fullWidth>
                <InputLabel>Role</InputLabel>
                <Select
                  value={selectedUser.role}
                  label="Role"
                  onChange={(e) =>
                    setSelectedUser({ ...selectedUser, role: e.target.value })
                  }
                >
                  <MenuItem value="USER">USER</MenuItem>
                  <MenuItem value="MANAGER">MANAGER</MenuItem>
                  <MenuItem value="ADMIN">ADMIN</MenuItem>
                </Select>
              </FormControl>
            </Box>
          )}
        </DialogContent>
        <DialogActions sx={{ p: 3 }}>
          <Button onClick={handleCloseEdit} color="inherit">
            Cancel
          </Button>
          <Button
            onClick={handleSaveEdit}
            variant="contained"
            sx={{ borderRadius: "10px" }}
          >
            Save Changes
          </Button>
        </DialogActions>
      </Dialog>

      <ConfirmationDialog
        title="Confirm User Deletion"
        description={`Are you sure you want to delete user ${userToDelete?.username}?`}
        open={showConfirmationDialog}
        onClose={() => setShowConfirmationDialog(false)}
        buttonOneText="Cancel"
        buttonTwoText="Delete User"
        buttonOneHandle={() => setShowConfirmationDialog(false)}
        buttonTwoHandle={handleConfirmDelete}
      />
    </Box>
  );
};

export default EditUserPage;
