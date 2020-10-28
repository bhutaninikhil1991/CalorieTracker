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

    handleInputClick(e) {
        e.stopPropagation();
    }

    handleItemRemove() {
        this.props.handleItemRemove();
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