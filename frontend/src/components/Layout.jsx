import { useNavigate } from 'react-router-dom';

export default function Layout({ children }) {
    const navigate = useNavigate();
    const role = localStorage.getItem('role');

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        navigate('/login');
    };

    return (
        <div className="min-h-screen flex flex-col">
            <header className="bg-white shadow-md p-4 flex justify-center items-center">
                <h1 className="text-xl font-bold text-blue-700">File d'attente – SNDP AGIL</h1>
                <div className="flex items-center space-x-4">
                    <span className="text-gray-600 capitalize">{role?.toLowerCase()}</span>
                    <button
                        onClick={handleLogout}
                        className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded"
                    >
                        Déconnexion
                    </button>
                </div>
            </header>
            <main className="flex-grow bg-gray-50 p-6">{children}</main>
        </div>
    );
}
