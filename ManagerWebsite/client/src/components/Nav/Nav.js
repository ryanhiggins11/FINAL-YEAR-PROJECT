import React, { Component } from 'react';
import './Nav.css';

/*
*Navbar
*Image displayed
*/

class Navbar extends Component {
    render() {
        return(
            <header>
                <img src = "./images/homepage.png" alt="Main IMAGE"/>
                <h1> Welcome to Biztech</h1>
                <h2> This area is for management only </h2>
            </header>

        )
    }

    
}
export default Navbar;