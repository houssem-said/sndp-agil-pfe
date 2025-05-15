import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { useEffect, useState } from "react";

const Navbar = () => {
    const navigate = useNavigate();
    const [role, setRole] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setRole(decoded.role);
            } catch (e) {
                console.error("Token invalide");
                localStorage.removeItem("token");
            }
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    return (
        <nav className="bg-blue-600 text-white p-4 flex justify-between items-center">
            <h1 className="font-bold text-xl">SNDP AGIL</h1>
            <div className="space-x-4">
                {role === "ADMIN" && (
                    <button onClick={() => navigate("/admin/dashboard")}>Admin</button>
                )}
                {role === "OPERATEUR" && (
                    <button onClick={() => navigate("/operator/dashboard")}>Opérateur</button>
                )}
                {role === "CLIENT" && (
                    <button onClick={() => navigate("/client/dashboard")}>Rendez-vous</button>
                )}
                {role && (
                    <button onClick={handleLogout} className="bg-red-500 px-3 py-1 rounded">Déconnexion</button>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
