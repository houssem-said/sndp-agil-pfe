import { createContext, useContext, useState } from "react";
import {fetchUserProfile, login as apiLogin} from "../services/api";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const login = async (credentials) => {
        const { data: token } = await apiLogin(credentials);
        localStorage.setItem("token", token);
        const { data: userData } = await fetchUserProfile();
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);