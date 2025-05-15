import axios from 'axios';

// Crée une instance Axios
const api = axios.create({
    baseURL: '/api',
});

// Ajoute un interceptor pour inclure le token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default api;
