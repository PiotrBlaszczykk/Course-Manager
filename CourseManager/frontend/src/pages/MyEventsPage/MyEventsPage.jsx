import React, { useContext, useEffect, useState } from "react";
import "./MyEventsPage.css";
import { AuthContext } from "../../context/AuthContext";

function MyEventsPage() {
    const { user } = useContext(AuthContext); // Pobieramy dane użytkownika z kontekstu
    const [pastEventIds, setPastEventIds] = useState([]);
    const [futureEventIds, setFutureEventIds] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user?.id) return;

        const fetchEvents = async () => {
            try {
                // Pobieranie przeszłych wydarzeń
                const [pastRes, futureRes] = await Promise.all([
                    fetch(`/api/participants/${user.id}/past`),
                    fetch(`/api/participants/${user.id}/future`),
                ]);

                if (!pastRes.ok || !futureRes.ok) {
                    throw new Error("Nie udało się załadować danych wydarzeń.");
                }

                const [pastEvents, futureEvents] = await Promise.all([
                    pastRes.json(),
                    futureRes.json(),
                ]);

                setPastEventIds(pastEvents.map((event) => event.id));
                setFutureEventIds(futureEvents.map((event) => event.id));
                setError(null);
            } catch (err) {
                console.error("Błąd podczas pobierania wydarzeń:", err);
                setError("Nie udało się załadować danych wydarzeń.");
            }
        };

        fetchEvents();
    }, [user]);

    return (
        <div className="MyCourses-container">
            <h1 className="MyCourses-greeting">
                Witaj {user?.firstname} {user?.surname}!
            </h1>

            {/* Sekcja przeszłych wydarzeń */}
            <div className="MyCourses-section">
                <h2>Wydarzenia, w których wziąłeś udział:</h2>
                <div className="MyCourses-list">
                    {error ? (
                        <p className="error-message">{error}</p>
                    ) : pastEventIds.length > 0 ? (
                        pastEventIds.map((id) => (
                            <p key={id} className="event-id">
                                ID wydarzenia: {id}
                            </p>
                        ))
                    ) : (
                        <p>Nie brałeś udziału w żadnych wydarzeniach.</p>
                    )}
                </div>
            </div>

            {/* Sekcja przyszłych wydarzeń */}
            <div className="MyCourses-section">
                <h2>Twoje nadchodzące wydarzenia:</h2>
                <div className="MyCourses-list">
                    {error ? (
                        <p className="error-message">{error}</p>
                    ) : futureEventIds.length > 0 ? (
                        futureEventIds.map((id) => (
                            <p key={id} className="event-id">
                                ID wydarzenia: {id}
                            </p>
                        ))
                    ) : (
                        <p>Nie jesteś zapisany na żadne nadchodzące wydarzenie.</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default MyEventsPage;
