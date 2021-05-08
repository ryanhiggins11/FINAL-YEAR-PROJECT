import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class BreakStartDetail extends Component {
	//object declared breaktime
	state = {
		breakStartTime: {}
	};

	//gets clock out time
	componentDidMount() {
		API.getBreakStartTime(this.props.match.params.id)
			.then(res => this.setState({ breakStartTime: res.data }))
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
								<h2>{this.state.breakStartTime.name} - Had a break from work at - {this.state.breakStartTime.breakStartedTime}</h2>
							</table>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
						<Link to="/breakStartTimes">← Back to Break Start Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default BreakStartDetail;