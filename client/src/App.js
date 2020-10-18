import React from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
} from "react-router-dom";
import './App.css';
import CreateFoodItem from "./Components/CreateFoodItem";
import FoodViewContainer from "./Components/FoodViewContainer";

function App() {
    return (
        <Router>
            <div className="App">
                <Switch>
                    <Route path="/add">
                        <FoodViewContainer/>
                    </Route>
                    <Route path="/createfood">
                        <CreateFoodItem/>
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}

export default App;
