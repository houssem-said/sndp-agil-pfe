import { Routes, Route } from "react-router-dom";
import OperatorAppointments from './components/OperatorAppointments';
import OperatorTickets from './components/OperatorTickets';
import Login from './components/Login';
import Register from "./components/Register";
import Home from "./pages/Home";
import ClientAppointmentForm from "./components/ClientAppointmentForm.jsx";
import AdminDashboard from "./components/AdminDashboard.jsx";
import ProtectedRoute from "./utils/ProtectedRoute";
import OperatorDashboard from "./components/OperatorDashboard.jsx";
import ClientDashboard from "./components/ClientDashboard.jsx";
import Tickets from "./components/Tickets.jsx";
import CurrentTickets from "./components/CurrentTickets.jsx";
import ConfirmEmail from "./components/ConfirmEmail.jsx";
import Layout from "./components/Layout";

function App() {
    return (
        <div className="app-container">
            <Routes>
                {/* Routes publiques hors layout */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/confirm-email" element={<ConfirmEmail />} />

                {/* Toutes les autres routes avec Layout */}
                <Route element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="admin/dashboard" element={
                        <ProtectedRoute allowedRoles={["ADMIN"]}>
                            <AdminDashboard />
                        </ProtectedRoute>
                    } />
                    <Route path="operator/dashboard" element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <OperatorDashboard />
                        </ProtectedRoute>
                    } />
                    <Route path="operator/appointments" element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <OperatorAppointments />
                        </ProtectedRoute>
                    } />
                    <Route path="operator/tickets" element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <OperatorTickets />
                        </ProtectedRoute>
                    } />
                    <Route path="operator/current-tickets" element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <CurrentTickets />
                        </ProtectedRoute>
                    } />
                    <Route path="client/dashboard" element={
                        <ProtectedRoute allowedRoles={["CLIENT"]}>
                            <ClientDashboard />
                        </ProtectedRoute>
                    } />
                    <Route path="client/tickets" element={
                        <ProtectedRoute allowedRoles={["CLIENT"]}>
                            <Tickets />
                        </ProtectedRoute>
                    } />
                    <Route path="client/appointment" element={
                        <ProtectedRoute allowedRoles={["CLIENT"]}>
                            <ClientAppointmentForm />
                        </ProtectedRoute>
                    } />
                </Route>
            </Routes>
        </div>
    );
}

export default App;