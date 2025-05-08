import { useEffect, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { fetchProjects, fetchUsers } from "../services/api";
import { Box, Typography, Button, List, ListItem, Chip } from "@mui/material";

const Dashboard = () => {
    const { user, logout } = useAuth();
    const [projects, setProjects] = useState([]);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        fetchProjects().then((data) => setProjects(data));
        fetchUsers().then((data) => setUsers(data));
    }, []);

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h4">Bienvenue, {user?.username}!</Typography>
            <Button onClick={logout} color="error" sx={{ mt: 2 }}>
                Déconnexion
            </Button>

            <Typography variant="h5" sx={{ mt: 4 }}>Projets</Typography>
            <List>
                {projects.map((project) => (
                    <ListItem key={project.id}>
                        <Chip label={project.name} sx={{ mr: 1 }} />
                        <span>Statut: {project.status}</span>
                    </ListItem>
                ))}
            </List>

            <Typography variant="h5" sx={{ mt: 4 }}>Utilisateurs</Typography>
            <List>
                {users.map((user) => (
                    <ListItem key={user.id}>{user.username}</ListItem>
                ))}
            </List>
        </Box>
    );
};

export default Dashboard;