import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './components/Login'
import Dashboard from './components/Dashboard'
import { useAuth } from './contexts/AuthContext'

const PrivateRoute = ({ children }) => {
    const { user } = useAuth()
    return user ? children : <Navigate to="/login" />
}

function App() {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route
                path="/dashboard"
                element={
                    <PrivateRoute>
                        <Dashboard />
                    </PrivateRoute>
                }
            />
            <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
    )
}

export default App