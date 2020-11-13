import React, {Component} from "react";
import {Link} from "react-router-dom";

class Header extends Component {
    render() {
        return (
            <header className="header">
                <div className="container header-container">
                    <nav>
                        <Link to={"/"}>Home</Link>
                        <Link to={"/goal"}>Goals</Link>
                    </nav>
                    <div className="clearfix"/>
                </div>
            </header>
        );
    }
}

export default Header;