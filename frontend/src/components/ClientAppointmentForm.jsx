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
            await axios.post("http://localhost:8080/api/rendezvous/create", form, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("Rendez-vous enregistré avec succès !");
        } catch (err) {
            setMessage("Erreur lors de l'enregistrement.");
        }
    };

    return (
        <div className="max-w-md mx-auto p-4">
            <h2 className="text-xl font-semibold mb-4">Prendre un rendez-vous</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <input
                    type="date"
                    name="date"
                    value={form.date}
                    onChange={handleChange}
                    required
                    className="w-full p-2 border rounded"
                />
                <input
                    type="time"
                    name="heure"
                    value={form.heure}
                    onChange={handleChange}
                    required
                    className="w-full p-2 border rounded"
                />
                <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">
                    Réserver
                </button>
            </form>
            {message && <p className="mt-4 text-green-600">{message}</p>}
        </div>
    );
};

export default ClientAppointmentForm;
