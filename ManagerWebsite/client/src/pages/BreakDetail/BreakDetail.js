import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class BreakDetail extends Component {
	//object declared breaktime
	state = {
		breakTime: {}
	};

	//gets break time
	componentDidMount() {
		API.getBreakTime(this.props.match.params.id)
			.then(res => this.setState({ breakTime: res.data }))
			.catch(err => console.log(err));
	}

	/*
	*renders images
	*displays name of employee and then displays break time
	*Back button to break times
	*/
	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-12">

						<div>
							<header>
                				<img width="30%" src = "../images/breaks.png" alt="Banner"/>
            				</header>
						</div>

						<Jumbotron>
							<table>
								<h2>{this.state.breakTime.name} - Had a break from work at - {this.state.breakTime.breakStartTime}</h2>
								<h2>{this.state.breakTime.name} - Finished their break from work at - {this.state.breakTime.breakFinishTime}</h2>
							</table>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
						<Link to="/breakTimes">← Back to Break Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default BreakDetail;