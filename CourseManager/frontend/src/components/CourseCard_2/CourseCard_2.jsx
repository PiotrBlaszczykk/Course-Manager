import { useState } from "react";
import "./CourseCard_2.css";

function CourseCard_2({ title, description, organizer, classroom, startDatetime, endDatetime, tags }) {
    const [showDetails, setShowDetails] = useState(false);

    const toggleDetails = () => {
        setShowDetails(!showDetails);
    };

    return (
        <div className="card">
            <div className="card__image"></div>
            <div className="card__content">
                <h2 className="card__title">{title}</h2>
                <p className="card__description">{description}</p>
                <button className="card__button" onClick={toggleDetails}>
                    {showDetails ? "SHOW LESS" : "READ MORE"}
                </button>
                {showDetails && (
                    <div className="card__details">
                        <p><strong>Organizer:</strong> {organizer}</p>
                        <p><strong>Classroom:</strong> {classroom}</p>
                        <p><strong>Start:</strong> {new Date(startDatetime).toLocaleString()}</p>
                        <p><strong>End:</strong> {new Date(endDatetime).toLocaleString()}</p>
                        {tags && tags.length > 0 && (
                            <p><strong>Tags:</strong> {tags.join(", ")}</p>
                        )}
                        <button className="card__enroll-button">Zapisz się na event</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default CourseCard_2;
