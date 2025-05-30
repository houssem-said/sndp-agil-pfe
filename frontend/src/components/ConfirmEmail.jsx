import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import api from "../services/api";

export default function ConfirmEmail() {
    const [params] = useSearchParams();
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        const confirm = async () => {
            const token = params.get("token");
            if (!token) {
                setError("Token manquant.");
                return;
            }

            try {
                const res = await api.post(`/api/auth/confirm?token=${token}`);
                setMessage(res.data);
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                setError("Le lien est invalide ou expiré.");
            }
        };

        confirm();
    }, [params]);

    return (
        <div className="flex items-center justify-center h-screen">
            <div className="bg-white p-6 rounded shadow text-center">
                {message && <p className="text-green-600">{message}</p>}
                {error && <p className="text-red-500">{error}</p>}
            </div>
        </div>
    );
}
