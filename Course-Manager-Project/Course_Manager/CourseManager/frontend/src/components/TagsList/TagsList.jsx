import React, { useState, useEffect } from "react";
import axios from "axios";
import "./TagsList.css";

function TagsList({ visible }) {
    // --- STANY KOMPONENTU ---
    // Obsługa widoczności sekcji
    const [filtersVisible, setFiltersVisible] = useState(false);
    const [addTagVisible, setAddTagVisible] = useState(false);

    // Ustawienia filtrów
    const [selectedFilters, setSelectedFilters] = useState({
        alphabetical: false,
        byUsage: false,
    });

    // Nowy tag (formularz dodawania)
    const [newTag, setNewTag] = useState({
        name: "",
    });

    // Lista tagów pobrana z backendu
    const [tags, setTags] = useState([]);

    // Stany obsługujące edycję tagu
    const [editingTagId, setEditingTagId] = useState(null);
    const [editingTagData, setEditingTagData] = useState({
        id: null,
        name: "",
    });
    const [isUpdating, setIsUpdating] = useState(false);

    // --- FUNKCJE POMOCNICZE ---

    // Pobierz wszystkie tagi z backendu
    const fetchTags = () => {
        axios
            .get("/api/tags")
            .then((res) => {
                setTags(res.data);
            })
            .catch((err) => {
                console.error("Błąd podczas pobierania tagów:", err);
                alert("Nie udało się pobrać listy tagów. Spróbuj ponownie.");
            });
    };

    // Załaduj tagi przy pierwszym renderze
    useEffect(() => {
        if (visible) {
            fetchTags();
        }
    }, [visible]);

    // Przełącz widoczność sekcji filtrów
    const toggleFilters = () => {
        setFiltersVisible(!filtersVisible);
    };

    // Przełącz widoczność formularza "Add Tag"
    const toggleAddTagForm = () => {
        setAddTagVisible(!addTagVisible);
    };

    // Zmiana checkboxa filtra
    const handleFilterChange = (filter) => {
        setSelectedFilters((prevFilters) => ({
            ...prevFilters,
            [filter]: !prevFilters[filter],
        }));
    };

    // Zmiana inputu w formularzu nowego tagu
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewTag((prevTag) => ({
            ...prevTag,
            [name]: value,
        }));
    };

    // Obsługa przycisku "Search" (np. można tu wywołać ponowne pobranie tagów z uwzględnieniem filtrów)
    const handleSearchClick = () => {
        // Tu można rozwinąć logikę, np. filtry na backendzie
        console.log("Applying filters:", selectedFilters);
        // Na potrzeby przykładu tylko wywołamy ponownie fetchTags()
        fetchTags();
    };

    // Dodawanie nowego tagu (zastępujemy console.log kodem z AddTag)
    const handleAddTagSubmit = (e) => {
        e.preventDefault();

        if (!newTag.name) {
            alert("Proszę wypełnić pole nazwy tagu.");
            return;
        }

        axios
            .post("/api/tags", {
                name: newTag.name,
            })
            .then((response) => {
                alert("Tag został pomyślnie dodany!");
                // Czyścimy formularz
                setNewTag({ name: "" });
                // Ukrywamy formularz
                setAddTagVisible(false);
                // Odświeżamy listę tagów
                fetchTags();
            })
            .catch((err) => {
                console.error("Błąd podczas dodawania tagu:", err);
                alert("Nie udało się dodać tagu. Spróbuj ponownie.");
            });
    };

    // Inicjalizacja trybu edycji dla konkretnego tagu
    const startEditing = (tag) => {
        setEditingTagId(tag.id);
        setEditingTagData({ id: tag.id, name: tag.name });
    };

    // Anulowanie edycji
    const cancelEditing = () => {
        setEditingTagId(null);
        setEditingTagData({ id: null, name: "" });
        setIsUpdating(false);
    };

    // Obsługa zmian w formularzu edycji
    const handleEditInputChange = (e) => {
        const { name, value } = e.target;
        setEditingTagData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    // Zapisywanie zmian (PUT)
    const saveTagChanges = () => {
        setIsUpdating(true);
        axios
            .put(`/api/tags/${editingTagData.id}`, {
                name: editingTagData.name,
            })
            .then(() => {
                alert("Zmiany zostały zapisane.");
                setIsUpdating(false);
                cancelEditing();
                fetchTags();
            })
            .catch((err) => {
                console.error("Błąd podczas zapisywania zmian:", err);
                alert("Nie udało się zapisać zmian. Spróbuj ponownie.");
                setIsUpdating(false);
            });
    };

    // Usuwanie tagu (DELETE)
    const deleteTag = (tagId, tagName) => {
        if (window.confirm(`Czy na pewno chcesz usunąć tag "${tagName}"?`)) {
            axios
                .delete(`/api/tags/${tagId}`)
                .then(() => {
                    alert("Tag został usunięty.");
                    fetchTags();
                })
                .catch((err) => {
                    console.error("Błąd podczas usuwania tagu:", err);
                    alert("Nie udało się usunąć tagu. Spróbuj ponownie.");
                });
        }
    };

    // Jeśli komponent ma być niewidoczny - nic nie renderujemy
    if (!visible) return null;

    // --- FILTROWANIE / SORTOWANIE TAGÓW PO STRONIE FRONTU (przykład) ---
    let displayedTags = [...tags];
    // Sortowanie alfabetyczne (opcjonalne)
    if (selectedFilters.alphabetical) {
        displayedTags.sort((a, b) => a.name.localeCompare(b.name));
    }
    // Sortowanie "byUsage" – tutaj brak implementacji, można dodać logikę

    return (
        <div className="edit-tags-subsite">
            <h2 className="edit-tags-subsite__title">Edit Tags</h2>
            <div className="edit-tags-subsite__content">
                {/* --- PANEL KONTROLNY Z PRZYCISKAMI --- */}
                <div className="edit-tags-subsite__controls">
                    <button
                        className="edit-tags-subsite__filter-button"
                        onClick={toggleFilters}
                    >
                        {filtersVisible ? "Hide Filters" : "Filter"}
                    </button>

                    <button
                        className="edit-tags-subsite__search-button"
                        onClick={handleSearchClick}
                    >
                        Search
                    </button>

                    <button
                        className={`edit-tags-subsite__add-button ${
                            addTagVisible ? "cancel" : "add"
                        }`}
                        onClick={toggleAddTagForm}
                    >
                        {addTagVisible ? "Cancel" : "Add Tag"}
                    </button>
                </div>

                {/* --- SEKCJA FILTRÓW --- */}
                {filtersVisible && (
                    <div className="edit-tags-subsite__filters">
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.alphabetical}
                                onChange={() => handleFilterChange("alphabetical")}
                            />
                            Sort Alphabetically
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={selectedFilters.byUsage}
                                onChange={() => handleFilterChange("byUsage")}
                            />
                            Sort by Usage
                        </label>
                    </div>
                )}

                {/* --- FORMULARZ DODAWANIA NOWEGO TAGU --- */}
                {addTagVisible && (
                    <form
                        className="edit-tags-subsite__add-tag-form"
                        onSubmit={handleAddTagSubmit}
                    >
                        <label>
                            Name:
                            <input
                                type="text"
                                name="name"
                                value={newTag.name}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <button type="submit" className="edit-tags-subsite__submit-button">
                            Submit
                        </button>
                    </form>
                )}

                {/* --- PODGLĄD AKTYWNYCH FILTRÓW --- */}
                <div className="edit-tags-subsite__stats">
                    <p>
                        Filters Applied:{" "}
                        {Object.keys(selectedFilters)
                            .filter((key) => selectedFilters[key])
                            .join(", ") || "None"}
                    </p>
                </div>

                {/* --- LISTA / WYNIKI TAGÓW --- */}
                <div className="edit-tags-subsite__results">
                    {displayedTags.length === 0 ? (
                        <p>No tags found.</p>
                    ) : (
                        displayedTags.map((tag) => {
                            // Jeśli dany tag jest w trybie edycji, pokaż formularz
                            if (tag.id === editingTagId) {
                                return (
                                    <div key={tag.id} className="singleTag">
                                        <div className="editMode">
                                            <h2>Edit Tag</h2>
                                            <div className="formGroup">
                                                <label htmlFor={`tagName-${tag.id}`}>Tag Name</label>
                                                <input
                                                    type="text"
                                                    id={`tagName-${tag.id}`}
                                                    name="name"
                                                    value={editingTagData.name}
                                                    onChange={handleEditInputChange}
                                                />
                                            </div>
                                            <div className="tagActions">
                                                <button
                                                    onClick={saveTagChanges}
                                                    disabled={isUpdating}
                                                    className="saveButton"
                                                >
                                                    {isUpdating ? "Saving..." : "Save"}
                                                </button>
                                                <button
                                                    onClick={cancelEditing}
                                                    className="cancelButton"
                                                >
                                                    Cancel
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                );
                            } else {
                                // Widok do odczytu
                                return (
                                    <div key={tag.id} className="singleTag">
                                        <div className="viewMode">
                                            <h2>{tag.name}</h2>
                                            <div className="tagActions">
                                                <button
                                                    onClick={() => startEditing(tag)}
                                                    className="editButton"
                                                >
                                                    Edit
                                                </button>
                                                <button
                                                    onClick={() => deleteTag(tag.id, tag.name)}
                                                    className="deleteButton"
                                                >
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                );
                            }
                        })
                    )}
                </div>
            </div>
        </div>
    );
}

export default TagsList;
