import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Adaptez à votre URL backend
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getData = () => api.get('/data');
export const postData = (data) => api.post('/data', data);

// Dans backend.js
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});