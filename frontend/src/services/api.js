import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
});

// Injection automatique du token JWT
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Gestion des erreurs globales
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export const login = (credentials) => api.post("/auth/login", credentials);
export const fetchProjects = () => api.get("/projects");
export const createProject = (project) => api.post("/projects", project);
export const fetchUsers = () => api.get("/users");
export const fetchUserProfile = () => api.get('/api/auth/me');

export default api;