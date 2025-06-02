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
                    <div className="fusion-client-dashboard">
                        <h1 className="fusion-dashboard-title">Bienvenue sur votre espace client</h1>

                        {error && <p className="fusion-error-message">{error}</p>}

                        {/* Tickets */}
                        <div className="fusion-section">
                            <h2 className="fusion-section-title">Vos Tickets</h2>
                            {tickets.length === 0 ? (
                                <p className="fusion-empty-message">Aucun ticket actuellement.</p>
                            ) : (
                                <ul className="fusion-list">
                                    {tickets.map(ticket => (
                                        <li key={ticket.id} className="fusion-card">
                                            <p><strong>Numéro :</strong> {ticket.number}</p>
                                            <p><strong>Service :</strong> {ticket.serviceName}</p>
                                            <p><strong>Statut :</strong> <span className="fusion-status fusion-status-pending">{ticket.status}</span></p>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        {/* Rendez-vous */}
                        <div className="fusion-section">
                            <h2 className="fusion-section-title">Vos Rendez-vous</h2>
                            {rendezVous.length === 0 ? (
                                <p className="fusion-empty-message">Aucun rendez-vous programmé.</p>
                            ) : (
                                <ul className="fusion-list">
                                    {rendezVous.map(rdv => (
                                        <li key={rdv.id} className="fusion-card">
                                            <p><strong>Date :</strong> {new Date(rdv.date).toLocaleDateString()}</p>
                                            <p><strong>Heure :</strong> {rdv.heure}</p>
                                            <p><strong>Service :</strong> {rdv.serviceName}</p>
                                            <p><strong>Statut :</strong> <span className="fusion-status fusion-status-confirmed">{rdv.status}</span></p>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        {/* Bouton de prise de rendez-vous */}
                        <div className="fusion-action-container">
                            <button
                                onClick={() => navigate('/client/rendezvous')}
                                className="fusion-button fusion-primary-btn"
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