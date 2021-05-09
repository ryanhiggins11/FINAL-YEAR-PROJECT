import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class ClockInDetail extends Component {
	/*
	*Delcare a state and clockintime object
	*/	
	state = {
		clockInTime: {}
	};

	//gets clocck in time
	componentDidMount() {
		API.getClockInTime(this.props.match.params.id)
			.then(res => this.setState({ clockInTime: res.data }))
			.catch(err => console.log(err));
	}

	/*
	*renders images
	*displays name of employee and then displays clock in time
	*Back button to clock in times
	*/
	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-6 sm-12">
						
						<div>
							<header>
                				<img width="30%" src = "../images/clockIn.png" alt="Details Banner"/>
            				</header>
						</div>

						<Jumbotron>
							<h2>
								{this.state.clockInTime.name} clocked in at {this.state.clockInTime.clockedInTime}
								
							</h2>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
						<Link to="/">← Back to All Clock-In Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default ClockInDetail;
