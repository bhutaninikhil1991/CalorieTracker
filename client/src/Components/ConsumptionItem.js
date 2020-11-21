import React, {Component} from "react";
import ServingSize from "./ServingSize";
import editIcon from "../resources/edit-icon.png";
import checkIcon from "../resources/checkmark-icon.png";
import update from "immutability-helper";

/**
 * consumption Item class
 */
class ConsumptionItem extends Component {
    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            editMode: false,
            selectedServing: {
                servingSize: this.props.selectedServing,
                quantity: this.props.servingQuantity
            }
        }

    }

    /**
     * to update the state whenever the value of the property changes
     * @param nextProps
     * @param nextContext
     */
    componentWillReceiveProps(nextProps, nextContext) {
        let newState = update(this.state, {
            selectedServing: {
                servingSize: {$set: nextProps.selectedServing},
                quantity: {$set: nextProps.servingQuantity}
            }
        });
        this.setState(newState);
    }

    /**
     * handle consumption edit event to edit the consumption Item
     */
    handleEditClick() {
        this.setState(prevState => ({
            editMode: !prevState.editMode
        }));
    }

    /**
     * handle consumption save event after the changes have been made to the consumption Item
     */
    handleSaveClick() {
        this.setState(prevState => ({
            editMode: !prevState.editMode
        }));
        this.props.handleNewServingSave();
    }

    /**
     * handle serving size change event
     * @param servingSizeId
     */
    handleSizeChange(servingSizeId) {
        this.props.handleSizeChange(servingSizeId, this.props.consumptionId);
    }

    /**
     * handle quantity change event
     * @param quantity
     */
    handleQuantityChange(quantity) {
        this.props.handleQuantityChange(quantity, this.props.consumptionId);
    }

    /**
     * handle delete consumption record event
     */
    handleItemRemove() {
        this.props.handleItemRemove(this.props.consumptionId);
    }

    render() {
        let icon;
        if (this.state.editMode) {
            icon = (
                <img src={checkIcon} alt="save" className="ConsumptionItem__save"
                     onClick={this.handleSaveClick.bind(this)}/>);
        } else {
            icon = (
                <img src={editIcon} alt="edit" className="ConsumptionItem__edit"
                     onClick={this.handleEditClick.bind(this)}/>);
        }
        return (
            <div className={"ConsumptionItem" + (this.state.editMode ? " editing" : "")}>
                {icon}
                <span className="ConsumptionItem__food">
                    <span className="ConsumptionItem__food--info">
                        <span className="ConsumptionItem__food--name">{this.props.name}</span>
                        <span className="ConsumptionItem__food--quantity">
                            {this.props.servingQuantity}{this.props.selectedServing.servingLabel}
                        </span>
                    </span>
                </span>
                <span className="ConsumptionItem__macros">
                    <span className="ConsumptionItem__macros--carbs">{this.props.nutritionValues.carbs}</span>
                    <span className="ConsumptionItem__macros--fat">{this.props.nutritionValues.fat}</span>
                    <span className="ConsumptionItem__macros--protein">{this.props.nutritionValues.protein}</span>
                </span>
                <span className="ConsumptionItem__calories">{this.props.nutritionValues.calories}</span>
                <div className="clearfix"/>
                {this.state.editMode &&
                <ServingSize
                    selectedServing={this.state.selectedServing}
                    servingSizes={this.props.servingSizes}
                    handleQuantityChange={this.handleQuantityChange.bind(this)}
                    handleSizeChange={this.handleSizeChange.bind(this)}
                    handleItemRemove={this.handleItemRemove.bind(this)}
                    removingItem={this.props.removingItem}
                    showAddRemoveButtons
                />
                }
            </div>
        );
    }


}

export default ConsumptionItem;