import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class ClockOutDetail extends Component {
	state = {
		clockOutTime: {}
	};


	componentDidMount() {
		API.getClockOutTime(this.props.match.params.id)
			.then(res => this.setState({ clockOutTime: res.data }))
			.catch(err => console.log(err));
	}

	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-12">
						<Jumbotron>
							<h1>
								{this.state.clockOutTime.name} clocked out at {this.state.clockOutTime.clockedOutTime}
							</h1>
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
