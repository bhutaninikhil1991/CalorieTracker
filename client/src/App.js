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
                    <Route path="/">
                        <DayView/>
                    </Route>
                    <Route path="/add">
                        <FoodViewContainer/>
                    </Route>
                    <Route path="/createfood">
                        <CreateFoodView/>
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}

export default App;
