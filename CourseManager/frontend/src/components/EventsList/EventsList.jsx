import React, { useState, useEffect } from "react";
import axios from "axios";
import "./EventsList.css";

function EventsList({ visible }) {
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
        organizer: "",
        classroom: "",
        tags: "",
    });

    // Pobieranie listy eventów
    useEffect(() => {
        if (visible) {
            axios
                .get("/api/events")
                .then((res) => {
                    setEvents(res.data);
                    setError(null);
                })
                .catch((err) => {
                    console.error("Błąd podczas pobierania eventów:", err);
                    setError("Nie udało się pobrać eventów.");
                });
        }
    }, [visible]);

    const toggleFilters = () => setFiltersVisible(!filtersVisible);

    const toggleAddEventForm = () => {
        setAddEventVisible(!addEventVisible);
        setEditingEventId(null); // Reset trybu edycji
        resetEventForm();
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewEvent((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    /**
     * Przygotowuje wybrany event do edycji, rozbijając daty i czasy,
     * a także ucinając sekundy, by <input type="time" /> wyświetlał się prawidłowo.
     */
    const prepareEventForEdit = (event) => {
        // Rozdzielamy datę/czas
        const [startDatePart, startTimeFull] = event.startDatetime.split("T");
        const [endDatePart, endTimeFull] = event.endDatetime.split("T");

        // Ucinamy sekundy: "HH:MM:SS" -> "HH:MM"
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
            organizer: event.organizerId,
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
            organizer: "",
            classroom: "",
            tags: "",
        });
    };

    /**
     * Obsługuje zapisywanie (lub tworzenie) eventu.
     * Dodajemy ":00" do czasu, by backend dostawał pełny format HH:MM:SS.
     */
    const handleEventSubmit = (e) => {
        e.preventDefault();

        const tagIds = newEvent.tags
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t.length > 0)
            .map((t) => parseInt(t, 10))
            .filter((id) => !isNaN(id));

        // Doklejamy ":00", aby zawsze mieć HH:MM:SS
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
            organizerId: parseInt(newEvent.organizer, 10),
            classroomId: parseInt(newEvent.classroom, 10),
            tagIds,
        };

        console.log("Wysyłane dane:", requestBody);

        const request =
            editingEventId !== null
                ? axios.put(`/api/events/${editingEventId}/update`, requestBody)
                : axios.post("/api/events/create", requestBody);

        request
            .then(() => {
                alert(
                    editingEventId
                        ? "Event updated successfully!"
                        : "Event created successfully!"
                );
                return axios.get("/api/events");
            })
            .then((res) => {
                setEvents(res.data);
                setAddEventVisible(false);
                resetEventForm();
                setEditingEventId(null);
            })
            .catch((err) => {
                console.error("Błąd przy zapisie eventu:", err.response?.data || err.message);
                setError("Nie udało się zapisać eventu.");
            });
    };

    const handleDeleteEvent = (id) => {
        if (!window.confirm("Are you sure you want to delete this event?")) return;

        axios
            .delete(`/api/events/${id}/delete`)
            .then(() => {
                setEvents((prevEvents) => prevEvents.filter((evt) => evt.id !== id));
                alert("Event deleted successfully!");
            })
            .catch((err) => {
                console.error("Błąd przy usuwaniu eventu:", err);
                setError("Nie udało się usunąć eventu.");
            });
    };

    if (!visible) return null;

    return (
        <div className="edit-courses-subsite">
            <h2 className="edit-courses-subsite__title">Edit Events</h2>
            <div className="edit-courses-subsite__content">
                <div className="edit-courses-subsite__controls">
                    <button onClick={toggleFilters} className="edit-courses-subsite__filter-button">
                        {filtersVisible ? "Hide Filters" : "Filter"}
                    </button>
                    <button onClick={toggleAddEventForm} className="edit-courses-subsite__add-button">
                        {addEventVisible ? "Cancel" : "Add Event"}
                    </button>
                </div>

                {addEventVisible && (
                    <form className="edit-courses-subsite__add-course-form" onSubmit={handleEventSubmit}>
                        <label>
                            Name:
                            <input
                                type="text"
                                name="name"
                                value={newEvent.name}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Start Date:
                            <input
                                type="date"
                                name="startDate"
                                value={newEvent.startDate}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Start Time:
                            <input
                                type="time"
                                name="startTime"
                                value={newEvent.startTime}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            End Date:
                            <input
                                type="date"
                                name="endDate"
                                value={newEvent.endDate}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            End Time:
                            <input
                                type="time"
                                name="endTime"
                                value={newEvent.endTime}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Max Participants:
                            <input
                                type="number"
                                name="maxParticipants"
                                value={newEvent.maxParticipants}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Min Age:
                            <input
                                type="number"
                                name="minAge"
                                value={newEvent.minAge}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Info:
                            <textarea
                                name="info"
                                value={newEvent.info}
                                onChange={handleInputChange}
                            />
                        </label>
                        <label>
                            Organizer ID:
                            <input
                                type="text"
                                name="organizer"
                                value={newEvent.organizer}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Classroom ID:
                            <input
                                type="text"
                                name="classroom"
                                value={newEvent.classroom}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Tags (comma-separated IDs):
                            <input
                                type="text"
                                name="tags"
                                value={newEvent.tags}
                                onChange={handleInputChange}
                                placeholder="np. 1,2,10"
                            />
                        </label>
                        <button type="submit">{editingEventId ? "Update" : "Submit"}</button>
                    </form>
                )}

                {error && <p className="error">{error}</p>}

                <div className="edit-courses-subsite__results">
                    <h3>Existing Events:</h3>
                    {events.length === 0 ? (
                        <p>No events found.</p>
                    ) : (
                        events.map((evt) => (
                            <div key={evt.id} className="eventItem">
                                <p><strong>Name:</strong> {evt.name}</p>
                                <p><strong>Start:</strong> {evt.startDatetime}</p>
                                <p><strong>End:</strong> {evt.endDatetime}</p>
                                <p><strong>Organizer:</strong> {evt.organizerName}</p>
                                <p><strong>Classroom:</strong> {evt.classroomName}</p>
                                <p><strong>Tags:</strong> {evt.tagIds?.join(", ") || "None"}</p>
                                <button onClick={() => prepareEventForEdit(evt)}>Edit</button>
                                <button onClick={() => handleDeleteEvent(evt.id)}>Delete</button>
                                <hr />
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}

export default EventsList;
