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
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Tableau de bord Admin</h1>
            <div className="grid grid-cols-2 gap-4">
                <div className="bg-blue-100 p-4 rounded shadow">
                    <h2 className="text-lg">Tickets générés</h2>
                    <p className="text-xl font-bold">{stats.totalTickets}</p>
                </div>
                <div className="bg-green-100 p-4 rounded shadow">
                    <h2 className="text-lg">Rendez-vous enregistrés</h2>
                    <p className="text-xl font-bold">{stats.totalRendezVous}</p>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
