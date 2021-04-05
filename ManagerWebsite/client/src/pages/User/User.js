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
		users: [], // set collection name here
		firstName: '',
		lastName: '',
		email: '',
		password: ''
	};

	componentDidMount() {
		this.loadUsers();
	}

	loadUsers = () => {
		API.getUsers()
			.then(res => this.setState({ users: res.data, firstName: '', lastName: '', email: '', password: ''}))
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

	handleFormSubmit = event => {
		event.preventDefault();
		if (this.state.firstName && this.state.lastName && this.state.email && this.state.password) {
			API.saveUser({
				firstName: this.state.firstName,
				lastName: this.state.lastName,
                email: this.state.email,
                password: this.state.password
			})
				.then(res => this.loadUsers())
				.catch(err => console.log(err));
		}
	};

	render() {
		return (
			<Container fluid>
				<Row>
					<Col size="md-6">
							<h1>Enter Employee Details Here</h1>
						<form>
							<Input
								value={this.state.firstName}
								onChange={this.handleInputChange}
								name="firstName"
								placeholder="First Name (required)"
							/>
							<Input
								value={this.state.lastName}
								onChange={this.handleInputChange}
								name="lastName"
								placeholder="Last Name (required)"
							/>
                            <Input
								value={this.state.email}
								onChange={this.handleInputChange}
								name="email"
								placeholder="Email (required)"
							/>
							<Input
								value={this.state.password}
								onChange={this.handleInputChange}
								type="password"
								name="password"
								placeholder="Password (required)"
							/>

							<FormBtn
								disabled={!(this.state.firstName && this.state.lastName 
                                            && this.state.email && this.state.password)}
								onClick={this.handleFormSubmit}
							>
								Submit details
							</FormBtn>
						</form>
					</Col>
					<Col size="md-6 sm-12">
							<h1>Employees</h1>
						{this.state.users.length ? (
							<List>
								{this.state.users.map(user => (
									<ListItem key={user._id}>
										<Link to={'/users/' + user._id}>
											<strong>
												{user.firstName} {user.lastName}'s email is: {user.email}
											</strong>
										</Link>
										<DeleteBtn onClick={() => this.deleteUser(user._id)} />
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
