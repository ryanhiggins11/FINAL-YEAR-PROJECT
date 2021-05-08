import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ClockInTimes from './pages/ClockInTimes';
import ClockInDetail from './pages/ClockInDetail';
import ClockOutDetail from './pages/ClockOutDetail';
import NoMatch from './pages/NoMatch';
import Navbar from './components/Nav/Nav';
import ClockOutTimes from './pages/ClockOutTimes';
import BreakTimes from './pages/BreakTimes';
import BreakDetail from './pages/BreakDetail';
import User from './pages/User';

/*
*Gets path for the pages and then links them to the page
*/
const App = () => (
	<Router>
		<div>
			<Navbar />
			<Switch>
				<Route exact path="/" component={ClockInTimes} />
				<Route exact path="/clockInTimes" component={ClockInTimes} />
				<Route exact path="/clockInTimes/:id" component={ClockInDetail} />
				<Route exact path="/clockOutTimes" component={ClockOutTimes} />
				<Route exact path="/clockOutTimes/:id" component={ClockOutDetail}/>
				<Route exact path="/breakTimes/:id" component={BreakDetail} />
				<Route exact path="/breakTimes" component={BreakTimes} />
				<Route exact path="/user" component={User}/>
				<Route component={NoMatch} />
			</Switch>
		</div>
	</Router>
);

export default App;
