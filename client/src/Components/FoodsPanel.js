import React, {Component} from "react";
import SearchResults from "./SearchResults";
import {Link} from "react-router-dom";
import MyFoods from "./MyFoods";


class FoodsPanel extends Component {

    constructor(props) {
        super(props);
        this.state = {
            editMode: false
        }
    }

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

    editMode() {
        this.setState(prevState => ({
            editMode: !prevState.editMode
        }));
    }

    getUserFoods() {
        this.props.getUserFoods();
    }

    deleteUserFoods(foodItemId) {
        this.props.deleteUserFoods(foodItemId);
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
                    <Link to={'/createfood'}>
                        <span
                            className={"FoodsPanel__tab--createFoodButton small-button" + (this.props.currentTab === 1 ? ' current' : '')}>Create Food</span>
                    </Link>
                    {editLink}
                </div>

                {
                    this.props.currentTab === 0 &&
                    <SearchResults searchResults={this.props.searchResults}/>
                }
                {
                    this.props.currentTab === 1 &&
                    <MyFoods
                        getFoods={this.getUserFoods.bind(this)}
                        foods={this.props.myFoods}
                        deleteUserFoodItem={this.deleteUserFoods.bind(this)}
                        editMode={this.state.editMode}
                    />
                }
            </div>
        );
    }
}

export default FoodsPanel;