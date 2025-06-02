import { useState } from "react";
import axios from "axios";

const ClientAppointmentForm = () => {
    const [form, setForm] = useState({ date: "", heure: "" });
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.post("/rendezvous/create", form, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("Rendez-vous enregistré avec succès !");
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setMessage("Erreur lors de l'enregistrement.");
        }
    };

    return (
        <div className="fusion-appointment-form">
            <h2 className="fusion-form-title">Prendre un rendez-vous</h2>
            <form onSubmit={handleSubmit} className="fusion-form">
                <div className="fusion-form-group">
                    <input
                        type="date"
                        name="date"
                        value={form.date}
                        onChange={handleChange}
                        required
                        className="fusion-form-input"
                    />
                </div>
                <div className="fusion-form-group">
                    <input
                        type="time"
                        name="heure"
                        value={form.heure}
                        onChange={handleChange}
                        required
                        className="fusion-form-input"
                    />
                </div>
                <button type="submit" className="fusion-button fusion-submit-btn">
                    Réserver
                </button>
            </form>
            {message && (
                <p className={`fusion-message ${message.includes("succès") ? "fusion-success" : "fusion-error"}`}>
                    {message}
                </p>
            )}
        </div>
    );
};

export default ClientAppointmentForm;