// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import MyEventsPage from "./pages/MyEventsPage/MyEventsPage.jsx";
import AvailableEventsPage from "./pages/AvailableEventsPage/AvailableEventsPage.jsx";
import HomePage from "./pages/HomePage/HomePage.jsx";
import ProfilePage from "./pages/ProfilePage/ProfilePage.jsx";
import CalendarPage from "./pages/CalendarPage/CalendarPage.jsx";
import LoginPage from "./pages/LoginPage/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage/RegisterPage.jsx";
import AdminPanelPage from "./pages/AdminPanelPage/AdminPanelPage.jsx";
import OrganizerPanelPage from "./pages/OrganizerPanelPage/OrganizerPanelPage.jsx";
import { AuthProvider, AuthContext } from './context/AuthContext';

function App() {
    return (
        <AuthProvider>
            <Router>
                <div>
                    <Header />
                    <Routes>
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />

                        <Route
                            path="/"
                            element={
                                <PrivateRoute>
                                    <HomePage />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/my-events"
                            element={
                                <PrivateRoute>
                                    <MyEventsPage />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/available-events"
                            element={
                                <PrivateRoute>
                                    <AvailableEventsPage />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/calendar"
                            element={
                                <PrivateRoute>
                                    <CalendarPage />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/profile"
                            element={
                                <PrivateRoute>
                                    <ProfilePage />
                                </PrivateRoute>
                            }
                        />

                        <Route
                            path="/admin-panel"
                            element={
                                    <AdminPanelPage />
                            }
                        />

                        <Route
                            path="/organizer-panel"
                            element={
                                <OrganizerRoute>
                                    <OrganizerPanelPage />
                                </OrganizerRoute>
                            }
                        />
                        <Route path="*" element={<Navigate to="/" />} />
                    </Routes>
                    <Footer />
                </div>
            </Router>
        </AuthProvider>
    );
}

const PrivateRoute = ({ children }) => {
    const { user } = React.useContext(AuthContext);
    return user ? children : <Navigate to="/login" />;
};

const OrganizerRoute = ({ children }) => {
    const { user } = React.useContext(AuthContext);
    return user && user.isOrganizer ? children : <Navigate to="/" />;
};

export default App;
