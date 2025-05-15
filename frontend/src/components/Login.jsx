import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const res = await api.post('/api/auth/login', { username, password });
            const { token, role } = res.data;

            localStorage.setItem('token', token);
            localStorage.setItem('role', role);

            switch (role) {
                case 'ADMIN':
                    navigate('/admin');
                    break;
                case 'OPERATOR':
                    navigate('/operator');
                    break;
                case 'CLIENT':
                    navigate('/client');
                    break;
                default:
                    navigate('/');
            }
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError("Identifiants invalides ou erreur serveur.");
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <form onSubmit={handleLogin} className="bg-white p-8 rounded shadow-md w-96">
                <h2 className="text-2xl font-bold mb-6 text-center text-blue-600">Connexion</h2>
                {error && <p className="text-red-500 mb-4">{error}</p>}
                <div className="mb-4">
                    <label className="block mb-1 font-medium">Nom d'utilisateur</label>
                    <input
                        type="text"
                        className="w-full border rounded px-3 py-2"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-6">
                    <label className="block mb-1 font-medium">Mot de passe</label>
                    <input
                        type="password"
                        className="w-full border rounded px-3 py-2"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
                >
                    Se connecter
                </button>
                <p className="text-center mt-4 text-sm text-gray-600">
                    Pas encore de compte ?{" "}
                    <Link to="/register" className="text-blue-600 hover:underline font-medium">
                        S'inscrire
                    </Link>
                </p>
            </form>
        </div>
    );
}
