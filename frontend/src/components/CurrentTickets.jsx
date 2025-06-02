import { useEffect, useState } from 'react';
import api from '../services/api';
import Layout from '../components/Layout';
import ProtectedRoute from '../components/ProtectedRoute';
import Spinner from '../components/Spinner';

export default function CurrentTickets() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [processingId, setProcessingId] = useState(null);
    const [error, setError] = useState('');

    const fetchCurrentTickets = async () => {
        setLoading(true);
        try {
            const res = await api.get('/operator/tickets/current');
            setTickets(res.data);
            setError('');
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Erreur lors du chargement des tickets en cours');
        } finally {
            setLoading(false);
        }
    };

    const finishTicket = async (ticketId) => {
        setProcessingId(ticketId);
        try {
            await api.post(`/operator/tickets/${ticketId}/finish`);
            await fetchCurrentTickets();
            setError('');
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Erreur lors de la finalisation du ticket');
        } finally {
            setProcessingId(null);
        }
    };

    useEffect(() => {
        fetchCurrentTickets();
        const interval = setInterval(fetchCurrentTickets, 10000);
        return () => clearInterval(interval);
    }, []);

    return (
        <ProtectedRoute allowedRoles={['OPERATOR']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="fusion-tickets-container">
                        <h2 className="fusion-tickets-title">Tickets en cours</h2>

                        {error && <p className="fusion-error-message">{error}</p>}

                        {tickets.length === 0 ? (
                            <p className="fusion-empty-message">Aucun ticket en cours.</p>
                        ) : (
                            <ul className="fusion-tickets-list">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="fusion-ticket-card">
                                        <div className="fusion-ticket-info">
                                            <p><strong>Numéro :</strong> {ticket.number}</p>
                                            <p><strong>Client :</strong> {ticket.clientName}</p>
                                            <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        </div>
                                        <button
                                            onClick={() => finishTicket(ticket.id)}
                                            disabled={processingId === ticket.id}
                                            className="fusion-button fusion-complete-btn"
                                        >
                                            {processingId === ticket.id ? (
                                                <span className="fusion-processing">Traitement...</span>
                                            ) : (
                                                'Terminer'
                                            )}
                                        </button>
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