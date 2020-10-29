import React from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
} from "react-router-dom";
import './App.css';
import CreateFoodView from "./Components/CreateFoodView";
import FoodViewContainer from "./Components/FoodViewContainer";
import DayView from "./Components/DayView";

function App() {
    return (
        <Router>
            <div className="App">
                <Switch>
                    <Route exact path="/" component={DayView}/>
                    <Route exact path="/add" component={FoodViewContainer}/>
                    <Route exact path="/createfood" component={CreateFoodView}/>
                </Switch>
            </div>
        </Router>
    );
}

export default App;
