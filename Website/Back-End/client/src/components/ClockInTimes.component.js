import React, { Component } from 'react';
import API from '../utils/Api';
import ClockInService from '../services/user.service';

export default class ClockInTimes extends Component {
	// state = {
	// 	clockInTimes: [],
	// 	name: '',
	// 	clockedInTime: ''
	// };

    constructor(props) {
        super(props);
    
        this.state = {
        clockInTimes: [],
		name: '',
		clockedInTime: ''
        };
      }

	// componentDidMount() {
	// 	this.loadClockInTimes();
	// }

	// loadClockInTimes = () => {
	// 	API.getClockInTimes()
	// 		.then(res => this.setState({ clockInTimes: res.data, name: '', clockedInTime: '' }))
	// 		.catch(err => console.log(err));
	// };

	// deleteClockInTime = id => {
	// 	API.deleteClockInTime(id)
	// 		.then(res => this.loadClockInTimes())
	// 		.catch(err => console.log(err));
	// };

	// handleInputChange = event => {
	// 	const { name, value } = event.target;
	// 	this.setState({
	// 		[name]: value
	// 	});
	// };

    componentDidMount() {
        ClockInService.getClockInTimes().then(
          response => {
            this.setState({
              name: response.data
            });
          },
          error => {
            this.setState({
              name:
                (error.response && error.response.data) ||
                error.message ||
                error.toString()
            });
          }
        );
      }


render() {
    return (
      <div className="container">
        <header className="jumbotron">
            <h2>ClockInTimes</h2>
          <h3>{this.state.clockInTimes}</h3>
        </header>
      </div>
    );
  }
}
