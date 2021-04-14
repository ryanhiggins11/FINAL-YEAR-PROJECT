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
					<Col size="md-12">
						<Jumbotron>
							<h1>
								{this.state.clockInTime.name} clocked in at {this.state.clockInTime.clockedInTime}
							</h1>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
					<Link to="/">← Back to Clock-Out Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default ClockInDetail;
