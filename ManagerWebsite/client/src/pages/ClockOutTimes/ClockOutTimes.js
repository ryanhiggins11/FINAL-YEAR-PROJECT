import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class ClockOutTimes extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		clockOutTimes: [],
		name: '',
		clockedOutTime: ''
	};

	//loads clock out times
	componentDidMount() {
		this.loadClockOutTimes();
	}

	//gets clock out times
	loadClockOutTimes = () => {
		API.getClockOutTimes()
			.then(res => this.setState({ clockOutTimes: res.data, name: '', clockedOutTime: '' }))
			.catch(err => console.log(err));
	};

	//deletes clock out times
	deleteClockOutTime = id => {
		API.deleteClockOutTime(id)
			.then(res => this.loadClockOutTimes())
			.catch(err => console.log(err));
	};


	handleInputChange = event => {
		const { name, value } = event.target;
		this.setState({
			[name]: value
		});
	};

	 /*
	 *renders image
	 *displays clock out times
	 *gets clock out times
	 *delete button if you want to remove a clock out time
	 *four buttons to bring to clock in times, break times, employee sick list and employee details page
	 */
	render() {
		return (
			<Container fluid>
				<Row>
					{}
					<Col size="md-6 sm-12">	
					
					<div classname="banner-out">
						<header>
                			<img width="30%" src = "./images/clockOut.png" alt="Clock out Banner"/>
            			</header>
					</div>

					<div classname="clock-out"><h1>Employees Clock-Out Times:</h1></div>
						{this.state.clockOutTimes.length ? (
							<List>
								
								{this.state.clockOutTimes.map(clockOutTimes => (
									<ListItem key={clockOutTimes._id}>
										<Link to={'/clockOutTimes/' + clockOutTimes._id}>
											<table>
												<td>{clockOutTimes.name} Clocked out at {clockOutTimes.clockedOutTime}</td>
												<td><DeleteBtn onClick={() => this.deleteClockOutTime(clockOutTimes._id)} /></td>
											</table>
										</Link>
									</ListItem>
								))}
							</List>
						) : (
							<h3>No Results Times To Display</h3>
						)}
						<a href="clockInTimes"><button>Clock-In Times</button></a>
						<a href="breakTimes"><button>Break Times</button></a>
						<a href="isSick"><button>Employee Sick List</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default ClockOutTimes;
