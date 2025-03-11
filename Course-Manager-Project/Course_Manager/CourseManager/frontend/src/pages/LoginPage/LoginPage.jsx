import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import './LoginPage.css';

const LoginPage = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const navigate = useNavigate();
    const { login } = useContext(AuthContext);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`/api/users/email/${formData.email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                const user = await response.json();

                if (user.password === formData.password) {
                    login(user);
                    alert('Logged in successfully!');
                    console.log('Logged in user:', user);
                    navigate('/');
                } else {
                    alert('Invalid email or password.');
                }
            } else {
                alert('Invalid email or password.');
            }
        } catch (error) {
            console.error('Error during login:', error);
            alert('An error occurred. Please try again.');
        }
    };

    return (
        <div className="login-subsite">
            <div className="login-container">
                <h1>Login</h1>
                <form className="form" onSubmit={handleSubmit}>
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
                    <button type="submit" className="btn-submit">
                        Login
                    </button>
                </form>
                <p>
                    Don't have an account?{' '}
                    <button
                        className="btn-toggle"
                        onClick={() => navigate('/register')}
                    >
                        Register here
                    </button>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
