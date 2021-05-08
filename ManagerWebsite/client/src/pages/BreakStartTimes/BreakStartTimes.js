import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class BreakStartTimes extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		breakStartTimes: [],
		name: '',
		breakStartedTime: ''
	};

	//loads clock out times
	componentDidMount() {
		this.loadBreakStartTimes();
	}

	//gets clock out times
	loadBreakStartTimes = () => {
		API.getBreakStartTimes()
			.then(res => this.setState({ breakStartTimes: res.data, name: '', breakStartedTime: '' }))
			.catch(err => console.log(err));
	};

	//deletes clock out times
	deleteBreakStartTime = id => {
		API.deleteBreakStartTime(id)
			.then(res => this.loadBreakStartTimes())
			.catch(err => console.log(err));
	};


	handleInputChange = event => {
		const { name, value } = event.target;
		this.setState({
			[name]: value
		});
	};

	 /*
	 *renders image
	 *displays clock out times
	 *gets clock out times
	 *delete button if you want to remove a clock out time
	 *two buttons to bring to clock in times and employee details page
	 */
	render() {
		return (
			<Container fluid>
				<Row>
					{}
					<Col size="md-6 sm-12">	
					
					<div classname="banner-out">
						<header>
                			<img width="30%" src = "./images/Clockout-Banner.png" alt="Clock out Banner"/>
            			</header>
					</div>

					<div classname="break-start"><h1>Employees Break Start Times</h1></div>
						{this.state.breakStartTimes.length ? (
							<List>
								
								{this.state.breakStartTimes.map(breakStartTime => (
									<ListItem key={breakStartTime._id}>
										<Link to={'/breakStartTimes/' + breakStartTime._id}>
											<table>
												<td>{breakStartTime.name} Break Started at {breakStartTime.breakStartedTime}</td>
												<td><DeleteBtn onClick={() => this.deleteBreakStartTime(breakStartTime._id)} /></td>
											</table>
										</Link>
									</ListItem>
								))}
							</List>

						) : (
							<h3>No Results Times To Display</h3>
						)}
						<a href="clockInTimes"><button>Clock-In Times</button></a>
                        <a href="clockOutTimes"><button>Clock-Out Times</button></a>
						<a href="breakFinishTimes"><button>Break Finish Times</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default BreakStartTimes;