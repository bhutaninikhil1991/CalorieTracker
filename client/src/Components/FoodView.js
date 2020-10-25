import React, {Component} from "react";
import SearchFood from "./SearchFood";
import {SERVER_URL} from "../config";
import update from "immutability-helper"
import FoodsPanel from "./FoodsPanel";

class FoodView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            searchFoodItem: '',
            searchResults: [],
            searchError: false,
            foodsPanelTab: this.props.tab ? this.props.tab : 0,
            myFoods: [],
            loading: true
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.tab) {
            if (nextProps.tab !== this.state.foodsPanelTab) {
                this.setState({
                    foodsPanelTab: nextProps.tab
                }, () => {
                    window.scrollTo(0, 0);
                });
            }
        }
    }

    switchTabs(tabNumber) {
        this.setState({foodsPanelTab: tabNumber})
    }

    // to handle search change
    handleSearchChange(foodItem) {
        this.setState({
            searchFoodItem: foodItem,
            foodsPanelTab: 0
        });

        this.getSearchResults(foodItem)
    }

    // to get search results
    getSearchResults(searchFoodItem) {
        fetch(`${SERVER_URL}` + "/api/foods/search?query=" + searchFoodItem)
            .then((response) => {
                if (response.ok) {
                    response.json()
                        .then(results => {
                            this.setState({
                                searchResults: results.success ? results.data[searchFoodItem] : [],
                                searchError: false
                            });
                        });
                } else {
                    this.setState({
                        searchError: true
                    });
                }
            });
    }

    getUserFoods() {
        const userId = 1;
        fetch(`${SERVER_URL}` + "/api/foods/user?userId=" + userId)
            .then((response) => response.json())
            .then(results => {
                this.setState({myFoods: results.success ? results.data[userId] : []})
            });
    }

    deleteUserFoodItem(foodItemId) {
        fetch(`${SERVER_URL}` + "/api/foods/remove/" + foodItemId, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
                let foodItem = this.state.myFoods.find(food => food.id === foodItemId);
                let foodItemIndex = this.state.myFoods.indexOf(foodItem);
                let newState = update(this.state, {
                    myFoods: {$splice: [[foodItemIndex, 1]]}
                });
                this.setState(newState);
            } else {
                alert("Food Item cannot be deleted");
            }
        });
    }

    render() {
        return (
            <div className="FoodView content-container">
                <SearchFood searchFoodItem={this.state.searchFoodItem}
                            handleSearchChange={this.handleSearchChange.bind(this)}/>
                <FoodsPanel
                    currentTab={this.state.foodsPanelTab}
                    handleSwitchTab={this.switchTabs.bind(this)}
                    searchResults={this.state.searchResults}
                    searchError={this.state.searchError}
                    getUserFoods={this.getUserFoods.bind(this)}
                    myFoods={this.state.myFoods}
                    deleteUserFoodItem={this.deleteUserFoodItem.bind(this)}
                    loading={this.state.loading}
                />
            </div>
        )
    }
}

export default FoodView;