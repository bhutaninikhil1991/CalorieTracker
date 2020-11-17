import React, {Component} from "react";
import {SERVER_URL} from "../config";
import {getUserId} from "./Helpers";

class MyGoals extends Component {
    constructor(props) {
        super(props);
        this.state = {
            goals: {
                calories: '',
                carbohydrates: '',
                fat: '',
                protein: ''
            },
            saving: false,
            finishedSaving: false
        }
    }

    componentDidMount() {
        const userId = getUserId();
        this.getGoals(userId);
    }

    getGoals(userId) {
        fetch(`${SERVER_URL}/api/goals?userId=${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        }).then((response) => {
            if (response.ok) {
                return response.json()
                    .then(results => {
                        let items = results.success && results.data !== undefined ? results.data[userId] : [];
                        if (items.length <= 0)
                            return;
                        let goals = {};
                        items.forEach((item) => {
                            goals[item.goalCategory.toLowerCase()] = item.goalValue;
                        });
                        this.setState({goals: goals});
                    });
            }
        });
    }

    handleSubmit(e) {
        e.preventDefault();
        const userId = getUserId();
        this.setState({saving: true})
        let goalsObj = {
            calories: parseInt(e.target[0].value),
            carbohydrates: parseInt(e.target[1].value),
            fat: parseInt(e.target[2].value),
            proteins: parseInt(e.target[3].value)
        }
        const reqObj = {
            goals: goalsObj
        }

        fetch(`${SERVER_URL}/api/goals/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(reqObj)
        }).then(response => {
            if (response.ok) {
                this.setState({
                    saving: false,
                    finishedSaving: true
                });
            } else {
                alert("There is a problem updating record");
                this.setState({
                    saving: false
                });
            }
        });
    }

    handleInputChange(e) {
        if (this.state.finishedSaving) {
            this.setState({finishedSaving: false})
        }

        let fieldChanged = e.target.name;
        this.setState({
            goals: {
                [fieldChanged]: e.target.value
            }
        })
    }

    render() {
        let saveButton;
        if (!this.state.saving) {
            saveButton = (<button type="submit" className="MyGoals__submit-button">Save Goals</button>);
        } else {
            saveButton = (<button type="submit" className="MyGoals__submit-button saving">Saving..</button>);
        }

        return (
            <div className="MyAccountView content-container">
                <h1 className="page-title">My Daily Goals</h1>
                <div className="MyGoals">
                    <form className="MyGoals__form" onSubmit={this.handleSubmit.bind(this)}>
                    <span className="MyGoals__form--input">
                        <label htmlFor="calories">Calories</label>
                        <input type="text" name="calories" placeholder="1000" value={this.state.goals.calories}
                               onChange={this.handleInputChange.bind(this)}/>
                    </span>
                        <span className="MyGoals__form--input">
                        <label htmlFor="carbohydrates">Carbohydrates (g)</label>
                        <input type="text" name="carbohydrates" placeholder="250 g"
                               value={this.state.goals.carbohydrates}
                               onChange={this.handleInputChange.bind(this)}/>
                    </span>
                        <span className="MyGoals__form--input">
                        <label htmlFor="fat">Fat (g)</label>
                        <input type="text" name="fat" placeholder="80 g" value={this.state.goals.fat}
                               onChange={this.handleInputChange.bind(this)}/>
                    </span>
                        <span className="MyGoals__form--input">
                        <label htmlFor="protein">Protein (g)</label>
                        <input type="text" name="protein" placeholder="50 g" value={this.state.goals.protein}
                               onChange={this.handleInputChange.bind(this)}/>
                    </span>
                        <div className="clearfix"/>
                        {saveButton}
                        {this.state.finishedSaving &&
                        <span className="MyGoals__form--save-success animated fadeIn" id="save-success-message">Your goals have been updated</span>}
                    </form>
                </div>
            </div>
        );
    }
}

export default MyGoals;