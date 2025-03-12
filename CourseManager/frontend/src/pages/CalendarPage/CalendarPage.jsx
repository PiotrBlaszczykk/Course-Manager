import React, { useState } from "react";
import "./CalendarPage.css";

function CalendarPage() {
    const schedule = {
        "2025-01-08": ["Mathematics - 10:00 AM", "Physics - 2:00 PM"],
        "2025-01-15": ["Chemistry - 1:00 PM"],
        "2025-01-20": ["Biology - 9:00 AM"],
        "2025-01-25": ["Computer Science - 11:30 AM"],
    };

    const [currentDate, setCurrentDate] = useState(new Date());

    const changeMonth = (direction) => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() + direction);
        setCurrentDate(newDate);
    };

    const generateCalendarDays = () => {
        const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
        const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

        const daysInMonth = [];
        const startDay = firstDayOfMonth.getDay(); // Day of the week (0=Sunday, 1=Monday, etc.)

        for (let i = 0; i < startDay; i++) {
            daysInMonth.push(null);
        }

        for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
            daysInMonth.push(new Date(currentDate.getFullYear(), currentDate.getMonth(), day));
        }

        return daysInMonth;
    };

    const days = generateCalendarDays();

    return (
        <div className="calendar-container">
            <div className="calendar-header">
                <button onClick={() => changeMonth(-1)} className="calendar-button">
                    &lt;
                </button>
                <h2>
                    {currentDate.toLocaleString("default", { month: "long" })} {currentDate.getFullYear()}
                </h2>
                <button onClick={() => changeMonth(1)} className="calendar-button">
                    &gt;
                </button>
            </div>
            <div className="calendar-grid">
                {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
                    <div key={day} className="calendar-day-header">
                        {day}
                    </div>
                ))}
                {days.map((date, index) => (
                    <div key={index} className={`calendar-cell ${date ? "" : "empty-cell"}`}>
                        {date && (
                            <>
                                <div className="calendar-date">{date.getDate()}</div>
                                <div className="calendar-events">
                                    {(schedule[date.toISOString().split("T")[0]] || []).map((event, idx) => (
                                        <div key={idx} className="calendar-event">
                                            {event}
                                        </div>
                                    ))}
                                </div>
                            </>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default CalendarPage;
