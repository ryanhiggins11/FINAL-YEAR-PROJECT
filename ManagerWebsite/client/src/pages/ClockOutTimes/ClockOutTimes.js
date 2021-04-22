import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class ClockOutTimes extends React.Component {
	state = {
		clockOutTimes: [],
		name: '',
		clockedOutTime: ''
	};

	componentDidMount() {
		this.loadClockOutTimes();
	}

	loadClockOutTimes = () => {
		API.getClockOutTimes()
			.then(res => this.setState({ clockOutTimes: res.data, name: '', clockedOutTime: '' }))
			.catch(err => console.log(err));
	};

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

	render() {
		return (
			<Container fluid>
				<Row>
					{}
					<Col size="md-6 sm-12">	
					
					<div classname="banner-out">
						<header>
                			<img width="30%" src = "./images/Clockout-Banner.png" alt="Clock out Banner"/>
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
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default ClockOutTimes;
