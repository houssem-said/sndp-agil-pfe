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
            // eslint-disable-next-line no-unused-vars
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
            await fetchTickets(); // rafraîchir la liste
            // eslint-disable-next-line no-unused-vars
        } catch (_) {
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
                    <div className="max-w-3xl mx-auto p-4 bg-white rounded shadow">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-2xl font-bold text-gray-800">Mes Tickets</h2>
                            <button
                                onClick={createTicket}
                                disabled={creating}
                                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                            >
                                {creating ? 'Création...' : 'Nouveau Ticket'}
                            </button>
                        </div>

                        {error && <p className="text-red-600 mb-2">{error}</p>}

                        {tickets.length === 0 ? (
                            <p className="text-gray-600">Aucun ticket trouvé.</p>
                        ) : (
                            <ul className="space-y-3">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="p-4 border rounded bg-gray-50">
                                        <p><strong>Numéro :</strong> {ticket.number}</p>
                                        <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        <p>
                                            <strong>Statut :</strong>{' '}
                                            <span
                                                className={`font-semibold ${
                                                    ticket.status === 'EN_ATTENTE'
                                                        ? 'text-yellow-600'
                                                        : ticket.status === 'EN_COURS'
                                                            ? 'text-blue-600'
                                                            : 'text-green-600'
                                                }`}
                                            >
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
