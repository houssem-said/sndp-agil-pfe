import { useEffect, useState } from "react";
import axios from "axios";

const AdminDashboard = () => {
    const [stats, setStats] = useState({ totalTickets: 0, totalRendezVous: 0 });

    useEffect(() => {
        const fetchStats = async () => {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/admin/stats", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setStats(res.data);
        };

        fetchStats();
    }, []);

    return (
        <div className="fusion-admin-dashboard">
            <h1 className="fusion-admin-title">Tableau de bord Admin</h1>
            <div className="fusion-stats-grid">
                <div className="fusion-stat-card fusion-stat-tickets">
                    <h2 className="fusion-stat-title">Tickets générés</h2>
                    <p className="fusion-stat-value">{stats.totalTickets}</p>
                </div>
                <div className="fusion-stat-card fusion-stat-rendezvous">
                    <h2 className="fusion-stat-title">Rendez-vous enregistrés</h2>
                    <p className="fusion-stat-value">{stats.totalRendezVous}</p>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;