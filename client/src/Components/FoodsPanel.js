import React, {Component} from "react";
import SearchResults from "./SearchResults";
import {Link} from "react-router-dom";
import MyFoods from "./MyFoods";
import carbsIcon from "../resources/bread-emoji.png";
import fatIcon from "../resources/bacon-strip-emoji.png";
import proteinIcon from "../resources/steak-emoji.png";
import calorieIcon from "../resources/calories.png";

/**
 * class responsible for creating panel
 */
class FoodsPanel extends Component {

    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            editMode: false
        }
    }

    /**
     * handle switch between panel events
     * @param e
     */
    handleSwitchTab(e) {
        let tab = e.target.attributes.class.value;
        if (!tab.includes(' ')) {
            tab = tab.substring(tab.indexOf('--') + 2);
            let tabNumber = ['searchResults', 'myFoods'].indexOf(tab);

            if (tabNumber === 1) {
                this.setState({editMode: false});
            }

            this.props.handleSwitchTab(tabNumber)
        }
    }

    /**
     * whether to display table headers or not
     * @returns {boolean}
     */
    shouldDisplayPanelHeader() {
        if ((this.props.currentTab === 0 && this.props.searchError)
            || (this.props.currentTab === 1 && !this.props.myFoods.length)) {
            return false;
        }
        return true;
    }

    /**
     * to set the edit state depending on user is editing or not
     */
    editMode() {
        this.setState(prevState => ({
            editMode: !prevState.editMode
        }));
    }

    /**
     * handle get user food items event
     */
    getUserFoods() {
        this.props.getUserFoods();
    }

    /**
     * handle delete user food items event
     * @param foodItemId
     */
    deleteUserFoods(foodItemId) {
        this.props.deleteUserFoodItem(foodItemId);
    }

    render() {
        let editLink;
        if (!this.state.editMode) {
            editLink = (
                <span className={"FoodsPanel__edit-foods" + ([1].includes(this.props.currentTab) ? ' current' : '')}
                      onClick={this.editMode.bind(this)}>
                    <i className="fas fa-pencil-alt"></i>&nbsp;edit
                </span>
            );
        } else {
            editLink = (
                <span
                    className={"FoodsPanel__edit-foods edit-mode" + ([1].includes(this.props.currentTab) ? ' current' : '')}>
                    <i className="fas fa-pencil-alt"></i>&nbsp;<b>Edit Mode</b>
                    <span className="FoodsPanel__edit-foods--done-link" onClick={this.editMode.bind(this)}>(done)</span>
                </span>
            );
        }

        return (
            <div className="FoodsPanel">
                <div className="FoodsPanel__tabs">
                    <span className={"FoodsPanel__tab--searchResults" + (this.props.currentTab === 0 ? ' current' : '')}
                          onClick={this.handleSwitchTab.bind(this)}>Search Results</span>
                    <span
                        className={"FoodsPanel__tab--myFoods" + (this.props.currentTab === 1 ? ' current' : '')}
                        onClick={this.handleSwitchTab.bind(this)}>My Foods</span>
                    <Link to={'/createfood?day=' + this.props.day}>
                        <span
                            className={"FoodsPanel__tab--createFoodButton small-button" + (this.props.currentTab === 1 ? ' current' : '')}>Create Food</span>
                    </Link>
                    {editLink}
                </div>

                {
                    this.shouldDisplayPanelHeader() &&
                    <div className="FoodsPanel__header">
                        <span className="FoodsPanel__header--macros">
                        <img src={carbsIcon} alt="carbohydrates" title="carbohydrates"/>
                        <img src={fatIcon} alt="fat" title="fat"/>
                        <img src={proteinIcon} alt="protein" title="protein"/>
                        </span>
                        <span className="Consumption__header--caloriesTotal">
                            <img src={calorieIcon} alt="calories" title="calories"/>
                        </span>
                    </div>
                }

                {
                    this.props.currentTab === 0 &&
                    <SearchResults searchResults={this.props.searchResults} day={this.props.day}
                                   error={this.props.searchError}/>
                }
                {
                    this.props.currentTab === 1 &&
                    <MyFoods
                        getFoods={this.getUserFoods.bind(this)}
                        foods={this.props.myFoods}
                        deleteUserFoodItem={this.deleteUserFoods.bind(this)}
                        editMode={this.state.editMode}
                        day={this.props.day}
                    />
                }
            </div>
        );
    }
}

export default FoodsPanel;