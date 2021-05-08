import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';

class BreakFinishDetail extends Component {
	/*
	*Delcare a state and clockintime object
	*/	
	state = {
		breakFinishTime: {}
	};

	//gets clocck in time
	componentDidMount() {
		API.getBreakFinishTime(this.props.match.params.id)
			.then(res => this.setState({ breakFinishTime: res.data }))
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
                				<img width="30%" src = "../images/employee.png" alt="Details Banner"/>
            				</header>
						</div>

						<Jumbotron>
							<h2>
								{this.state.breakFinishTime.name} break finished at {this.state.breakFinishTime.breakFinishedTime}
								
							</h2>
						</Jumbotron>
					</Col>
				</Row>

				<Row>
					<Col size="md-2">
						<Link to="/">← Back to All Break Started Times</Link>
					</Col>
				</Row>
			</Container>
		);
	}
}

export default BreakFinishDetail;