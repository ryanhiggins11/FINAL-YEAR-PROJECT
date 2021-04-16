import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';

class User extends Component {

	state = {
		User: [],
		name: '',
		firstName: '',
		secondName: '',
		dateOfBirth: '',
		emergencyContact:''
	};

	componentDidMount() {
		this.loadUsers();
	}

	loadUsers = () => {
		API.getUsers()
			.then(res => this.setState({ User: res.data, name: '', firstName: '', secondName: '', dateOfBirth: '', emergencyContact: ''}))
			.catch(err => console.log(err));
	};

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

	// handleFormSubmit = event => {
	// 	event.preventDefault();
	// 	if (this.state.name && this.state.firstName && this.state.secondName && this.state.dateOfBirth && this.state.emergencyContact) {
	// 		API.saveUser({
	// 			name: this.state.name,
	// 			firstName: this.state.firstName,
    //             secondName: this.state.secondName,
    //             dateOfBirth: this.state.dateOfBirth,
	// 			emergencyContact: this.state.emergencyContact
	// 		})
	// 			.then(res => this.loadUsers())
	// 			.catch(err => console.log(err));
	// 	}
	// };
	
	render() {
		console.log(this.state.User)
		return (
			<Container fluid>
				<Row>
					<Col size="md-6 sm-12">
						<h1>Employees</h1>
						{this.state.User.length ? (
							<List>
								{this.state.User.map(User => (
									<ListItem key={User._id}>
										{/* <Link to={'/users/' + u._id}> */}
											<table>
												<td>Employee Email: {User.name}</td>
												<td>Employee First Name: {User.firstName}</td>
												<td>Employee Surname: {User.secondName}</td>
												<td>Employee Date of Birth: {User.dateOfBirth}</td>
												<td>Emergency Contact Number: {User.emergencyContact}</td>
												<td><DeleteBtn onClick={() => this.deleteUser(User._id)} /></td>
											</table>
										{/* </Link> */}

									</ListItem>
								))}
							</List>
						) : (
							<h3>No Results to Display</h3>
						)}
					</Col>
				</Row>
			</Container>
		);
	}
}

export default User;