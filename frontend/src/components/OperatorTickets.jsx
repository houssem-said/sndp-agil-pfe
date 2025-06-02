import { useEffect, useState } from "react";
import axios from "axios";
import Spinner from "../components/Spinner"; // Supposons que vous avez un composant Spinner

const OperatorTickets = () => {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await axios.get("http://localhost:8080/api/tickets/agency", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setTickets(response.data);
                setError(null);
            } catch (error) {
                console.error("Erreur de récupération des tickets", error);
                setError("Impossible de charger les tickets");
            } finally {
                setLoading(false);
            }
        };

        fetchTickets();
        const interval = setInterval(fetchTickets, 5000);

        return () => clearInterval(interval);
    }, []);

    if (loading) return <Spinner />;

    return (
        <div className="fusion-tickets-container">
            <h2 className="fusion-section-title">Tickets en cours</h2>

            {error && (
                <div className="fusion-error-message">
                    {error}
                </div>
            )}

            {tickets.length === 0 ? (
                <p className="fusion-empty-message">Aucun ticket en cours</p>
            ) : (
                <ul className="fusion-tickets-list">
                    {tickets.map((ticket) => (
                        <li key={ticket.id} className="fusion-ticket-item">
                            <div className="fusion-ticket-content">
                                <span className="fusion-ticket-number">#{ticket.numero}</span>
                                <span className={`fusion-ticket-status fusion-status-${ticket.statut.toLowerCase()}`}>
                                    {ticket.statut}
                                </span>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OperatorTickets;