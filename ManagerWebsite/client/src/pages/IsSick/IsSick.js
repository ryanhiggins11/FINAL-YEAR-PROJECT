import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class IsSick extends React.Component {
	//state with 3 javascript objects delcared
	state = {
		isSick: [],
		name: '',
		isEmployeeSick: ''
	};

	//loads clock out times
	componentDidMount() {
		this.loadIsSicks();
	}

	//gets clock out times
	loadIsSicks = () => {
		API.getIsSicks()
			.then(res => this.setState({ isSick: res.data, name: '', isEmployeeSick: '' }))
			.catch(err => console.log(err));
	};

	//deletes clock out times
	deleteIsSick = id => {
		API.deleteIsSick(id)
			.then(res => this.loadIsSicks())
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

					<div classname="clock-out"><h1>Sick Employees List:</h1></div>
						{this.state.isSick.length ? (
							<List>
								
								{this.state.isSick.map(isSickEmployee => (
									<ListItem key={isSickEmployee._id}>
										<Link to={'/isSick/' + isSickEmployee._id}>
											<table>
												<td>{isSickEmployee.name} Clocked out at {isSickEmployee.isEmployeeSick}</td>
												<td><DeleteBtn onClick={() => this.deleteisSick(isSickEmployee._id)} /></td>
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
						<a href="breakTimes"><button>Break Times</button></a>
						<a href="user"><button>Employee Details</button></a>
					</Col>
				</Row>
			</Container>
		);

	}
}

export default IsSick;