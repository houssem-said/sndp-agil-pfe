import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import Layout from '../components/Layout';
import ProtectedRoute from '../components/ProtectedRoute';
import Spinner from '../components/Spinner';

export default function ClientDashboard() {
    const [tickets, setTickets] = useState([]);
    const [rendezVous, setRendezVous] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const ticketRes = await api.get('/client/tickets');
                const rdvRes = await api.get('/client/rendezvous');
                setTickets(ticketRes.data);
                setRendezVous(rdvRes.data);
                setError('');
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                setError("Erreur lors du chargement des données.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    return (
        <ProtectedRoute allowedRoles={['CLIENT']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="max-w-5xl mx-auto p-6 bg-white rounded shadow">
                        <h1 className="text-2xl font-bold mb-4 text-green-700">Bienvenue sur votre espace client</h1>

                        {error && <p className="text-red-600 mb-4">{error}</p>}

                        {/* Tickets */}
                        <div className="mb-6">
                            <h2 className="text-xl font-semibold mb-2">Vos Tickets</h2>
                            {tickets.length === 0 ? (
                                <p className="text-gray-600">Aucun ticket actuellement.</p>
                            ) : (
                                <ul className="space-y-3">
                                    {tickets.map(ticket => (
                                        <li key={ticket.id} className="p-4 border rounded bg-gray-50">
                                            <p><strong>Numéro :</strong> {ticket.number}</p>
                                            <p><strong>Service :</strong> {ticket.serviceName}</p>
                                            <p><strong>Statut :</strong> <span className="text-yellow-600 font-semibold">{ticket.status}</span></p>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        {/* Rendez-vous */}
                        <div className="mb-6">
                            <h2 className="text-xl font-semibold mb-2">Vos Rendez-vous</h2>
                            {rendezVous.length === 0 ? (
                                <p className="text-gray-600">Aucun rendez-vous programmé.</p>
                            ) : (
                                <ul className="space-y-3">
                                    {rendezVous.map(rdv => (
                                        <li key={rdv.id} className="p-4 border rounded bg-gray-50">
                                            <p><strong>Date :</strong> {new Date(rdv.date).toLocaleDateString()}</p>
                                            <p><strong>Heure :</strong> {rdv.heure}</p>
                                            <p><strong>Service :</strong> {rdv.serviceName}</p>
                                            <p><strong>Statut :</strong> <span className="text-blue-700 font-semibold">{rdv.status}</span></p>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        {/* Bouton de prise de rendez-vous */}
                        <div className="text-center">
                            <button
                                onClick={() => navigate('/client/rendezvous')}
                                className="px-6 py-3 bg-green-600 text-white rounded hover:bg-green-700"
                            >
                                Prendre un rendez-vous
                            </button>
                        </div>
                    </div>
                )}
            </Layout>
        </ProtectedRoute>
    );
}
