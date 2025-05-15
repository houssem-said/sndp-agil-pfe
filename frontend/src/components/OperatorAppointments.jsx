import { useEffect, useState } from "react";
import axios from "axios";

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
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                setError("Erreur lors du chargement des rendez-vous.");
            } finally {
                setLoading(false);
            }
        };

        fetchAppointments();
    }, []);

    if (loading) return <div className="text-center mt-10">Chargement...</div>;
    if (error) return <div className="text-red-500 mt-10">{error}</div>;

    return (
        <div className="p-4">
            <h2 className="text-xl font-semibold mb-4">Liste des rendez-vous</h2>
            <table className="min-w-full bg-white rounded shadow">
                <thead>
                <tr>
                    <th className="px-4 py-2">Client</th>
                    <th className="px-4 py-2">Date</th>
                    <th className="px-4 py-2">Heure</th>
                    <th className="px-4 py-2">Statut</th>
                </tr>
                </thead>
                <tbody>
                {appointments.map((rdv) => (
                    <tr key={rdv.id}>
                        <td className="border px-4 py-2">{rdv.client.nom}</td>
                        <td className="border px-4 py-2">{rdv.date}</td>
                        <td className="border px-4 py-2">{rdv.heure}</td>
                        <td className="border px-4 py-2">{rdv.statut}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default OperatorAppointments;
