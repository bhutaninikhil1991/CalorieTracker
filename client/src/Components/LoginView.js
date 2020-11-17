import React, {Component} from 'react'
import {SERVER_URL} from "../config";

class LoginView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            registering: false,
            email: '',
            password: '',
            errorMessage: '',
            finishedRegistering: false
        }
    }

    handleSubmit(e) {
        e.preventDefault();
        const reqObj = {
            username: this.state.email,
            password: this.state.password
        }

        if (this.state.registering) {
            fetch(`${SERVER_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reqObj)
            }).then((response) => {
                if (response.ok) {
                    return response.json().then(result => {
                        if (result.success === true) {
                            this.setState({
                                finishedRegistering: true,
                                errorMessage: ''
                            });
                        } else {
                            this.setState({
                                errorMessage: result.errorMessage
                            });
                        }
                    });
                }
            });
        } else {
            fetch(`${SERVER_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reqObj)
            }).then((response) => {
                if (response.ok) {
                    return response.json()
                        .then(result => {
                            localStorage.setItem("token", result.access_token);
                            this.setState({
                                errorMessage: ''
                            });
                            window.location = "/";
                        });
                } else {
                    return response.json().then(result => {
                        this.setState({
                            errorMessage: result.message
                        });
                    });
                }
            });
        }
    }

    handleEmailChange(e) {
        this.setState({
            email: e.target.value
        });
    }

    handlePasswordChange(e) {
        this.setState({
            password: e.target.value
        });
    }

    switchActionType() {
        this.setState(prevState => ({
            registering: !prevState.registering,
            errorMessage: ''
        }));
    }

    render() {
        let submitButton;

        submitButton = (
            <button type="submit" className='loginButton'>
                {this.state.registering ? 'Register' : 'Login'}
            </button>
        );

        let switchText;

        if (this.state.registering) {
            if (this.state.finishedRegistering) {
                switchText = (
                    <span className="LoginView_form--switch">You have been successfully registered. Please <span
                        onClick={this.switchActionType.bind(this)}>login</span> to access your account.</span>
                );
            } else {
                switchText = (
                    <span className="LoginView_form--switch">
                  You are registering a new account. If you already have one, you can <span
                        onClick={this.switchActionType.bind(this)}>login</span>.
              </span>
                );
            }
        } else {
            switchText = (
                <span className="LoginView_form--switch">
                    Don't have an account? <span onClick={this.switchActionType.bind(this)}>Click here</span> to register.
                </span>
            );
        }

        return (
            <div className="LoginView content-container">
                <div className="LoginView_form">
                    <h2>{this.state.registering ? 'Register a new account' : 'Log In'}</h2>
                    <form onSubmit={this.handleSubmit.bind(this)}>
                        <div className="form-group">
                            <label htmlFor="email">Email Address</label>
                            <input type="email" id="email" placeholder="Email Address" value={this.state.email}
                                   onChange={this.handleEmailChange.bind(this)}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input type="password" id="password" placeholder="Password" value={this.state.password}
                                   onChange={this.handlePasswordChange.bind(this)}/>
                        </div>
                        {submitButton}
                    </form>
                </div>

                {this.state.errorMessage &&
                <span className="LoginView__notify LoginView__error">{this.state.errorMessage}</span>}

                {switchText}
            </div>
        );
    }

}

export default LoginView;