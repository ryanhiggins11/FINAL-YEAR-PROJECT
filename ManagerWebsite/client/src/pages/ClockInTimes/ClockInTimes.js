import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';

class ClockInTimes extends Component {
	state = {
		clockInTimes: [],
		employee: '',
		clockedInTime: ''
	};

	componentDidMount() {
		this.loadClockInTimes();
	}

	loadClockInTimes = () => {
		API.getClockInTimes()
			.then(res => this.setState({ clockInTimes: res.data, employee: '', clockedInTime: '' }))
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

	// handleFormSubmit = event => {
	// 	event.preventDefault();
	// 	if (this.state.employee && this.state.clockedInTime) {
	// 		API.saveClockInTime({
	// 			employee: this.state.employee,
	// 			clockedInTime: this.state.clockedInTime
	// 		})
	// 			.then(res => this.loadClockInTimes())
	// 			.catch(err => console.log(err));
	// 	}
	// };

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
						<Jumbotron>
							<h1>Employees and times they clocked in</h1>
						</Jumbotron>
						{this.state.clockInTimes.length ? (
							<List>
								{this.state.clockInTimes.map(clockInTime => (
									<ListItem key={clockInTime._id}>
										<Link to={'/clockInTimes/' + clockInTime._id}>
											<strong>
												{clockInTime.employee} clocked in at {clockInTime.clockedInTime}
											</strong>
										</Link>
										<DeleteBtn onClick={() => this.deleteClockInTime(clockInTime._id)} />
									</ListItem>
								))}
							</List>
						) : (
							<h3>No Results to Display</h3>
						)}
					</Col>
				</Row>
			</Container>
		);
	}
}

export default ClockInTimes;
