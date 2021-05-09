import React, { Component } from 'react';
import DeleteBtn from '../../components/DeleteBtn';
import Jumbotron from '../../components/Jumbotron';
import API from '../../utils/API';
import { Link } from 'react-router-dom';
import { Col, Row, Container } from '../../components/Grid';
import { List, ListItem } from '../../components/List';
import { Input, FormBtn } from '../../components/Form';

class IsSick extends Component {
    //state with 3 javascript objects delcared
	state = {
		isSicks: [],
		name: '',
		isEmployeeSick: ''
	};

    //loads sick feature
	componentDidMount() {
		this.loadIsSicks();
	}

    //gets sick feature
	loadIsSicks = () => {
		API.getIsSicks()
			.then(res => this.setState({ isSicks: res.data, name: '', isEmployeeSick: '' }))
			.catch(err => console.log(err));
	};

    //deletes sick feature
	deleteIsSick = id => {
		API.deleteIsSick(id)
			.then(res => this.loadIsSicks())
			.catch(err => console.log(err));
	};

    /*
	 *renders image
	 *displays employees who have answered the sickness feature
	 *gets the list of employees who have answered the sickness feature
	 *delete button if you want to remove a sickness feature request
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

                    <div classname="is-sick"><h1>Employee's unable to make work today</h1></div>
                        {this.state.isSicks.length ? (
                            <List>

                                {this.state.isSicks.map(isSick => (
                                    <ListItem key={isSick._id}>
                                    {isSick.isEmployeeSick == 'Yes' ? (
                                        <table>
                                            <td>{isSick.name}</td>
                                            <td><DeleteBtn onClick={() => this.deleteIsSick(isSick._id)} /></td>
                                        </table>
                                         ): (
                                            <h3>No Sick Employees</h3>
                                        )} 
                                    </ListItem>
                                ))}
                            </List>

                        ) : (
                            <h3>No Sick Employees</h3>
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