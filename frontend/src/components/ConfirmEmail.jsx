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
        <div className="fusion-email-confirmation">
            <div className="fusion-confirmation-card">
                {message && (
                    <p className="fusion-confirmation-message fusion-success">
                        {message}
                    </p>
                )}
                {error && (
                    <p className="fusion-confirmation-message fusion-error">
                        {error}
                    </p>
                )}
            </div>
        </div>
    );
}