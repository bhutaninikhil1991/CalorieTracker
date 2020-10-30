import React, {Component} from "react";

/**
 * class responsible for deleting food item
 */
class ItemDeleteButton extends Component {

    /**
     * handle delete food item event
     * @param e
     */
    deleteItem(e) {
        e.stopPropagation();
        this.props.deleteItem();
    }

    render() {
        return (
            <div className="ItemDeleteButton animated fadeInDown" onClick={this.deleteItem.bind(this)}>
                <span>Delete this item</span>
            </div>
        );
    };
}

export default ItemDeleteButton;