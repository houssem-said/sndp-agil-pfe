import { useEffect, useState } from 'react';
import api from '../services/api';
import Layout from '../components/Layout';
import ProtectedRoute from '../components/ProtectedRoute';
import Spinner from '../components/Spinner';

export default function Tickets() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [creating, setCreating] = useState(false);
    const [error, setError] = useState('');

    const fetchTickets = async () => {
        setLoading(true);
        try {
            const res = await api.get('/tickets/my');
            setTickets(res.data);
        } catch (err) {
            setError('Erreur lors de la récupération des tickets');
        } finally {
            setLoading(false);
        }
    };

    const createTicket = async () => {
        setCreating(true);
        setError('');
        try {
            await api.post('/tickets');
            await fetchTickets();
        } catch (err) {
            setError('Échec de création du ticket');
        } finally {
            setCreating(false);
        }
    };

    useEffect(() => {
        fetchTickets();
    }, []);

    return (
        <ProtectedRoute allowedRoles={['CLIENT']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="fusion-tickets-page">
                        <div className="fusion-tickets-header">
                            <h2 className="fusion-page-title">Mes Tickets</h2>
                            <button
                                onClick={createTicket}
                                disabled={creating}
                                className="fusion-button fusion-create-btn"
                            >
                                {creating ? (
                                    <span className="fusion-creating">
                                        <span className="fusion-spinner"></span>
                                        Création...
                                    </span>
                                ) : (
                                    'Nouveau Ticket'
                                )}
                            </button>
                        </div>

                        {error && <p className="fusion-error-message">{error}</p>}

                        {tickets.length === 0 ? (
                            <p className="fusion-empty-message">Aucun ticket trouvé.</p>
                        ) : (
                            <ul className="fusion-tickets-list">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="fusion-ticket-card">
                                        <p><strong>Numéro :</strong> {ticket.number}</p>
                                        <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        <p>
                                            <strong>Statut :</strong>{' '}
                                            <span className={`fusion-status fusion-status-${ticket.status.toLowerCase()}`}>
                                                {ticket.status}
                                            </span>
                                        </p>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                )}
            </Layout>
        </ProtectedRoute>
    );
}