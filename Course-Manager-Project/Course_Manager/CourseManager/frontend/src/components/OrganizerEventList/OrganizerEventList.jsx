import React, { useState, useEffect } from "react";
import axios from "axios";
import "./OrganizerEventList.css";

function OrganizerEventList({ visible, organizerId }) {
    const [filtersVisible, setFiltersVisible] = useState(false);
    const [addEventVisible, setAddEventVisible] = useState(false);
    const [editingEventId, setEditingEventId] = useState(null);
    const [events, setEvents] = useState([]);
    const [error, setError] = useState(null);

    const [newEvent, setNewEvent] = useState({
        name: "",
        startDate: "",
        startTime: "",
        endDate: "",
        endTime: "",
        maxParticipants: "",
        minAge: "",
        info: "",
        classroom: "",
        tags: "",
    });

    useEffect(() => {
        if (!visible || !organizerId) return;

        axios
            .get(`/api/events/organizers/${organizerId}/events`)
            .then((res) => {
                setEvents(res.data);
                setError(null);
            })
            .catch((err) => {
                console.error("Błąd podczas pobierania eventów organizatora:", err);
                setError("Nie udało się pobrać eventów organizatora.");
            });
    }, [visible, organizerId]);

    const toggleFilters = () => setFiltersVisible(!filtersVisible);

    const toggleAddEventForm = () => {
        setAddEventVisible(!addEventVisible);
        setEditingEventId(null);
        resetEventForm();
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewEvent((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const prepareEventForEdit = (event) => {
        const [startDatePart, startTimeFull] = event.startDatetime.split("T");
        const [endDatePart, endTimeFull] = event.endDatetime.split("T");

        const startTimeShort = startTimeFull ? startTimeFull.slice(0, 5) : "";
        const endTimeShort = endTimeFull ? endTimeFull.slice(0, 5) : "";

        setEditingEventId(event.id);
        setNewEvent({
            name: event.name,
            startDate: startDatePart,
            startTime: startTimeShort,
            endDate: endDatePart,
            endTime: endTimeShort,
            maxParticipants: event.maxParticipants,
            minAge: event.minAge,
            info: event.info,
            classroom: event.classroomId,
            tags: event.tagIds.join(", "),
        });
        setAddEventVisible(true);
    };

    const resetEventForm = () => {
        setNewEvent({
            name: "",
            startDate: "",
            startTime: "",
            endDate: "",
            endTime: "",
            maxParticipants: "",
            minAge: "",
            info: "",
            classroom: "",
            tags: "",
        });
    };

    const handleEventSubmit = (e) => {
        e.preventDefault();

        const tagIds = newEvent.tags
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t.length > 0)
            .map((t) => parseInt(t, 10))
            .filter((id) => !isNaN(id));

        const startDatetime =
            newEvent.startDate && newEvent.startTime
                ? `${newEvent.startDate}T${newEvent.startTime}:00`
                : null;
        const endDatetime =
            newEvent.endDate && newEvent.endTime
                ? `${newEvent.endDate}T${newEvent.endTime}:00`
                : null;

        const requestBody = {
            name: newEvent.name,
            startDatetime,
            endDatetime,
            maxParticipants: parseInt(newEvent.maxParticipants, 10) || 1,
            minAge: parseInt(newEvent.minAge, 10) || 0,
            info: newEvent.info || "",
            organizerId: organizerId, // Ustawiamy automatycznie na ID organizatora
            classroomId: parseInt(newEvent.classroom, 10),
            tagIds,
        };

        const request =
            editingEventId !== null
                ? axios.put(`/api/events/${editingEventId}/update`, requestBody)
                : axios.post("/api/events/create", requestBody);

        request
            .then(() => {
                alert(editingEventId ? "Zaktualizowano wydarzenie!" : "Utworzono wydarzenie!");
                return axios.get(`/api/events/organizers/${organizerId}/events`);
            })
            .then((res) => {
                setEvents(res.data);
                setAddEventVisible(false);
                resetEventForm();
                setEditingEventId(null);
            })
            .catch((err) => {
                console.error("Błąd przy zapisie eventu:", err.response?.data || err.message);
                setError("Nie udało się zapisać wydarzenia.");
            });
    };

    const handleDeleteEvent = (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć to wydarzenie?")) return;

        axios
            .delete(`/api/events/${id}/delete`)
            .then(() => {
                setEvents((prevEvents) => prevEvents.filter((evt) => evt.id !== id));
                alert("Usunięto wydarzenie!");
            })
            .catch((err) => {
                console.error("Błąd przy usuwaniu eventu:", err);
                setError("Nie udało się usunąć wydarzenia.");
            });
    };

    if (!visible) return null;

    return (
        <div className="edit-courses-subsite">
            <h2 className="edit-courses-subsite__title">Zarządzaj swoimi wydarzeniami</h2>
            <div className="edit-courses-subsite__content">
                <div className="edit-courses-subsite__controls">
                    <button onClick={toggleFilters} className="edit-courses-subsite__filter-button">
                        {filtersVisible ? "Ukryj filtry" : "Filtruj"}
                    </button>
                    <button onClick={toggleAddEventForm} className="edit-courses-subsite__add-button">
                        {addEventVisible ? "Anuluj" : "Dodaj wydarzenie"}
                    </button>
                </div>

                {addEventVisible && (
                    <form className="edit-courses-subsite__add-course-form" onSubmit={handleEventSubmit}>
                        <label>
                            Nazwa:
                            <input type="text" name="name" value={newEvent.name} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Data rozpoczęcia:
                            <input type="date" name="startDate" value={newEvent.startDate} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Godzina rozpoczęcia:
                            <input type="time" name="startTime" value={newEvent.startTime} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Data zakończenia:
                            <input type="date" name="endDate" value={newEvent.endDate} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Godzina zakończenia:
                            <input type="time" name="endTime" value={newEvent.endTime} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Maksymalna liczba uczestników:
                            <input type="number" name="maxParticipants" value={newEvent.maxParticipants} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Minimalny wiek:
                            <input type="number" name="minAge" value={newEvent.minAge} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Informacje:
                            <textarea name="info" value={newEvent.info} onChange={handleInputChange} />
                        </label>
                        <label>
                            ID sali:
                            <input type="text" name="classroom" value={newEvent.classroom} onChange={handleInputChange} required />
                        </label>
                        <label>
                            Tagi (ID oddzielone przecinkami):
                            <input type="text" name="tags" value={newEvent.tags} onChange={handleInputChange} placeholder="np. 1,2,10" />
                        </label>
                        <button type="submit">{editingEventId ? "Zapisz" : "Dodaj"}</button>
                    </form>
                )}

                {error && <p className="error">{error}</p>}

                <div className="edit-courses-subsite__results">
                    <h3>Twoje wydarzenia:</h3>
                    {events.length === 0 ? (
                        <p>Brak wydarzeń.</p>
                    ) : (
                        events.map((evt) => (
                            <div key={evt.id} className="eventItem">
                                <p>
                                    <strong>Nazwa:</strong> {evt.name}
                                </p>
                                <p>
                                    <strong>Data rozpoczęcia:</strong> {evt.startDatetime}
                                </p>
                                <p>
                                    <strong>Data zakończenia:</strong> {evt.endDatetime}
                                </p>
                                <p>
                                    <strong>Organizator:</strong> {evt.organizerName}
                                </p>
                                <p>
                                    <strong>Sala:</strong> {evt.classroomName}
                                </p>
                                <p>
                                    <strong>Tagi:</strong> {evt.tagIds?.join(", ") || "Brak"}
                                </p>
                                <button onClick={() => prepareEventForEdit(evt)}>Edytuj</button>
                                <button onClick={() => handleDeleteEvent(evt.id)}>Usuń</button>
                                <hr />
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}

export default OrganizerEventList;
