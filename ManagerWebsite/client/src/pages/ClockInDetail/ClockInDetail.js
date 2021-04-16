import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class ClockInDetail extends Component {
	state = {
		clockInTime: {}
	};


	componentDidMount() {
		API.getClockInTime(this.props.match.params.id)
			.then(res => this.setState({ clockInTime: res.data }))
			.catch(err => console.log(err));
	}

	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-6 sm-12">

						<div>
							<header>
                				<img width="30%" src = "../images/employee.png" alt="Details Banner"/>
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
