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

    // Récupérer la file d'attente (tickets en attente)
    const fetchTickets = async () => {
        setLoading(true);
        try {
            const res = await api.get('/operator/tickets'); // adapter URL si nécessaire
            setTickets(res.data);
            setError('');
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Erreur lors du chargement des tickets');
        } finally {
            setLoading(false);
        }
    };

    // Appeler le ticket suivant
    const callNextTicket = async () => {
        setCalling(true);
        try {
            await api.post('/operator/tickets/call-next'); // adapter URL selon backend
            await fetchTickets(); // rafraîchir la liste
            setError('');
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError('Erreur lors de l\'appel du ticket suivant');
        } finally {
            setCalling(false);
        }
    };

    useEffect(() => {
        fetchTickets();
        // Optionnel : rafraîchir la liste toutes les 10s
        const interval = setInterval(fetchTickets, 10000);
        return () => clearInterval(interval);
    }, []);

    return (
        <ProtectedRoute allowedRoles={['OPERATOR']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="max-w-4xl mx-auto p-4 bg-white rounded shadow">
                        <h2 className="text-2xl font-bold mb-4 text-gray-800">File d'attente - Tickets en attente</h2>

                        {error && <p className="text-red-600 mb-4">{error}</p>}

                        <button
                            onClick={callNextTicket}
                            disabled={calling || tickets.length === 0}
                            className="mb-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
                        >
                            {calling ? 'Appel en cours...' : 'Appeler le ticket suivant'}
                        </button>

                        {tickets.length === 0 ? (
                            <p className="text-gray-600">Aucun ticket en attente.</p>
                        ) : (
                            <ul className="space-y-3">
                                {tickets.map((ticket) => (
                                    <li key={ticket.id} className="p-4 border rounded bg-gray-50">
                                        <p><strong>Numéro :</strong> {ticket.number}</p>
                                        <p><strong>Client :</strong> {ticket.clientName}</p>
                                        <p><strong>Service :</strong> {ticket.serviceName}</p>
                                        <p><strong>Statut :</strong> <span className="text-yellow-600 font-semibold">{ticket.status}</span></p>
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
