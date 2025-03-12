import React from "react";
import "./ProfilePage.css";

function ProfilePage() {
    const user = {
        name: "Piotr Barszczyk",
        email: "Barszczyk@gmail.com",
        password: "LubieWDupe69",
        profilePicture: "https://via.placeholder.com/150",
    };

    return (
        <div className="profile-container">
            <div className="profile-card">
                <div className="profile-header">
                    <img
                        src={user.profilePicture}
                        alt="Profile"
                        className="profile-picture"
                    />
                    <h2 className="profile-name">{user.name}</h2>
                </div>
                <div className="profile-details">
                    <div className="profile-row">
                        <span className="profile-label">Email:</span>
                        <span className="profile-value">{user.email}</span>
                    </div>
                    <div className="profile-row">
                        <span className="profile-label">Password:</span>
                        <span className="profile-value">{user.password}</span>
                    </div>
                </div>
                <div className="profile-actions">
                    <button className="profile-button">Edit Profile</button>
                    <button className="profile-button">Change Password</button>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;
