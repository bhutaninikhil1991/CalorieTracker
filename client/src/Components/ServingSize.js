import React, {Component} from "react";

class ServingSize extends Component {

    constructor(props) {
        super(props);
        this.state = {
            quantityValue: this.props.selectedServing.quantity
        }
    }

    handleQuantityChange(e) {
        this.setState({
            quantityValue: e.target.value
        });
        this.props.handleQuantityChange(e.target.value);
    }

    handleSizeChange(e) {
        this.props.handleSizeChange(parseInt(e.target.value, 10))
    }

    handleItemRemove() {
        this.props.handleItemRemove();
    }

    handleInputClick(e) {
        e.stopPropagation();
    }

    getServingLabel(servingSize) {
        return typeof servingSize.label === 'string' ? servingSize.label : servingSize.label.labelValue;
    }

    render() {
        let servingUnitOptions;
        if (this.props.servingSizes) {
            servingUnitOptions = this.props.servingSizes.map(servingSize => {
                return (<option key={servingSize.id} value={servingSize.id}>{this.getServingLabel.bind(this)}</option>);
            });
        }

        return (
            <div id="ServingSelect">
                <input className="servingAmt" type="text" name="servingAmt" id="servingAmt" placeholder="1"
                       value={this.state.quantityValue} onChange={this.handleQuantityChange.bind(this)}
                       onClick={this.handleInputClick.bind(this)}/>
                <select className="servingAmt" name="servingUnit" id="servingUnit"
                        defaultValue={this.props.selectedServing.servingSize.id}
                        onChange={this.handleSizeChange.bind(this)} onClick={this.handleInputClick.bind(this)}>
                    {servingUnitOptions}
                </select>
            </div>
        );
    };
}

export default ServingSize;