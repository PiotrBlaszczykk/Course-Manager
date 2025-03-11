import React, { useEffect, useState } from "react";
import axios from "axios";
import "./UsersList.css";

function UsersList({ visible }) {
    // --- STANY ZWIĄZANE Z FILTRAMI ---
    const [filtersVisible, setFiltersVisible] = useState(false);
    const [selectedFilters, setSelectedFilters] = useState({
        organizers: false,
        sortAlphabetically: false,
        activeUsers: false,
    });

    // --- STANY ZWIĄZANE Z UŻYTKOWNIKAMI (CRUD) ---
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Dodawanie nowego użytkownika
    const [isAdding, setIsAdding] = useState(false);
    const [newUserData, setNewUserData] = useState({
        firstname: "",
        surname: "",
        age: "",
        email: "",
        password: "",
        isOrganizer: false, // Ujednolicamy nazwę
    });

    // Edycja istniejącego użytkownika
    const [editingUserId, setEditingUserId] = useState(null);
    const [editingUserData, setEditingUserData] = useState({
        firstname: "",
        surname: "",
        age: "",
        email: "",
        password: "",
        isOrganizer: false,
    });
    const [isUpdating, setIsUpdating] = useState(false);

    // --- POBRANIE LISTY UŻYTKOWNIKÓW (GET) ---
    const fetchUsers = () => {
        setLoading(true);
        axios
            .get("/api/users")
            .then((res) => {
                setUsers(res.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Błąd podczas pobierania użytkowników:", err);
                setError("Nie udało się załadować danych użytkowników.");
                setLoading(false);
            });
    };

    // Pobierz użytkowników po wejściu na widok (gdy `visible` będzie true)
    useEffect(() => {
        if (visible) {
            fetchUsers();
        }
    }, [visible]);

    // --- DODAWANIE NOWEGO UŻYTKOWNIKA (POST) ---
    const handleAddUserSubmit = (e) => {
        e.preventDefault();
        if (
            !newUserData.firstname ||
            !newUserData.surname ||
            !newUserData.email ||
            !newUserData.password
        ) {
            alert("Wypełnij wszystkie pola obowiązkowe (Imię, Nazwisko, Email, Hasło).");
            return;
        }

        axios
            .post("/api/users/register", {
                firstname: newUserData.firstname,
                surname: newUserData.surname,
                age: parseInt(newUserData.age, 10) || 0,
                email: newUserData.email,
                password: newUserData.password,
                isOrganizer: !!newUserData.isOrganizer,
            })
            .then((response) => {
                alert("Użytkownik został pomyślnie dodany!");
                // Dodajemy użytkownika do listy
                setUsers((prev) => [...prev, response.data]);

                // Reset i schowanie formularza
                setNewUserData({
                    firstname: "",
                    surname: "",
                    age: "",
                    email: "",
                    password: "",
                    isOrganizer: false,
                });
                setIsAdding(false);
            })
            .catch((err) => {
                console.error("Błąd podczas dodawania użytkownika:", err);
                alert("Nie udało się dodać użytkownika. Spróbuj ponownie.");
            });
    };

    // --- USUWANIE UŻYTKOWNIKA (DELETE) ---
    const handleDeleteUser = (userId) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tego użytkownika?")) return;

        axios
            .delete(`/api/users/${userId}`)
            .then(() => {
                // Usuń użytkownika z listy
                setUsers((prev) => prev.filter((user) => user.id !== userId));
                alert("Użytkownik został usunięty.");
            })
            .catch((err) => {
                console.error("Błąd podczas usuwania użytkownika:", err);
                alert("Nie udało się usunąć użytkownika. Spróbuj ponownie.");
            });
    };

    // --- ROZPOCZĘCIE EDYCJI UŻYTKOWNIKA ---
    const startEditing = (user) => {
        setEditingUserId(user.id);
        setEditingUserData({
            firstname: user.firstname || "",
            surname: user.surname || "",
            age: user.age || "",
            email: user.email || "",
            // Zakładamy, że serwer zwraca `user.password`,
            // co w rzeczywistości rzadko się zdarza.
            password: user.password || "",
            // Uwaga: w roboczym froncie jest `isOrganizer`, więc trzymamy się tej nazwy:
            isOrganizer: user.isOrganizer ?? false,
        });
    };

    // --- ZAKOŃCZENIE EDCYCJI (ANULOWANIE) ---
    const cancelEditing = () => {
        setEditingUserId(null);
        setEditingUserData({
            firstname: "",
            surname: "",
            age: "",
            email: "",
            password: "",
            isOrganizer: false,
        });
        setIsUpdating(false);
    };

    // --- ZAPIS ZMIAN W UŻYTKOWNIKU (PUT) ---
    const handleSaveUserChanges = () => {
        setIsUpdating(true);

        const updatedUser = {
            firstname: editingUserData.firstname,
            surname: editingUserData.surname,
            age: parseInt(editingUserData.age, 10) || 0,
            email: editingUserData.email,
            password: editingUserData.password,
            // Najważniejsze: Wysyłamy `isOrganizer`, tak jak w roboczym froncie
            isOrganizer: !!editingUserData.isOrganizer,
        };

        axios
            .put(`/api/users/${editingUserId}`, updatedUser)
            .then((res) => {
                alert("Zmiany zostały zapisane.");
                // Aktualizujemy listę w stanie (nadpisujemy dane)
                setUsers((prev) => prev.map((u) => (u.id === editingUserId ? res.data : u)));
                cancelEditing();
            })
            .catch((err) => {
                console.error("Błąd podczas zapisywania zmian:", err);
                alert("Nie udało się zapisać zmian. Spróbuj ponownie.");
                setIsUpdating(false);
            });
    };

    // --- PRZEŁĄCZANIE ROLI ORGANIZATORA W TRAKCIE EDEKCJI ---
    const toggleOrganizer = () => {
        setEditingUserData((prev) => ({
            ...prev,
            isOrganizer: !prev.isOrganizer,
        }));
    };

    // --- FILTROWANIE / SORTOWANIE (LOKALNIE) ---
    const applyFilters = (originalUsers) => {
        let filtered = [...originalUsers];

        if (selectedFilters.organizers) {
            filtered = filtered.filter((u) => u.isOrganizer === true);
        }
        if (selectedFilters.sortAlphabetically) {
            filtered.sort((a, b) => a.surname.localeCompare(b.surname));
        }
        if (selectedFilters.activeUsers) {
            // Przykład: jeśli jest pole user.active === true
            filtered = filtered.filter((u) => u.active === true);
        }
        return filtered;
    };

    // --- OBSŁUGA ZDARZEŃ (SEARCH, TOGGLE) ---
    const handleSearchClick = () => {
        console.log("Searching users with filters:", selectedFilters);
    };

    const toggleFilters = () => {
        setFiltersVisible(!filtersVisible);
    };

    const handleFilterChange = (filterName) => {
        setSelectedFilters((prev) => ({
            ...prev,
            [filterName]: !prev[filterName],
        }));
    };

    // --- RENDER ---
    if (!visible) return null;

    if (loading) {
        return <p className="loadingMsg">Ładowanie użytkowników...</p>;
    }
    if (error) {
        return <p className="errorMsg">{error}</p>;
    }

    // Przefiltruj użytkowników lokalnie
    const displayedUsers = applyFilters(users);

    return (
        <div className="users-subsite">
            <h2 className="users-subsite__title">Search Users</h2>
            <div className="users-subsite__content">
                {/* --- PRZYCISKI FILTR / SEARCH --- */}
                <div className="users-subsite__controls">
                    <button
                        className="users-subsite__filter-button"
                        onClick={toggleFilters}
                    >
                        {filtersVisible ? "Hide Filters" : "Filter"}
                    </button>
                    <button
                        className="users-subsite__search-button"
                        onClick={handleSearchClick}
                    >
                        Search
                    </button>
                </div>

                {/* --- SEKCJA FILTRÓW --- */}
                {filtersVisible && (
                    <div className="users-subsite__filters">
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.organizers}
                                onChange={() => handleFilterChange("organizers")}
                            />
                            Show only organizers
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.sortAlphabetically}
                                onChange={() => handleFilterChange("sortAlphabetically")}
                            />
                            Sort Alphabetically
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.activeUsers}
                                onChange={() => handleFilterChange("activeUsers")}
                            />
                            Show Active Users
                        </label>
                    </div>
                )}

                {/* --- DODAWANIE NOWEGO UŻYTKOWNIKA --- */}
                {!isAdding ? (
                    <div style={{ marginBottom: "1rem" }}>
                        <button onClick={() => setIsAdding(true)}>Add User</button>
                    </div>
                ) : (
                    <form className="addUserForm" onSubmit={handleAddUserSubmit}>
                        <h3>Add a new User</h3>

                        <div className="formGroup">
                            <label>First Name *</label>
                            <input
                                type="text"
                                value={newUserData.firstname}
                                onChange={(e) =>
                                    setNewUserData((prev) => ({ ...prev, firstname: e.target.value }))
                                }
                                required
                            />
                        </div>

                        <div className="formGroup">
                            <label>Last Name *</label>
                            <input
                                type="text"
                                value={newUserData.surname}
                                onChange={(e) =>
                                    setNewUserData((prev) => ({ ...prev, surname: e.target.value }))
                                }
                                required
                            />
                        </div>

                        <div className="formGroup">
                            <label>Age</label>
                            <input
                                type="number"
                                value={newUserData.age}
                                onChange={(e) =>
                                    setNewUserData((prev) => ({ ...prev, age: e.target.value }))
                                }
                            />
                        </div>

                        <div className="formGroup">
                            <label>Email *</label>
                            <input
                                type="email"
                                value={newUserData.email}
                                onChange={(e) =>
                                    setNewUserData((prev) => ({ ...prev, email: e.target.value }))
                                }
                                required
                            />
                        </div>

                        <div className="formGroup">
                            <label>Password *</label>
                            <input
                                type="text"
                                value={newUserData.password}
                                onChange={(e) =>
                                    setNewUserData((prev) => ({ ...prev, password: e.target.value }))
                                }
                                required
                            />
                        </div>

                        <div className="formGroup">
                            <button
                                type="button"
                                onClick={() =>
                                    setNewUserData((prev) => ({
                                        ...prev,
                                        isOrganizer: !prev.isOrganizer,
                                    }))
                                }
                            >
                                {newUserData.isOrganizer
                                    ? "Odbierz uprawnienia organizatora"
                                    : "Nadaj uprawnienia organizatora"}
                            </button>
                        </div>

                        <div style={{ marginTop: "1rem" }}>
                            <button type="submit">Save</button>
                            <button type="button" onClick={() => setIsAdding(false)}>
                                Cancel
                            </button>
                        </div>
                    </form>
                )}

                {/* --- LISTA UŻYTKOWNIKÓW (WYNIKI) --- */}
                <div className="users-subsite__results">
                    {displayedUsers.length === 0 ? (
                        <p>No users found.</p>
                    ) : (
                        displayedUsers.map((user) => {
                            const isEditingThisUser = user.id === editingUserId;
                            return (
                                <div key={user.id} className="singleUserContainer">
                                    {isEditingThisUser ? (
                                        <div className="editMode">
                                            <h3>Edit User</h3>
                                            <div className="formGroup">
                                                <label>First Name</label>
                                                <input
                                                    type="text"
                                                    value={editingUserData.firstname}
                                                    onChange={(e) =>
                                                        setEditingUserData((prev) => ({
                                                            ...prev,
                                                            firstname: e.target.value,
                                                        }))
                                                    }
                                                />
                                            </div>
                                            <div className="formGroup">
                                                <label>Last Name</label>
                                                <input
                                                    type="text"
                                                    value={editingUserData.surname}
                                                    onChange={(e) =>
                                                        setEditingUserData((prev) => ({
                                                            ...prev,
                                                            surname: e.target.value,
                                                        }))
                                                    }
                                                />
                                            </div>
                                            <div className="formGroup">
                                                <label>Age</label>
                                                <input
                                                    type="number"
                                                    value={editingUserData.age}
                                                    onChange={(e) =>
                                                        setEditingUserData((prev) => ({
                                                            ...prev,
                                                            age: e.target.value,
                                                        }))
                                                    }
                                                />
                                            </div>
                                            <div className="formGroup">
                                                <label>Email</label>
                                                <input
                                                    type="email"
                                                    value={editingUserData.email}
                                                    onChange={(e) =>
                                                        setEditingUserData((prev) => ({
                                                            ...prev,
                                                            email: e.target.value,
                                                        }))
                                                    }
                                                />
                                            </div>
                                            <div className="formGroup">
                                                <label>Password (will overwrite if changed)</label>
                                                <input
                                                    type="text"
                                                    value={editingUserData.password}
                                                    onChange={(e) =>
                                                        setEditingUserData((prev) => ({
                                                            ...prev,
                                                            password: e.target.value,
                                                        }))
                                                    }
                                                />
                                            </div>

                                            {/* Toggler dla roli organizatora */}
                                            <div className="formGroup">
                                                <button type="button" onClick={toggleOrganizer}>
                                                    {editingUserData.isOrganizer
                                                        ? "Odbierz uprawnienia organizatora"
                                                        : "Nadaj uprawnienia organizatora"}
                                                </button>
                                            </div>

                                            <div style={{ marginTop: "1rem" }}>
                                                <button onClick={handleSaveUserChanges} disabled={isUpdating}>
                                                    {isUpdating ? "Saving..." : "Save"}
                                                </button>
                                                <button onClick={cancelEditing}>Cancel</button>
                                            </div>
                                        </div>
                                    ) : (
                                        <div className="viewMode">
                                            <p>
                                                <strong>Name:</strong> {user.firstname} {user.surname}
                                            </p>
                                            <p>
                                                <strong>Age:</strong> {user.age || "N/A"}
                                            </p>
                                            <p>
                                                <strong>Email:</strong> {user.email}
                                            </p>
                                            <p>
                                                <strong>Organizer?</strong>{" "}
                                                {user.isOrganizer ? "Yes" : "No"}
                                            </p>
                                            <button onClick={() => startEditing(user)}>Edit</button>
                                            <button onClick={() => handleDeleteUser(user.id)}>
                                                Delete
                                            </button>
                                        </div>
                                    )}
                                </div>
                            );
                        })
                    )}
                </div>
            </div>
        </div>
    );
}

export default UsersList;
