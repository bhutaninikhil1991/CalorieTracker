import React, {Component} from "react";
import {SERVER_URL} from "../config";
import carbsIcon from "../resources/bread-emoji.png";
import fatIcon from "../resources/bacon-strip-emoji.png";
import proteinIcon from "../resources/steak-emoji.png";
import {Link} from "react-router-dom";


class CreateFoodView extends Component {
    constructor(props) {
        super(props);
    }

    handleSubmit(e) {
        e.preventDefault();
        const userId = 1;
        const servingSize = {
            servingAmount: (e.target[1].value.split(' '))[0],
            servingLabel: (e.target[1].value.split(' '))[1]
        }
        const newFoodItem = {
            name: e.target[0].value,
            carbohydrates: e.target[2].value.split(' ')[0],
            fat: e.target[3].value.split(' ')[0],
            protein: e.target[4].value.split(' ')[0],
            servingSizes: [servingSize]
        };
        const reqObj = {
            foodItem: newFoodItem
        };
        fetch(`${SERVER_URL}` + "/api/foods/" + userId, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(reqObj)
        }).then(response => {
            if (response.ok) {
                window.location = "/add";
            } else {
                alert("unable to add food item")
            }
        });
    }

    render() {
        return (
            <div className="CreateFoodView content-container">
                <h1 className="page-title">Create Food</h1>
                <form className="CreateFoodView__form" onSubmit={this.handleSubmit.bind(this)}>
                    <label htmlFor="name">Food Name</label>
                    <input type="text" name="name" id="name" placeholder="Salad"/>
                    <label htmlFor="servingsize"> Serving Size</label>
                    <input type="text" name="servingsize" id="servingsize" placeholder="1 bowl"/>
                    <span className="CreateFoodView__nutrition-separator">NUTRITION</span>
                    <div className="CreateFoodView__inputGroup">
                        <div className="CreateFoodView__input">
                            <span className="CreateFoodView__input--image">
                                <img src={carbsIcon} alt={"carbohydrates"}/>
                            </span>
                            <label htmlFor="carbohydrates"><strong>Carbohydrates</strong></label>
                            <input type="text" name="carbohydrates" id="carbohydrates" placeholder="100 gm"/>
                        </div>
                        <div className="CreateFoodView__input">
                            <span className="CreateFoodView__input--image">
                                <img src={fatIcon} alt={"fat"}/>
                            </span>
                            <label htmlFor="fat"><strong>Fat</strong></label>
                            <input type="text" name="fat" id="fat" placeholder="80 gm"/>
                        </div>
                        <div className="CreateFoodView__input">
                            <span className="CreateFoodView__input--image">
                                <img src={proteinIcon} alt={"protein"}/>
                            </span>
                            <label htmlFor="protein"><strong>Protein</strong></label>
                            <input type="text" name="protein" id="protein" placeholder="50 gm"/>
                        </div>
                        <button className="CreateFoodView__submit-button" type="submit">Create Food</button>
                        <Link to="/add" className="cancel-link">Cancel</Link>
                    </div>
                </form>
            </div>
        );
    }
}

export default CreateFoodView;