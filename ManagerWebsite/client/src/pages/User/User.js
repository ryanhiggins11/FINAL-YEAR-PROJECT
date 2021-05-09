import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';



class User extends Component {
	//state with 3 javascript objects delcared
	state = {
		User: [],
		name: '',
		firstName: '',
		secondName: '',
		dateOfBirth: '',
		emergencyContact:''
	};

	//loads users
	componentDidMount() {
		this.loadUsers();
	}

	//gets users from database
	loadUsers = () => {
		API.getUsers()
			.then(res => this.setState({ User: res.data, name: '', firstName: '', secondName: '', dateOfBirth: '', emergencyContact: ''}))
			.catch(err => console.log(err));
	};

	//deletes users
	deleteUser = id => {
		API.deleteUser(id)
			.then(res => this.loadUsers())
			.catch(err => console.log(err));
	};

	handleInputChange = event => {
		const { name, value } = event.target;
		this.setState({
			[name]: value
		});
	};

	/*
	*Loads in employees
	*Gets employees Email, First Name, Surname, DOB and Emergency Contact Number
	*Delete button
	*displays these all in a table
	*four buttons to bring to clock in times, clock out times, break times and employee sick list
	*/
	render() {
		console.log(this.state.User)
		return (
			<Container fluid>
					<div classname="banner-out">
						<header>
                			<img width="30%" src = "./images/employees.png" alt="breaks"/>
            			</header>
					</div>		
						<h1>Employee Details</h1>
						{this.state.User.length ? (
							<List>
								{this.state.User.map(User => (
									<table key={User._id}>
											<thead>
												<tr>
												<td><th>Employee Email: {User.name}</th></td>
												<td><th>First Name: {User.firstName}</th></td>
												<td><th>Surname: {User.secondName}</th></td>
												<td><th>DOB: {User.dateOfBirth}</th></td>
												<td><th>Emergency Contact Number: {User.emergencyContact}</th></td>
												<td><DeleteBtn onClick={() => this.deleteUser(User._id)} /></td>
												</tr>
											</thead>
									</table>
								))}
								</List>
						) : (
							<h3>No Results to Display</h3>
						)}
					
					<a href="clockOutTimes"><button>Clock-Out Times</button></a>
					<a href="clockInTimes"><button>Clock-In Times</button></a>
					<a href="breakTimes"><button>Break Times</button></a>
					<a href="isSick"><button>Employee Sick List</button></a>
				
				
			</Container>
		);
	}
}

export default User;