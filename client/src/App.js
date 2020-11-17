import React, {Component} from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
} from "react-router-dom";
import './App.css';
import CreateFoodView from "./Components/CreateFoodView";
import FoodViewContainer from "./Components/FoodViewContainer";
import DayView from "./Components/DayView";
import LoginView from "./Components/LoginView";
import MyGoals from "./Components/MyGoals";
import Header from "./Components/Header";
import StatisticsView from "./Components/StatisticsView";
import PrivateRoute from "./Components/PrivateRoute";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isUserLoggedIn: localStorage.getItem("token") !== null
        }
    }

    render() {
        return (
            <div className="App">
                <Router>
                    {this.state.isUserLoggedIn && <Header/>}
                    <Switch>
                        <Route exact path="/login" component={LoginView}/>
                        <PrivateRoute exact isUserAuthenticated={this.state.isUserLoggedIn} path="/"
                                      component={DayView}/>
                        <PrivateRoute exact isUserAuthenticated={this.state.isUserLoggedIn} path="/add"
                                      component={FoodViewContainer}/>
                        <PrivateRoute exact isUserAuthenticated={this.state.isUserLoggedIn} path="/createfood"
                                      component={CreateFoodView}/>
                        <PrivateRoute exact isUserAuthenticated={this.state.isUserLoggedIn} path="/goal"
                                      component={MyGoals}/>
                        <PrivateRoute exact isUserAuthenticated={this.state.isUserLoggedIn} path="/stats"
                                      component={StatisticsView}/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
