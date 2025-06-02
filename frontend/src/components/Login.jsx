    import { useState } from 'react';
    import { useNavigate, Link } from 'react-router-dom';
    import api from '../services/api';
    import { FaSignInAlt, FaEnvelope, FaLock } from 'react-icons/fa';

    export default function Login() {
        const [email, setEmail] = useState('');
        const [password, setPassword] = useState('');
        const [error, setError] = useState('');
        const [isLoading, setIsLoading] = useState(false);
        const navigate = useNavigate();

        const handleLogin = async (e) => {
            e.preventDefault();
            setError('');
            setIsLoading(true);

            try {
                const res = await api.post('/auth/login', { email, password });
                const { token, role } = res.data;

                localStorage.setItem('token', token);
                localStorage.setItem('role', role);

                switch (role) {
                    case 'ADMIN':
                        navigate('/admin/dashboard');
                        break;
                    case 'OPERATOR':
                        navigate('/operator/dashboard');
                        break;
                    case 'CLIENT':
                        navigate('/client/dashboard');
                        break;
                    default:
                        navigate('/');
                }
            } catch (err) {
                setError(err.response?.data?.message || "Identifiants incorrects ou problème de connexion");
            } finally {
                setIsLoading(false);
            }
        };

        return (
            <div className="fusion-auth-page">
                <div className="fusion-page-content">
                    <section className="auth-section">
                        <div className="auth-container">
                            <h1 className="auth-title">Connexion à <span className="highlight">AGIL SNDP</span></h1>

                            {error && (
                                <div className="auth-error">
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleLogin} className="auth-form">
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

                                <div className="form-group">
                                    <label className="form-label">Mot de passe</label>
                                    <div className="input-with-icon">
                                        <FaLock className="input-icon" />
                                        <input
                                            type="password"
                                            className="form-input"
                                            placeholder="••••••••"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
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
                                            <FaSignInAlt className="btn-icon" />
                                            Se connecter
                                        </>
                                    )}
                                </button>

                                <div className="auth-links">
                                    <Link to="/forgot-password" className="auth-link">
                                        Mot de passe oublié ?
                                    </Link>
                                    <span className="auth-separator">|</span>
                                    <Link to="/register" className="auth-link">
                                        Créer un compte
                                    </Link>
                                </div>
                            </form>
                        </div>
                    </section>
                </div>
            </div>
        );
    }