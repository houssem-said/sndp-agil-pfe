import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // Notez le /api/v1 présent dans vos controllers
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Intercepteur pour les erreurs
api.interceptors.response.use(
    response => response,
    error => {
        console.error('Erreur API:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);

export default api;