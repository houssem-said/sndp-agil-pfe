import { useState } from "react";
import { createProject } from "../services/api";
import { TextField, Button, Stack, Alert } from "@mui/material";

const ProjectForm = () => {
    const [project, setProject] = useState({ name: "", description: "" });
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createProject(project);
            alert("Projet créé !");
            setProject({ name: "", description: "" });
        } catch (err) {
            setError(err.response?.data?.message || "Erreur lors de la création");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <Stack spacing={2}>
                {error && <Alert severity="error">{error}</Alert>}
                <TextField
                    label="Nom du projet"
                    required
                    value={project.name}
                    onChange={(e) => setProject({ ...project, name: e.target.value })}
                />
                <TextField
                    label="Description"
                    multiline
                    rows={4}
                    value={project.description}
                    onChange={(e) => setProject({ ...project, description: e.target.value })}
                />
                <Button type="submit" variant="contained">
                    Créer
                </Button>
            </Stack>
        </form>
    );
};

export default ProjectForm;