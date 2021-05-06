import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class ClockOutDetail extends Component {
	//object declared clockoutime
	state = {
		clockOutTime: {}
	};

	//gets clock out time
	componentDidMount() {
		API.getClockOutTime(this.props.match.params.id)
			.then(res => this.setState({ clockOutTime: res.data }))
			.catch(err => console.log(err));
	}

	/*
	*renders images
	*displays name of employee and then displays clock out time
	*Back button to clock out times
	*/
	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-12">

						<div>
							<header>
                				<img width="30%" src = "../images/employee.png" alt="Details Banner"/>
            				</header>
						</div>

						<Jumbotron>
							<table>
								<h2>{this.state.clockOutTime.name} - Clocked out of work at - {this.state.clockOutTime.clockedOutTime}</h2>
							</table>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
						<Link to="/clockOutTimes">← Back to Clock-Out Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default ClockOutDetail;
