import { useState, useEffect } from "react";
import "./HomePage.css";
import CourseCard from "../../components/CourseCard/CourseCard.jsx";

function HomePage() {
    const [events, setEvents] = useState([]);
    const [tags, setTags] = useState({});
    const [error, setError] = useState(null);

    useEffect(() => {
        // Pobranie listy wydarzeń i tagów z backendu
        Promise.all([
            fetch("/api/events").then((res) => res.json()),
            fetch("/api/tags").then((res) => res.json()),
        ])
            .then(([eventsData, tagsData]) => {
                // Bierzemy tylko pierwsze 3 wydarzenia
                const limitedEvents = eventsData.slice(0, 3);

                // Mapowanie tagów na obiekt { id: nazwa }
                const tagsMap = tagsData.reduce((acc, tag) => {
                    acc[tag.id] = tag.name;
                    return acc;
                }, {});

                setEvents(limitedEvents);
                setTags(tagsMap);
                setError(null);
            })
            .catch((err) => {
                console.error("Error fetching data:", err);
                setError("Failed to load events or tags. Please try again later.");
            });
    }, []);

    return (
        <div className="home-wrapper">
            <div className="welcome-container">
                <h1 className="welcome-text">Welcome to Course Manager</h1>
            </div>

            <div className="recommended-container">
                <h2 className="recommended-title">Polecane kursy</h2>
                {error && <p className="error-message">{error}</p>}
                <div className="cards-container">
                    {events.length > 0 ? (
                        events.map((event) => (
                            <CourseCard
                                key={event.id}
                                title={event.name}
                                description={event.info}
                                organizer={event.organizerName}
                                classroom={event.classroomName}
                                startDatetime={event.startDatetime}
                                endDatetime={event.endDatetime}
                                tags={event.tagIds.map((id) => tags[id]).filter(Boolean)}
                            />
                        ))
                    ) : (
                        <p>No recommended courses available.</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default HomePage;
