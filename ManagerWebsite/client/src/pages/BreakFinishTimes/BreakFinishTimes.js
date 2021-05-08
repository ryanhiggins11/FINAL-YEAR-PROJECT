import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class BreakFinishTimes extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		breakFinishTimes: [],
		name: '',
		breakFinishedTime: ''
	};

	//loads clock in times
	componentDidMount() {
		this.loadBreakFinishTimes();
	}

	//gets clock in times
	loadBreakFinishTimes = () => {
		API.getBreakFinishTimes()
			.then(res => this.setState({ breakFinishTimes: res.data, name: '', breakFinishedTime: '' }))
			.catch(err => console.log(err));
	};

	//deletes clock in times
	deleteBreakFinishTime = id => {
		API.deleteBreakFinishTime(id)
			.then(res => this.loadBreakFinishTimes())
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
	 *displays clock in times
	 *gets clock in times
	 *delete button if you want to remove a clock in time
	 *two buttons to bring to clock out and employee details page
	 */
	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-6 sm-12">	
					
					<div>
						<header>
                			<img width="30%" src = "./images/Clockin-Banner.png" alt="Clock in Banner"/>
            			</header>
					</div>

					<div classname="break-finish"><h1>Employee Break Finish Times</h1></div>
					
						{this.state.breakFinishTimes.length ? (
							<List>
								
								{this.state.breakFinishTimes.map(breakFinishTime => (
									<ListItem key={breakFinishTime._id}>
										<Link to={'/breakFinishTimes/' + breakFinishTime._id}>
											<table>
												<td>{breakFinishTime.name} - Clocked into work at - {breakFinishTime.breakFinishedTime}</td>
												<td><DeleteBtn onClick={() => this.deletebreakFinishTime(breakFinishTime._id)} /></td> 
											</table>
										</Link>
										{/* <td><DeleteBtn onClick={() => this.deleteClockInTime(clockInTime._id)}/></td>  */}
									</ListItem>
									
								))}
							</List>
							
						) : (
							<h3>No Results Times To Display</h3>
						)}
                        <a href="clockInTimes"><button>Clock-In Times</button></a>
						<a href="clockOutTimes"><button>Clock-Out Times</button></a>
						<a href="breakStartTimes"><button>Break Start Times</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default BreakFinishTimes;