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

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        let location = document.location.href;
        let onLoginPage = location.includes("login");

        return (
            <div className="App">
                <Router>
                    {!onLoginPage && <Header/>}
                    <Switch>
                        <Route exact path="/login" component={LoginView}/>
                        <Route exact path="/" component={DayView}/>
                        <Route exact path="/add" component={FoodViewContainer}/>
                        <Route exact path="/createfood" component={CreateFoodView}/>
                        <Route exact path="/goal" component={MyGoals}/>
                        <Route exact path="/stats" component={StatisticsView}/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
