import { Outlet } from "react-router-dom";
import { FaArrowUp } from "react-icons/fa";
import { useState, useEffect } from "react";

export default function Layout() {
    const [showScrollTop, setShowScrollTop] = useState(false);

    useEffect(() => {
        const handleScroll = () => setShowScrollTop(window.scrollY > 300);
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    return (
        <div className="avada-layout">
            {/* Navbar moderne */}
            <header className="fusion-header">
                <div className="fusion-header-content">
                    <div className="fusion-logo">
                        <a href="/">AGIL SNDP</a>
                    </div>

                    <nav className="fusion-main-menu">
                        <ul className="fusion-menu">
                            <li><a href="/">Accueil</a></li>
                            <li className="fusion-menu-button">
                                <a href="/login" className="fusion-button button-flat">
                                    Connexion/Inscription
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </header>

            {/* Contenu principal */}
            <main className="fusion-main-content">
                <Outlet />
            </main>

            {/* Footer moderne */}
            <footer className="fusion-footer">
                <div className="fusion-footer-content">
                    <div className="fusion-footer-widgets">
                        <div className="fusion-widget-column">
                            <h4>Nos horaires</h4>
                            <adress>
                                <p>Lundi - Vendredi : 8h00 - 17h00 </p>
                                <p>Samedi : 9h00 - 12h00</p>
                            </adress>
                        </div>

                        <div className="fusion-widget-column">
                            <h4>Nos bureaux</h4>
                            <address>
                                <p>AGIL SNDP</p>
                                <p>33 Rue de l'Industrie</p>
                                <p>Tunis, Tunisie</p>
                                <p>contact@sndp-agil.tn</p>
                                <p>+216 71 234 567</p>
                            </address>
                        </div>

                        <div className="fusion-widget-column">
                            <h4>Réseaux sociaux</h4>
                            <div className="fusion-social-links">
                                <a href="#"><i className="fab fa-linkedin"></i></a>
                                <a href="#"><i className="fab fa-facebook"></i></a>
                                <a href="#"><i className="fab fa-twitter"></i></a>
                            </div>
                            <a href="/contact" className="fusion-button button-flat">
                                Nous contacter
                            </a>
                        </div>
                    </div>

                    <div className="fusion-copyright">
                        © 2025 AGIL SNDP. Tous droits réservés.
                    </div>
                </div>
            </footer>

            {/* Bouton retour en haut */}
            {showScrollTop && (
                <button
                    onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                    className="fusion-scroll-to-top"
                >
                    <FaArrowUp />
                </button>
            )}
        </div>
    );
}