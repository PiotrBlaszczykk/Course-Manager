import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import './RegisterPage.css';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        firstname: '',
        surname: '',
        age: '',
        email: '',
        password: '',
        isOrganizer: false,
    });
    const navigate = useNavigate();
    const { login } = useContext(AuthContext);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === 'checkbox' ? checked : value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const result = await response.json();
                alert('User registered successfully!');
                console.log('Registered user:', result);
                navigate('/login');
                login(result);
                navigate('/');
            } else {
                const errorMessage = await response.text();
                alert(`Error: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error during registration:', error);
            alert('An error occurred. Please try again.');
        }
    };

    return (
        <div className="login-subsite">
            <div className="login-container">
                <h1>Register</h1>
                <form className="form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="firstname">First Name</label>
                        <input
                            type="text"
                            id="firstname"
                            name="firstname"
                            placeholder="Enter your first name"
                            value={formData.firstname}
                            onChange={handleChange}
                            maxLength={50}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="surname">Last Name</label>
                        <input
                            type="text"
                            id="surname"
                            name="surname"
                            placeholder="Enter your last name"
                            value={formData.surname}
                            onChange={handleChange}
                            maxLength={50}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="age">Age</label>
                        <input
                            type="number"
                            id="age"
                            name="age"
                            placeholder="Enter your age"
                            value={formData.age}
                            onChange={handleChange}
                            min={0}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            placeholder="Enter your email"
                            value={formData.email}
                            onChange={handleChange}
                            maxLength={100}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Enter your password"
                            value={formData.password}
                            onChange={handleChange}
                            minLength={8}
                            maxLength={255}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="isOrganizer">
                            <input
                                type="checkbox"
                                id="isOrganizer"
                                name="isOrganizer"
                                checked={formData.isOrganizer}
                                onChange={handleChange}
                            />
                            {' '}Organizer
                        </label>
                    </div>
                    <button type="submit" className="btn-submit">
                        Register
                    </button>
                </form>
                <p>
                    Already have an account?{' '}
                    <button
                        className="btn-toggle"
                        onClick={() => navigate('/login')}
                    >
                        Login here
                    </button>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;
