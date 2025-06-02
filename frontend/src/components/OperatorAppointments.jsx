import { useEffect, useState } from "react";
import axios from "axios";
import Spinner from "../components/Spinner"; // Supposons que vous avez un composant Spinner

const OperatorAppointments = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchAppointments = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await axios.get("http://localhost:8080/api/rendezvous/agency", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setAppointments(response.data);
            } catch (err) {
                setError("Erreur lors du chargement des rendez-vous.");
            } finally {
                setLoading(false);
            }
        };

        fetchAppointments();
    }, []);

    if (loading) return <Spinner />;
    if (error) return <div className="fusion-error-message">{error}</div>;

    return (
        <div className="fusion-appointments-container">
            <h2 className="fusion-section-title">Liste des rendez-vous</h2>

            <div className="fusion-table-container">
                <table className="fusion-appointments-table">
                    <thead>
                    <tr>
                        <th>Client</th>
                        <th>Date</th>
                        <th>Heure</th>
                        <th>Statut</th>
                    </tr>
                    </thead>
                    <tbody>
                    {appointments.map((rdv) => (
                        <tr key={rdv.id}>
                            <td>{rdv.client.nom}</td>
                            <td>{new Date(rdv.date).toLocaleDateString()}</td>
                            <td>{rdv.heure}</td>
                            <td>
                                    <span className={`fusion-status fusion-status-${rdv.statut.toLowerCase()}`}>
                                        {rdv.statut}
                                    </span>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OperatorAppointments;