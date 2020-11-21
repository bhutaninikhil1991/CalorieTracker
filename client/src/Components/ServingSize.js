import React, {Component} from "react";

/**
 * serving size class
 */
class ServingSize extends Component {

    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            quantityValue: this.props.selectedServing.quantity
        }
    }

    /**
     * handle serving quantity change event
     * @param e
     */
    handleQuantityChange(e) {
        this.setState({
            quantityValue: e.target.value
        });
        this.props.handleQuantityChange(e.target.value);
    }

    /**
     * handle serving size change event
     * @param e
     */
    handleSizeChange(e) {
        this.props.handleSizeChange(parseInt(e.target.value, 10))
    }

    /**
     * handle input click event
     * @param e
     */
    handleInputClick(e) {
        e.stopPropagation();
    }

    /**
     * handle removing serving size event
     */
    handleItemRemove() {
        this.props.handleItemRemove();
    }

    /**
     * handle adding serving size event
     * @param e
     */
    handleAddClick(e) {
        e.stopPropagation();
        this.props.handleAddClick(this.props.itemId);
    }

    render() {
        let servingUnitOptions;
        if (this.props.servingSizes.length > 0) {
            servingUnitOptions = this.props.servingSizes.map(servingSize => {
                return (<option key={servingSize.id} value={servingSize.id}>{servingSize.servingLabel}</option>);
            });
        }

        return (
            <div className="ServingSelect animated fadeInDown" id="ServingSelect">
                <input className="servingAmt" type="number" min="1" name="servingAmt" id="servingAmt" placeholder="1"
                       value={this.state.quantityValue} onChange={this.handleQuantityChange.bind(this)}
                       onClick={this.handleInputClick.bind(this)}/>
                <select className="servingAmt" name="servingUnit" id="servingUnit"
                        defaultValue={this.props.selectedServing.servingSize.id}
                        onChange={this.handleSizeChange.bind(this)} onClick={this.handleInputClick.bind(this)}>
                    {servingUnitOptions}
                </select>
                {
                    this.props.showAddRemoveButtons &&
                    <button className="ServingSelect__add-button" onClick={this.handleAddClick.bind(this)}>Add</button>
                }
                {
                    this.props.showAddRemoveButtons &&
                    <button className="ServingSelect__remove-button" onClick={this.handleItemRemove.bind(this)}>Remove
                    </button>
                }
            </div>
        );
    };
}

export default ServingSize;