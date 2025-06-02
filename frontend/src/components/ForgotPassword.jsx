import { useState } from 'react';
import api from '../services/api';
import { FaEnvelope } from 'react-icons/fa';

export default function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError('');
        setIsLoading(true);

        try {
            const res = await api.post('/auth/forgot-password', { email });
            setMessage(res.data.message || 'Un lien de réinitialisation a été envoyé.');
        } catch (err) {
            setError(err.response?.data?.message || 'Erreur lors de l’envoi.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="fusion-auth-page">
            <div className="fusion-page-content">
                <section className="auth-section">
                    <div className="auth-container">
                        <h1 className="auth-title">Réinitialisation du mot de passe</h1>

                        {message && <div className="auth-success">{message}</div>}
                        {error && <div className="auth-error">{error}</div>}

                        <form onSubmit={handleSubmit} className="auth-form">
                            <div className="form-group">
                                <label className="form-label">Adresse e-mail</label>
                                <div className="input-with-icon">
                                    <FaEnvelope className="input-icon" />
                                    <input
                                        type="email"
                                        className="form-input"
                                        placeholder="votre@email.com"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>
                            </div>

                            <button
                                type="submit"
                                className="primary-btn auth-btn"
                                disabled={isLoading}
                            >
                                {isLoading ? 'Envoi...' : 'Envoyer le lien'}
                            </button>
                        </form>
                    </div>
                </section>
            </div>
        </div>
    );
}
