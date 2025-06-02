// src/services/api.js
import axios from "axios";

// 1) Crée une instance Axios avec baseURL sur /api
const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");

        if (token && !config.url.includes('/auth/login') && !config.url.includes('/auth/register')) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

// 3) Intercepteur pour gérer globalement les erreurs 401 Unauthorized
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Token invalide ou expiré → on le supprime et on redirige vers /login
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

// 4) Fonctions exportées pour chaque endpoint du backend

// Authentification / Inscriptions
export const login = (credentials) => api.post("/auth/login", credentials);
export const register = (userData) => api.post("/auth/register", userData);
export const confirmEmail = (token) => api.post(`/auth/confirm?token=${token}`);

// Profil utilisateur
export const fetchUserProfile = () => api.get("/auth/me");

// ====================
// Endpoints TICKETS
// ====================

// CLIENT : récupérer ses propres tickets
export const fetchMyTickets = () => api.get("/tickets/my");

// CLIENT : créer un nouveau ticket (sans préciser de guichet, géré côté backend)
export const createMyTicket = () => api.post("/tickets");

// OPÉRATEUR : récupérer la file d’attente pour l’agence (tous les tickets en attente)
export const fetchAgencyTickets = () => api.get("/tickets/agency");

// OPÉRATEUR : appeler le prochain ticket
export const callNextTicket = () => api.post("/tickets/call-next");

// OPÉRATEUR : récupérer les tickets “appelés” en cours pour l’agent
export const fetchCurrentTickets = () => api.get("/tickets/current");

// OPÉRATEUR : terminer un ticket par son ID
// Exemple d’utilisation : finishTicket(42)
export const finishTicket = (ticketId) => api.post(`/tickets/${ticketId}/finish`);

// ====================
// Endpoints RENDEZ-VOUS
// ====================

// CLIENT : créer un rendez-vous
// data doit contenir { date, heure, serviceId, motif }
export const createAppointment = (data) => api.post("/rendezvous/create", data);

// CLIENT : récupérer ses propres rendez-vous
export const fetchMyAppointments = () => api.get("/rendezvous/my");

// OPÉRATEUR : récupérer tous les rendez-vous de l’agence
export const fetchAgencyAppointments = () => api.get("/rendezvous/agency");

// ====================
// Endpoints ADMIN
// ====================

// ADMIN : récupérer les statistiques globales (total tickets, total rdv)
export const fetchAdminStats = () => api.get("/admin/stats");

// ====================
// (Éventuels autres endpoints…)
// ====================

// Exemple générique si besoin :
// export const fetchServices = () => api.get("/services");
// export const fetchGuichets = () => api.get("/guichets");

export default api;
