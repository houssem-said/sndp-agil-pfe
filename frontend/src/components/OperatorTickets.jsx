import { useEffect, useState } from "react";
import axios from "axios";

const OperatorTickets = () => {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await axios.get("http://localhost:8080/api/tickets/agency", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setTickets(response.data);
            } catch (error) {
                console.error("Erreur de récupération des tickets", error);
            } finally {
                setLoading(false);
            }
        };

        fetchTickets();
        const interval = setInterval(fetchTickets, 5000); // toutes les 5s

        return () => clearInterval(interval);
    }, []);

    if (loading) return <div>Chargement des tickets...</div>;

    return (
        <div className="p-4">
            <h2 className="text-xl font-bold mb-4">Tickets en cours</h2>
            <ul className="space-y-2">
                {tickets.map((ticket) => (
                    <li key={ticket.id} className="bg-gray-100 p-3 rounded shadow">
                        <strong>Numéro :</strong> {ticket.numero} — <strong>Statut :</strong> {ticket.statut}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default OperatorTickets;
