import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ClockInTimes from './pages/ClockInTimes';
import ClockInDetail from './pages/ClockInDetail';
import ClockOutDetail from './pages/ClockOutDetail';
import NoMatch from './pages/NoMatch';
import Navbar from './components/Nav/Nav';
import ClockOutTimes from './pages/ClockOutTimes';



const App = () => (
	
    //<RealmAppProvider appId={APP_ID}>
	<Router>
		<div>
			<Navbar />
			<Switch>
				{}
				{/* <Route exact path="/" component={Home}/> */}
				<Route exact path="/" component={ClockInTimes} /> 
				<Route exact path="/clockInTimes" component={ClockInTimes} />
				<Route exact path="/clockInTimes/:id" component={ClockInDetail} />
				<Route exact path="/clockOutTimes" component={ClockOutTimes} />
				<Route exact path="/clockOutTimes/:id" component={ClockOutDetail}/>
				<Route component={NoMatch} />
			</Switch>
		</div>
	</Router>
	//</RealmAppProvider>
);


export default App;
