import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { TextField, Button, Container, Typography, Alert } from "@mui/material";

const Login = () => {
    const [credentials, setCredentials] = useState({ username: "", password: "" });
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(credentials);
            navigate("/dashboard");
        } catch (err) {
            setError("Identifiants invalides");
        }
    };

    return (
        <Container maxWidth="xs" sx={{ mt: 8 }}>
            <Typography variant="h4" gutterBottom>Connexion</Typography>
            {error && <Alert severity="error">{error}</Alert>}
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Nom d'utilisateur"
                    fullWidth
                    margin="normal"
                    required
                    value={credentials.username}
                    onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
                />
                <TextField
                    label="Mot de passe"
                    type="password"
                    fullWidth
                    margin="normal"
                    required
                    value={credentials.password}
                    onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
                />
                <Button type="submit" variant="contained" fullWidth sx={{ mt: 3 }}>
                    Se connecter
                </Button>
            </form>
        </Container>
    );
};

export default Login;