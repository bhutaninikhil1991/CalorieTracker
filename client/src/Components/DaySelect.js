import React, {Component} from "react";
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';

class DaySelect extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pickerVisible: false
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.state.pickerVisible) {
            document.body.addEventListener('click', this.handleClick.bind(this), true);
        }
    }

    handleClick(e) {
        if (document.querySelector('.DayPicker')) {
            if (!document.querySelector('.DayPicker').contains(e.target)) {
                document.body.removeEventListener('click', this.handleClick.bind(this), true);
                this.setState({pickerVisible: false});
            }
        }
    }

    //change to day before event
    changeToDayBefore() {
        let newDay = this.props.selectedDay;
        newDay.setDate(newDay.getDate() - 1);
        //need to call change event
        this.handleDayChange(newDay);
    }

    //handle day change
    handleDayChange(day) {
        if (day > this.props.todaysDate)
            return;
        this.props.changeSelectedDay(day);
    }

    //change to day after event
    changeToDayAfter() {
        let newDay = this.props.selectedDay;
        newDay.setDate(newDay.getDate() + 1);
        //need to call change event
        this.handleDayChange(newDay);
    }

    //to check if selected day is equal to current day
    selectedDayIsToday() {
        return this.props.selectedDay.toISOString().split('T')[0] ===
            this.props.todaysDate.toISOString().split('T')[0];
    }

    render() {
        let dayText = this.props.selectedDay.toISOString().split('T')[0];
        return (
            <div className="DaySelect">
                <div className="DaySelect__arrow left" onClick={this.changeToDayBefore.bind(this)}/>
                <span className="DaySelect__choose" onClick={() => this.setState({pickerVisible: true})}>
                    <div className="DaySelect__calendar"/>
                    <span className="DaySelect__dayText">{dayText}</span>
                </span>

                {!this.selectedDayIsToday() &&
                <div className="DaySelect__arrow right" onClick={this.changeToDayAfter.bind(this)}/>}

                {this.state.pickerVisible && <DayPicker
                    onDayClick={this.handleDayChange.bind(this)}
                    disabledDays={{after: this.props.todaysDate}}
                    selectedDays={this.props.selectedDay}
                    month={new Date(this.props.selectedDay.getFullYear(), this.props.selectedDay.getMonth())}
                />}
                <div className="clearfix"/>
            </div>
        );
    }
}

export default DaySelect;