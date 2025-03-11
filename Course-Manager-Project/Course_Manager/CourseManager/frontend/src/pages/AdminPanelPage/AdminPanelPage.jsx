import React, { useState } from "react";
import "./AdminPanelPage.css";
import UsersList from "../../components/UsersList/UsersList.jsx";
import EventsList from "../../components/EventsList/EventsList.jsx";
import TagsList from "../../components/TagsList/TagsList.jsx";
import ClassroomsList from "../../components/ClassroomsList/ClassroomsList.jsx";

function AdminPanelPage() {
    const [activeOption, setActiveOption] = useState("");

    function handleEditUsers() {
        setActiveOption("searchUsers");
    }

    function handleEditCourses() {
        setActiveOption("editCourses");
    }

    function handleEditTags() {
        setActiveOption("editTags");
    }

    function handleEditClassroom() {
        setActiveOption("editClassroom");
    }

    return (
        <div className="admin-panel">
            <h1 className="admin-panel__title">Admin Panel</h1>
            <div className="admin-panel__options">
                <button
                    className="admin-panel__button"
                    onClick={handleEditUsers}
                >
                    Edit Users
                </button>
                <button
                    className="admin-panel__button"
                    onClick={handleEditCourses}
                >
                    Edit Courses
                </button>
                <button
                    className="admin-panel__button"
                    onClick={handleEditTags}
                >
                    Edit Tags
                </button>
                <button
                    className="admin-panel__button"
                    onClick={handleEditClassroom}
                >
                    Edit Classroom
                </button>
            </div>
            <UsersList visible={activeOption === "searchUsers"} />
            <EventsList visible={activeOption === "editCourses"} />
            <TagsList visible={activeOption === "editTags"} />
            <ClassroomsList visible={activeOption === "editClassroom"} />
        </div>
    );
}


export default AdminPanelPage;
