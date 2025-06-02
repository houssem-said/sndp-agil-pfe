import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const ProtectedRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    try {
        // Décode le JWT
        const decoded = jwtDecode(token);

        // On récupère le claim "roles", qui est un tableau d’exemples ["ROLE_CLIENT"] ou ["ROLE_AGENT"]
        const userRoles = Array.isArray(decoded.roles) ? decoded.roles : [];

        // Normalize : on retire le préfixe "ROLE_" pour comparer avec allowedRoles
        const normalized = userRoles.map(r => r.replace(/^ROLE_/, ""));

        // Si aucun des rôles de l’utilisateur n’est dans allowedRoles, on redirige
        const isAllowed = normalized.some(r => allowedRoles.includes(r));

        if (!isAllowed) {
            localStorage.removeItem("token");
            return <Navigate to="/login" replace />;
        }

        return children;
        // eslint-disable-next-line no-unused-vars
    } catch (err) {
        // Token invalide ou mal formé : on nettoie et on redirige vers login
        localStorage.removeItem("token");
        return <Navigate to="/login" replace />;
    }
};

export default ProtectedRoute;
