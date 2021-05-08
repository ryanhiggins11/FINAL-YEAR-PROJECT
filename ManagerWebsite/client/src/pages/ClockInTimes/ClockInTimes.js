import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class ClockInTimes extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		clockInTimes: [],
		name: '',
		clockedInTime: ''
	};

	//loads clock in times
	componentDidMount() {
		this.loadClockInTimes();
	}

	//gets clock in times
	loadClockInTimes = () => {
		API.getClockInTimes()
			.then(res => this.setState({ clockInTimes: res.data, name: '', clockedInTime: '' }))
			.catch(err => console.log(err));
	};

	//deletes clock in times
	deleteClockInTime = id => {
		API.deleteClockInTime(id)
			.then(res => this.loadClockInTimes())
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
	 *displays clock in times
	 *gets clock in times
	 *delete button if you want to remove a clock in time
	 *two buttons to bring to clock out and employee details page
	 */
	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-6 sm-12">	
					
					<div>
						<header>
                			<img width="30%" src = "./images/Clockin-Banner.png" alt="Clock in Banner"/>
            			</header>
					</div>

					<div classname="clock-in"><h1>Employee Clock-in Times</h1></div>
					
						{this.state.clockInTimes.length ? (
							<List>
								
								{this.state.clockInTimes.map(clockInTime => (
									<ListItem key={clockInTime._id}>
										<Link to={'/clockInTimes/' + clockInTime._id}>
											<table>
												<td>{clockInTime.name} - Clocked into work at - {clockInTime.clockedInTime}</td>
												<td><DeleteBtn onClick={() => this.deleteClockInTime(clockInTime._id)} /></td> 
											</table>
										</Link>
										{/* <td><DeleteBtn onClick={() => this.deleteClockInTime(clockInTime._id)}/></td>  */}
									</ListItem>
									
								))}
							</List>
							
						) : (
							<h3>No Results Times To Display</h3>
						)}
						<a href="clockOutTimes"><button>Clock-Out Times</button></a>
						<a href="breakTimes"><button>Break Times</button></a>
						<a href="isSick"><button>Employee Sick List</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default ClockInTimes;
