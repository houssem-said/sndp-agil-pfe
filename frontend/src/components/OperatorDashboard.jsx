import { useEffect, useState } from 'react';
import api from "../services/api";
import Layout from '../components/Layout';
import ProtectedRoute from '../components/ProtectedRoute';
import Spinner from '../components/Spinner';

export default function OperatorDashboard() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [calling, setCalling] = useState(false);
    const [error, setError] = useState('');

    const fetchTickets = async () => {
        setLoading(true);
        try {
            const res = await api.get('/operator/tickets');
            setTickets(res.data);
            setError('');
        } catch (err) {
            setError('Erreur lors du chargement des tickets');
        } finally {
            setLoading(false);
        }
    };

    const callNextTicket = async () => {
        setCalling(true);
        try {
            await api.post('/operator/tickets/call-next');
            await fetchTickets();
            setError('');
        } catch (err) {
            setError('Erreur lors de l\'appel du ticket suivant');
        } finally {
            setCalling(false);
        }
    };

    useEffect(() => {
        fetchTickets();
        const interval = setInterval(fetchTickets, 10000);
        return () => clearInterval(interval);
    }, []);

    return (
        <ProtectedRoute allowedRoles={['OPERATOR']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="fusion-operator-dashboard">
                        <h2 className="fusion-dashboard-title">File d'attente - Tickets en attente</h2>

                        {error && <p className="fusion-error-message">{error}</p>}

                        <button
                            onClick={callNextTicket}
                            disabled={calling || tickets.length === 0}
                            className="fusion-button fusion-call-btn"
                        >
                            {calling ? (
                                <span className="fusion-calling">
                                    <span className="fusion-spinner"></span>
                                    Appel en cours...
                                </span>
                            ) : (
                                'Appeler le ticket suivant'
                            )}
                        </button>

                        {tickets.length === 0 ? (
                            <p className="fusion-empty-message">Aucun ticket en attente.</p>
                        ) : (
                            <ul className="fusion-tickets-list">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="fusion-ticket-card">
                                        <p><strong>Numéro :</strong> {ticket.number}</p>
                                        <p><strong>Client :</strong> {ticket.clientName}</p>
                                        <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        <p><strong>Statut :</strong> <span className="fusion-status fusion-status-waiting">{ticket.status}</span></p>
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