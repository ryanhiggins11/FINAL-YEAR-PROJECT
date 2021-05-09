import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class BreakTimes extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		breakTimes: [],
		name: '',
		breakStartTime: '',
		breakFinishTime: ''
	};

	//loads clock out times
	componentDidMount() {
		this.loadBreakTimes();
	}

	//gets clock out times
	loadBreakTimes = () => {
		API.getBreakTimes()
			.then(res => this.setState({ breakTimes: res.data, name: '', breakStartTime: '' , breakFinishTime: '' ,}))
			.catch(err => console.log(err));
	};

	//deletes clock out times
	deleteBreakTime = id => {
		API.deleteBreakTime(id)
			.then(res => this.loadBreakTimes())
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
	 *displays break times
	 *gets break times
	 *delete button if you want to remove a break time
	 *four buttons to bring to clock in times, clock out times, employee sick list and employee details page
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

					<div classname="break-start"><h1>Employees Break Times</h1></div>
						{this.state.breakTimes.length ? (
							<List>
								
								{this.state.breakTimes.map(breakTime => (
									<ListItem key={breakTime._id}>
										<Link to={'/breakTimes/' + breakTime._id}>
											<table>
												<td>{breakTime.name} started their break at {breakTime.breakStartTime} and finished their break at {breakTime.breakFinishTime}</td>
												<td><DeleteBtn onClick={() => this.deleteBreakTime(breakTime._id)} /></td>
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
						<a href="isSick"><button>Employee Sick List</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default BreakTimes;