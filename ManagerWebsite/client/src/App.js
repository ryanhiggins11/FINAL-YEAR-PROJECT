import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
//import Books from './pages/Books';
import ClockInTimes from './pages/ClockInTimes';
import Detail from './pages/Detail';
import NoMatch from './pages/NoMatch';
import Navbar from './components/Nav/Nav';

const App = () => (
	<Router>
		<div>
			<Navbar />
			<Switch>
				{}
				<Route exact path="/" component={ClockInTimes} />
				<Route exact path="/clockInTimes" component={ClockInTimes} />
				<Route exact path="/clockInTimes/:id" component={Detail} />
				<Route component={NoMatch} />
			</Switch>
		</div>
	</Router>
);


export default App;
