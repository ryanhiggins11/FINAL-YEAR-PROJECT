import React, { Component } from 'react';
import './Nav.css';

class Navbar extends Component {
    render() {
        return(
            <header>
                 <h1> Welcome to Biztech</h1>
                <img src = "./images/homepage.png" alt="BANNER IMAGE"/>
                <h2> This area is for managment only </h2>
            </header>

        )
    }

    
}
export default Navbar;