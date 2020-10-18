import React, {Component} from "react";
import {SERVER_URL} from "../config";

class CreateFoodItem extends Component {
    constructor(props) {
        super(props);
    }

    handleSubmit(e) {
        e.preventDefault();
        const userId = 1;
        const servingSize = {
            quantity: (e.target[1].value.split(' '))[0],
            label: (e.target[1].value.split(' '))[1]
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

            } else {
                alert("unable to add food item")
            }
        });
    }

    render() {
        return (
            <div>
                <h1>Create Food</h1>
                <form onSubmit={this.handleSubmit.bind(this)}>
                    <label htmlFor="name">Food Name</label>
                    <input type="text" name="name" id="name" placeholder="Salad"/>
                    <label htmlFor="servingsize"> Serving Size</label>
                    <input type="text" name="servingsize" id="servingsize" placeholder="1 bowl"/>
                    <label htmlFor="carbohydrates">Carbohydrates</label>
                    <input type="text" name="carbohydrates" id="carbohydrates" placeholder="100 gm"/>
                    <label htmlFor="fat">Fat</label>
                    <input type="text" name="fat" id="fat" placeholder="80 gm"/>
                    <label htmlFor="protein">Protein</label>
                    <input type="text" name="protein" id="protein" placeholder="50 gm"/>
                    <button className="CreateFoodItem__submit_button" type="submit">Create Food</button>
                </form>
            </div>
        );
    }
}

export default CreateFoodItem;