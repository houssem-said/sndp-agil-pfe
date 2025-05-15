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

    // Récupère les tickets en cours pour l'opérateur
    const fetchCurrentTickets = async () => {
        setLoading(true);
        try {
            const res = await api.get('/operator/tickets/current'); // adapter URL backend
            setTickets(res.data);
            setError('');
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Erreur lors du chargement des tickets en cours');
        } finally {
            setLoading(false);
        }
    };

    // Marquer un ticket comme terminé
    const finishTicket = async (ticketId) => {
        setProcessingId(ticketId);
        try {
            await api.post(`/operator/tickets/${ticketId}/finish`); // adapter URL backend
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
        const interval = setInterval(fetchCurrentTickets, 10000); // rafraîchir toutes les 10s
        return () => clearInterval(interval);
    }, []);

    return (
        <ProtectedRoute allowedRoles={['OPERATOR']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="max-w-4xl mx-auto p-4 bg-white rounded shadow">
                        <h2 className="text-2xl font-bold mb-4 text-gray-800">Tickets en cours</h2>

                        {error && <p className="text-red-600 mb-4">{error}</p>}

                        {tickets.length === 0 ? (
                            <p className="text-gray-600">Aucun ticket en cours.</p>
                        ) : (
                            <ul className="space-y-3">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="p-4 border rounded bg-gray-50 flex justify-between items-center">
                                        <div>
                                            <p><strong>Numéro :</strong> {ticket.number}</p>
                                            <p><strong>Client :</strong> {ticket.clientName}</p>
                                            <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        </div>
                                        <button
                                            onClick={() => finishTicket(ticket.id)}
                                            disabled={processingId === ticket.id}
                                            className="ml-4 px-3 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                                        >
                                            {processingId === ticket.id ? 'Traitement...' : 'Terminer'}
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
