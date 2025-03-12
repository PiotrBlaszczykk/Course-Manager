import React from 'react';
import './Footer.css';

function Footer() {
    return (
        <footer className="sticky-footer">
            <div className="footer-content">
                <div className="footer-left">
                    <h3 className="footer-title">© Course Manager</h3>
                    <p className="footer-subtitle">Technologie Obiektowe – Romper Megamocny</p>
                </div>
                <p className="footer-names">
                    Piotr Błaszczyk, Piotr Branewski, Krzysztof Swędzioł, Michał Plaza
                </p>
            </div>
        </footer>

    );
}

export default Footer;
