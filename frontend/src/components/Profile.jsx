import { useEffect, useState } from 'react';
import api from '../services/api';
import Layout from '../components/Layout';
import Spinner from '../components/Spinner';
import ProtectedRoute from '../components/ProtectedRoute';

export default function Profile() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get('/user/me')
            .then((res) => setUser(res.data))
            .catch((err) => console.error(err))
            .finally(() => setLoading(false));
    }, []);

    return (
        <ProtectedRoute allowedRoles={['CLIENT', 'OPERATOR', 'ADMIN']}>
            <Layout>
                {loading ? (
                    <Spinner />
                ) : (
                    <div className="max-w-xl mx-auto p-4 bg-white rounded shadow">
                        <h2 className="text-2xl font-bold text-gray-800 mb-4">Profil Utilisateur</h2>
                        <div className="text-gray-700 space-y-2">
                            <p><strong>Nom d'utilisateur :</strong> {user.username}</p>
                            <p><strong>Email :</strong> {user.email}</p>
                            <p><strong>Rôle :</strong> {user.role}</p>
                        </div>
                    </div>
                )}
            </Layout>
        </ProtectedRoute>
    );
}
