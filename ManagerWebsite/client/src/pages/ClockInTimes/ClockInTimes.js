import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class ClockInTimes extends React.Component {
	state = {
		clockInTimes: [],
		name: '',
		clockedInTime: ''
	};

	componentDidMount() {
		this.loadClockInTimes();
	}

	loadClockInTimes = () => {
		API.getClockInTimes()
			.then(res => this.setState({ clockInTimes: res.data, name: '', clockedInTime: '' }))
			.catch(err => console.log(err));
	};

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

	/* handleFormSubmit = event => {
	 	event.preventDefault();
	 	if (this.state.employee && this.state.clockedInTime) {
	 		API.saveClockInTime({
	 			employee: this.state.employee,
	 			clockedInTime: this.state.clockedInTime
	 		})
	 			.then(res => this.loadClockInTimes())
	 			.catch(err => console.log(err));
		}
	 };*/

	render() {
		return (
			<Container fluid>
				<Row>
					{/* <Col size="md-6">
						<Jumbotron>
							<h1>Enter Employee and Clock In Times</h1>
						</Jumbotron>
						<form>
							<Input
								value={this.state.employee}
								onChange={this.handleInputChange}
								name="employee"
								placeholder="Employee (required)"
							/>
							<Input
								value={this.state.clockedInTime}
								onChange={this.handleInputChange}
								name="clockedInTime"
								placeholder="Clock In Time (required)"
							/>

							<FormBtn
								disabled={!(this.state.clockedInTime && this.state.employee)}
								onClick={this.handleFormSubmit}
							>
								Submit time
							</FormBtn>
						</form>
					</Col> */}
					<Col size="md-6 sm-12">	
					
					<div classname="clock-in"><h1>Employee Clock-in Times</h1></div>
						{this.state.clockInTimes.length ? (
							<List>
								
								{this.state.clockInTimes.map(clockInTime => (
									<ListItem key={clockInTime._id}>
										<Link to={'/clockInTimes/' + clockInTime._id}>
											<table>
												{clockInTime.name} - Clocked into work at - {clockInTime.clockedInTime}
											</table>
										</Link>
										<DeleteBtn onClick={() => this.deleteClockInTime(clockInTime._id)} />
									</ListItem>
									
								))}
							</List>
							
						) : (
							<h3>No Results to Display</h3>
						)}
						<a href="clockOutTimes"><button>Clock-Out Times</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default ClockInTimes;
