import React, {Component} from "react";
import SearchFood from "./SearchFood";
import {SERVER_URL} from "../config";
import update from "immutability-helper"
import FoodsPanel from "./FoodsPanel";

/**
 * food view class
 */
class FoodView extends Component {
    /**
     * constructor
     * @param props
     */
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

    /**
     * to update the state whenever the value of the property changes
     * @param nextProps
     * @param nextContext
     */
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

    /**
     * set update the tab numbers when tabs are switched
     * @param tabNumber
     */
    switchTabs(tabNumber) {
        this.setState({foodsPanelTab: tabNumber})
    }

    /**
     * handle foodItem search event
     * @param foodItem
     */
    // to handle search change
    handleSearchChange(foodItem) {
        this.setState({
            searchFoodItem: foodItem,
            foodsPanelTab: 0
        });

        this.getSearchResults(foodItem)
    }

    /**
     * get food item search results
     * @param searchFoodItem
     */
    // to get search results
    getSearchResults(searchFoodItem) {
        //to fix issue with multiple post request
        if (this.apiPostTimeout) {
            clearTimeout(this.apiPostTimeout);
        }
        this.apiPostTimeout = setTimeout(() => this.searchItems(searchFoodItem), 500);
    }

    searchItems(searchFoodItem) {
        fetch(`${SERVER_URL}/api/foods/search?query=${searchFoodItem}`)
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

    /**
     * get user created food items
     */
    getUserFoods() {
        const userId = 1;
        fetch(`${SERVER_URL}/api/foods/user?userId=${userId}`)
            .then((response) => response.json())
            .then(results => {
                this.setState({myFoods: results.success ? results.data[userId] : []})
            });
    }

    /**
     * delete user created food items
     * @param foodItemId
     */
    deleteUserFoodItem(foodItemId) {
        fetch(`${SERVER_URL}/api/foods/remove/${foodItemId}`, {
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
                    day={this.props.day}
                />
            </div>
        )
    }
}

export default FoodView;