import { Routes, Route } from "react-router-dom";
import OperatorAppointments from './components/OperatorAppointments';
import OperatorTickets from './components/OperatorTickets';
import Login from './components/Login';
import Register from "./components/Register";
import Home from "./pages/Home";
import ClientAppointmentForm from "./components/ClientAppointmentForm.jsx";
import AdminDashboard from "./components/AdminDashboard.jsx";
import ProtectedRoute from "./utils/ProtectedRoute";
import Navbar from "./components/Navbar";
import OperatorDashboard from "./components/OperatorDashboard.jsx";
import ClientDashboard from "./components/ClientDashboard.jsx";
import Tickets from "./components/Tickets.jsx";
import CurrentTickets from "./components/CurrentTickets.jsx";

function App() {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/operator" element={<OperatorDashboard />} />
                <Route path="/client" element={<ClientDashboard />} />
                <Route path="/client/tickets" element={<Tickets />} />
                <Route path="/operator/current-tickets" element={<CurrentTickets />} />
                <Route
                    path="/operator/appointments"
                    element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <OperatorAppointments />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/operator/tickets"
                    element={
                        <ProtectedRoute allowedRoles={["OPERATEUR"]}>
                            <OperatorTickets />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/client/appointment"
                    element={
                        <ProtectedRoute allowedRoles={["CLIENT"]}>
                            <ClientAppointmentForm />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/dashboard"
                    element={
                        <ProtectedRoute allowedRoles={["ADMIN"]}>
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </>
    );
}

export default App;
