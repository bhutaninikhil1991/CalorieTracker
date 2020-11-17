import React, {Component} from "react";
import {Link} from "react-router-dom";

class Header extends Component {
    logUserOut() {
        localStorage.removeItem("token");
        window.location = "/login";
    }

    render() {
        return (
            <header className="header">
                <div className="container header-container">
                    <nav>
                        <Link to={"/"}>Home</Link>
                        <Link to={"/goal"}>Goals</Link>
                        <Link onClick={this.logUserOut.bind(this)}>Log out</Link>
                    </nav>
                    <div className="clearfix"/>
                </div>
            </header>
        );
    }
}

export default Header;