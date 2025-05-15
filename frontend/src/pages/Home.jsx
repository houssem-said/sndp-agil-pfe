import { useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';

export default function Home() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');

        if (token && role) {
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
                    localStorage.clear();
            }
        }
    }, [navigate]);


    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 px-4">
            <h1 className="text-4xl font-bold mb-6 text-blue-700">Bienvenue sur notre plateforme</h1>
            <p className="mb-8 text-center text-gray-700 max-w-xl">
                Gérez vos rendez-vous et tickets en ligne facilement avec notre application moderne.
            </p>
            <div className="flex gap-4">
                <Link to="/login">
                    <button className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
                        Se connecter
                    </button>
                </Link>
                <Link to="/register">
                    <button className="bg-gray-300 text-gray-800 px-6 py-2 rounded-lg hover:bg-gray-400 transition">
                        S'inscrire
                    </button>
                </Link>
            </div>
        </div>
    );
}
