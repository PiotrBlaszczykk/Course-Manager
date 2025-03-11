// src/components/Header/Header.jsx
import React, { useContext } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { AuthContext } from '../../context/AuthContext';
import "./Header.css";

function Header() {
    const { user, logout } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <header className="header">
            <div className="header__left">
                <div className="header__logo">🎓CourseManager</div>
            </div>
            <nav className="header__nav">
                <NavLink
                    to="/"
                    className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                >
                    Strona Główna
                </NavLink>
                <NavLink
                    to="/available-events" // Zmieniono ścieżkę
                    className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                >
                    Dostępne wydarzenia
                </NavLink>

                {user && (
                    <>
                        <NavLink
                            to="/my-events" // Zmieniono ścieżkę
                            className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                        >
                            Moje wydarzenia
                        </NavLink>
                        <NavLink
                            to="/calendar"
                            className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                        >
                            Kalendarz
                        </NavLink>
                        <NavLink
                            to="/profile"
                            className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                        >
                            Profil
                        </NavLink>
                    </>
                )}

                {user && user.isOrganizer && (
                    <NavLink
                        to="/organizer-panel"
                        className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                    >
                        Panel Organizatora
                    </NavLink>
                )}

                {/* Jeśli nie chcesz panelu administratora, usuń poniższy blok */}
                {/* {user && !user.isOrganizer && (
                    <NavLink
                        to="/admin-panel"
                        className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                    >
                        Panel Administratora
                    </NavLink>
                )} */}

                {!user ? (
                    <>
                        <NavLink
                            to="/login"
                            className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                        >
                            Login
                        </NavLink>
                        <NavLink
                            to="/register"
                            className={({ isActive }) => isActive ? "headerButton active" : "headerButton"}
                        >
                            Register
                        </NavLink>
                    </>
                ) : (
                    <button className="headerButton" onClick={handleLogout}>
                        Logout
                    </button>
                )}
            </nav>
        </header>
    );
}

export default Header;
