import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import { FaUser, FaEnvelope, FaLock, FaUserPlus } from 'react-icons/fa';

export default function Register() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');

        if (formData.password !== formData.confirmPassword) {
            setError("Les mots de passe ne correspondent pas");
            return;
        }

        setIsLoading(true);

        try {
            await api.post('/api/auth/register', {
                username: formData.username,
                email: formData.email,
                password: formData.password,
                role: 'CLIENT'
            });
            navigate('/login?registered=true');
        } catch (err) {
            setError(err.response?.data?.message || "Erreur lors de l'inscription");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="fusion-auth-page">
            <div className="fusion-page-content">
                <section className="auth-section">
                    <div className="auth-container">
                        <h1 className="auth-title">Créer un compte <span className="highlight">AGIL SNDP</span></h1>

                        {error && (
                            <div className="auth-error">
                                {error}
                            </div>
                        )}

                        <form onSubmit={handleRegister} className="auth-form">
                            <div className="form-group">
                                <label className="form-label">Nom complet</label>
                                <div className="input-with-icon">
                                    <FaUser className="input-icon" />
                                    <input
                                        type="text"
                                        name="username"
                                        className="form-input"
                                        placeholder="Votre nom complet"
                                        value={formData.username}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Adresse e-mail</label>
                                <div className="input-with-icon">
                                    <FaEnvelope className="input-icon" />
                                    <input
                                        type="email"
                                        name="email"
                                        className="form-input"
                                        placeholder="votre@email.com"
                                        value={formData.email}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Mot de passe</label>
                                <div className="input-with-icon">
                                    <FaLock className="input-icon" />
                                    <input
                                        type="password"
                                        name="password"
                                        className="form-input"
                                        placeholder="••••••••"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required
                                        minLength="6"
                                    />
                                </div>
                                <p className="input-hint">Minimum 6 caractères</p>
                            </div>

                            <div className="form-group">
                                <label className="form-label">Confirmer le mot de passe</label>
                                <div className="input-with-icon">
                                    <FaLock className="input-icon" />
                                    <input
                                        type="password"
                                        name="confirmPassword"
                                        className="form-input"
                                        placeholder="••••••••"
                                        value={formData.confirmPassword}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            <button
                                type="submit"
                                disabled={isLoading}
                                className="primary-btn auth-btn"
                            >
                                {isLoading ? (
                                    <span className="auth-spinner">↻</span>
                                ) : (
                                    <>
                                        <FaUserPlus className="btn-icon" />
                                        S'inscrire
                                    </>
                                )}
                            </button>

                            <div className="auth-links">
                                <span>Déjà un compte ?</span>
                                <Link to="/login" className="auth-link">
                                    Se connecter
                                </Link>
                            </div>
                        </form>
                    </div>
                </section>
            </div>
        </div>
    );
}