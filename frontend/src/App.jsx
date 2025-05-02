import { useEffect, useState } from 'react';
import { authService } from './api/authService';

function App() {
    const [message, setMessage] = useState('Chargement...');

    useEffect(() => {
        authService.login({
            email: 'test@example.com',  // Remplacez par un email valide de votre base
            password: 'test123'         // Remplacez par un mot de passe valide
        })
            .then(response => {
                setMessage(`Connexion réussie! Token: ${response.data.token.substring(0, 10)}...`);
                console.log('Réponse complète:', response.data);
            })
            .catch(error => {
                const errorMsg = error.response?.data?.message || error.message;
                setMessage(`Échec: ${errorMsg}`);
                console.error('Détails erreur:', error.response?.data);
            });
    }, []);

    return (
        <div style={{ padding: '20px', fontFamily: 'Arial' }}>
            <h1>Test connexion backend</h1>
            <p>{message}</p>
            <p>Vérifiez aussi la console (F12)</p>
        </div>
    );
}

export default App;