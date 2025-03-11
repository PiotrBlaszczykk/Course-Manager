import React, { useState, useContext } from "react";
import "./OrganizerPanelPage.css";
import OrganizerEventList from "../../components/OrganizerEventList/OrganizerEventList.jsx";
import { AuthContext } from "../../context/AuthContext";

function OrganizerPanelPage() {
    const { user } = useContext(AuthContext);
    const [activeOption, setActiveOption] = useState("");

    function handleEditCourses() {
        setActiveOption("editCourses");
    }

    return (
        <div className="organizer-panel">
            <h1 className="organizer-panel__title">
                Witaj {user?.firstname} {user?.surname}!
            </h1>
            <div className="organizer-panel__options">
                <button
                    className="organizer-panel__button"
                    onClick={handleEditCourses}
                >
                    Organizowane przez Ciebie eventy
                </button>
            </div>
            {/* Przekazujemy ID organizatora do OrganizerEventList */}
            {activeOption === "editCourses" && user?.id && (
                <OrganizerEventList visible={true} organizerId={user.id} />
            )}
        </div>
    );
}

export default OrganizerPanelPage;
