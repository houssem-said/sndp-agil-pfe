import { useNavigate } from "react-router-dom";
import { FaCalendarAlt, FaClock, FaTicketAlt, FaMobileAlt, FaChartLine } from "react-icons/fa";

export default function Home() {
    const navigate = useNavigate();

    return (
        <div className="fusion-home-page">
            <div className="fusion-page-content">
                {/* Hero Section */}
                <section className="hero-section">
                    <div className="hero-content">
                        <h1>AGIL SNDP <span className="highlight">Service Digital</span></h1>
                        <p className="tagline">
                            Simplifiez vos démarches avec notre solution de prise de rendez-vous
                            et de tickets
                        </p>
                    </div>
                    <div className="hero-image">
                        {/* Remplacez par une image ou illustration appropriée */}
                        <div className="placeholder-image">📱⏱️</div>
                    </div>
                </section>

                {/* Avantages */}
                <section className="benefits-section">
                    <h2>Pourquoi utiliser notre service ?</h2>
                    <div className="benefits-grid">
                        <div className="benefit-card">
                            <FaClock className="benefit-icon" />
                            <h3>Gagnez du temps</h3>
                            <p>Évitez les files d'attente interminables grâce à la prise de rendez-vous en ligne</p>
                        </div>
                        <div className="benefit-card">
                            <FaMobileAlt className="benefit-icon" />
                            <h3>Accès 24/7</h3>
                            <p>Prenez rendez-vous quand vous voulez depuis votre smartphone ou ordinateur</p>
                        </div>
                        <div className="benefit-card">
                            <FaChartLine className="benefit-icon" />
                            <h3>Temps d'attente réduits</h3>
                            <p>Notre système intelligent optimise le flux des clients dans nos agences</p>
                        </div>
                    </div>
                </section>

                {/* Comment ça marche */}
                <section className="how-it-works">
                    <h2>Comment utiliser notre service ?</h2>
                    <div className="steps">
                        <div className="step">
                            <div className="step-number">1</div>
                            <div className="step-content">
                                <h3>Prenez rendez-vous en ligne</h3>
                                <p>Choisissez le service dont vous avez besoin et sélectionnez un créneau disponible</p>
                            </div>
                        </div>
                        <div className="step">
                            <div className="step-number">2</div>
                            <div className="step-content">
                                <h3>Recevez votre e-ticket</h3>
                                <p>Vous recevrez votre e-ticket utilisable en agence</p>
                            </div>
                        </div>
                        <div className="step">
                            <div className="step-number">3</div>
                            <div className="step-content">
                                <h3>Présentez-vous à l'agence</h3>
                                <p>Arrivez 10 minutes avant votre rendez-vous et votre ticket</p>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Témoignages */}
                <section className="testimonials">
                    <h2>Ce que disent nos clients</h2>
                    <div className="testimonial-cards">
                        <div className="testimonial">
                            <p>"J'ai pu prendre un rendez-vous sans avoir à me déplacer en agence. Génial !"</p>
                            <div className="client">- Mohamed T. , Tunis</div>
                        </div>
                        <div className="testimonial">
                            <p>"Le système de rendez-vous m'a évité de perdre une matinée. Je recommande !"</p>
                            <div className="client">- Salma K. , Ariana</div>
                        </div>
                    </div>
                </section>

                {/* Call to Action final */}
                <section className="final-cta">
                    <h2>Prêt à simplifier vos démarches ?</h2>
                    <button
                        onClick={() => navigate("/register")}
                        className="primary-btn large"
                    >
                        Créer un compte gratuit
                    </button>
                    <p className="login-prompt">
                        Déjà inscrit ? <a href="/login">Connectez-vous</a> pour gérer vos rendez-vous
                    </p>
                </section>
            </div>
        </div>
    );
}