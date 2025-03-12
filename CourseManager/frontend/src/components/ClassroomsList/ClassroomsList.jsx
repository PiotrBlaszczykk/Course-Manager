import React, { useState, useEffect } from "react";
import axios from "axios";
import "./ClassroomsList.css";

function ClassroomsList({ visible }) {
    const [filtersVisible, setFiltersVisible] = useState(false);
    const [addClassroomVisible, setAddClassroomVisible] = useState(false);
    const [selectedFilters, setSelectedFilters] = useState({
        onlyLarge: false,
        groundFloor: false,
    });

    const [newClassroom, setNewClassroom] = useState({
        classroomName: "",
        capacity: "",
        location: "",
        info: "",
    });

    const [classrooms, setClassrooms] = useState([]);
    const [editingClassroomId, setEditingClassroomId] = useState(null);
    const [editingClassroomData, setEditingClassroomData] = useState({
        id: null,
        classroomName: "",
        capacity: "",
        location: "",
        info: "",
    });
    const [isUpdating, setIsUpdating] = useState(false);

    const fetchClassrooms = () => {
        axios
            .get("/api/classrooms")
            .then((res) => {
                setClassrooms(res.data);
            })
            .catch((err) => {
                console.error("Błąd podczas pobierania sal:", err);
                alert("Nie udało się pobrać listy sal. Spróbuj ponownie.");
            });
    };

    useEffect(() => {
        if (visible) {
            fetchClassrooms();
        }
    }, [visible]);

    const handleAddClassroomSubmit = (e) => {
        e.preventDefault();

        if (!newClassroom.classroomName || !newClassroom.capacity || !newClassroom.location) {
            alert("Proszę wypełnić wszystkie wymagane pola.");
            return;
        }

        axios
            .post("/api/classrooms", {
                classroomName: newClassroom.classroomName,
                capacity: parseInt(newClassroom.capacity, 10),
                location: newClassroom.location,
                info: newClassroom.info,
            })
            .then(() => {
                alert("Sala została dodana!");
                setNewClassroom({
                    classroomName: "",
                    capacity: "",
                    location: "",
                    info: "",
                });
                setAddClassroomVisible(false);
                fetchClassrooms();
            })
            .catch((err) => {
                console.error("Błąd podczas dodawania sali:", err);
                alert("Nie udało się dodać sali. Spróbuj ponownie.");
            });
    };

    const startEditing = (classroom) => {
        setEditingClassroomId(classroom.id);
        setEditingClassroomData({ ...classroom });
    };

    const cancelEditing = () => {
        setEditingClassroomId(null);
        setEditingClassroomData({
            id: null,
            classroomName: "",
            capacity: "",
            location: "",
            info: "",
        });
        setIsUpdating(false);
    };

    const saveClassroomChanges = () => {
        setIsUpdating(true);
        axios
            .put(`/api/classrooms/${editingClassroomData.id}`, editingClassroomData)
            .then(() => {
                alert("Zmiany zostały zapisane.");
                cancelEditing();
                fetchClassrooms();
            })
            .catch((err) => {
                console.error("Błąd podczas zapisywania zmian:", err);
                alert("Nie udało się zapisać zmian. Spróbuj ponownie.");
                setIsUpdating(false);
            });
    };

    const deleteClassroom = (classroomId, classroomName) => {
        if (window.confirm(`Czy na pewno chcesz usunąć salę "${classroomName}"?`)) {
            axios
                .delete(`/api/classrooms/${classroomId}`)
                .then(() => {
                    alert("Sala została usunięta.");
                    fetchClassrooms();
                })
                .catch((err) => {
                    console.error("Błąd podczas usuwania sali:", err);
                    alert("Nie udało się usunąć sali. Spróbuj ponownie.");
                });
        }
    };

    if (!visible) return null;

    return (
        <div className="edit-classroom-subsite">
            <h2 className="edit-classroom-subsite__title">Edit Classrooms</h2>
            <div className="edit-classroom-subsite__content">
                <div className="edit-classroom-subsite__controls">
                    <button
                        className="edit-classroom-subsite__filter-button"
                        onClick={() => setFiltersVisible(!filtersVisible)}
                    >
                        {filtersVisible ? "Hide Filters" : "Filter"}
                    </button>
                    <button
                        className="edit-classroom-subsite__search-button"
                        onClick={fetchClassrooms}
                    >
                        Search
                    </button>
                    <button
                        className={`edit-classroom-subsite__add-button ${
                            addClassroomVisible ? "cancel" : "add"
                        }`}
                        onClick={() => setAddClassroomVisible(!addClassroomVisible)}
                    >
                        {addClassroomVisible ? "Cancel" : "Add Classroom"}
                    </button>
                </div>

                {filtersVisible && (
                    <div className="edit-classroom-subsite__filters">
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.onlyLarge}
                                onChange={() =>
                                    setSelectedFilters((prev) => ({ ...prev, onlyLarge: !prev.onlyLarge }))
                                }
                            />
                            Show Only Large Classrooms
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.groundFloor}
                                onChange={() =>
                                    setSelectedFilters((prev) => ({
                                        ...prev,
                                        groundFloor: !prev.groundFloor,
                                    }))
                                }
                            />
                            Show Only Ground Floor
                        </label>
                    </div>
                )}

                {addClassroomVisible && (
                    <form
                        className="edit-classroom-subsite__add-classroom-form"
                        onSubmit={handleAddClassroomSubmit}
                    >
                        <label>
                            Classroom Name:
                            <input
                                type="text"
                                name="classroomName"
                                value={newClassroom.classroomName}
                                onChange={(e) =>
                                    setNewClassroom((prev) => ({ ...prev, classroomName: e.target.value }))
                                }
                                required
                            />
                        </label>
                        <label>
                            Capacity:
                            <input
                                type="number"
                                name="capacity"
                                value={newClassroom.capacity}
                                onChange={(e) =>
                                    setNewClassroom((prev) => ({ ...prev, capacity: e.target.value }))
                                }
                                required
                            />
                        </label>
                        <label>
                            Location:
                            <input
                                type="text"
                                name="location"
                                value={newClassroom.location}
                                onChange={(e) =>
                                    setNewClassroom((prev) => ({ ...prev, location: e.target.value }))
                                }
                                required
                            />
                        </label>
                        <label>
                            Info:
                            <textarea
                                name="info"
                                value={newClassroom.info}
                                onChange={(e) =>
                                    setNewClassroom((prev) => ({ ...prev, info: e.target.value }))
                                }
                            />
                        </label>
                        <button type="submit" className="edit-classroom-subsite__submit-button">
                            Submit
                        </button>
                    </form>
                )}

                <div className="edit-classroom-subsite__results">
                    {classrooms.map((classroom) => (
                        <div key={classroom.id} className="singleClassroom">
                            {classroom.id === editingClassroomId ? (
                                <div className="editMode">
                                    <h2>Edit Classroom</h2>
                                    <label>
                                        Name:
                                        <input
                                            type="text"
                                            value={editingClassroomData.classroomName}
                                            onChange={(e) =>
                                                setEditingClassroomData((prev) => ({
                                                    ...prev,
                                                    classroomName: e.target.value,
                                                }))
                                            }
                                        />
                                    </label>
                                    <label>
                                        Capacity:
                                        <input
                                            type="number"
                                            value={editingClassroomData.capacity}
                                            onChange={(e) =>
                                                setEditingClassroomData((prev) => ({
                                                    ...prev,
                                                    capacity: e.target.value,
                                                }))
                                            }
                                        />
                                    </label>
                                    <label>
                                        Location:
                                        <input
                                            type="text"
                                            value={editingClassroomData.location}
                                            onChange={(e) =>
                                                setEditingClassroomData((prev) => ({
                                                    ...prev,
                                                    location: e.target.value,
                                                }))
                                            }
                                        />
                                    </label>
                                    <label>
                                        Info:
                                        <textarea
                                            value={editingClassroomData.info}
                                            onChange={(e) =>
                                                setEditingClassroomData((prev) => ({
                                                    ...prev,
                                                    info: e.target.value,
                                                }))
                                            }
                                        />
                                    </label>
                                    <button onClick={saveClassroomChanges} disabled={isUpdating}>
                                        Save
                                    </button>
                                    <button onClick={cancelEditing}>Cancel</button>
                                </div>
                            ) : (
                                <div className="viewMode">
                                    <h3>{classroom.classroomName}</h3>
                                    <p><strong>Capacity:</strong> {classroom.capacity}</p>
                                    <p><strong>Location:</strong> {classroom.location}</p>
                                    <p><strong>Info:</strong> {classroom.info || "No additional info"}</p>
                                    <button onClick={() => startEditing(classroom)}>Edit</button>
                                    <button
                                        onClick={() => deleteClassroom(classroom.id, classroom.classroomName)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default ClassroomsList;
