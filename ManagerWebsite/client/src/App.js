import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
//import Books from './pages/Books';
import ClockInTimes from './pages/ClockInTimes';
import User from './pages/User';
import Detail from './pages/Detail';
import NoMatch from './pages/NoMatch';
import Nav from './components/Nav';

const App = () => (
	
    //<RealmAppProvider appId={APP_ID}>
	<Router>
		<div>
			<Nav />
			<Switch>
				{/* <Route exact path="/" component={Books} />
				<Route exact path="/books" component={Books} />
				<Route exact path="/books/:id" component={Detail} />
				<Route component={NoMatch} /> */}

				<Route exact path="/" component={ClockInTimes} />
				<Route exact path="/clockInTimes" component={ClockInTimes} />
				<Route exact path="/clockInTimes/:id" component={Detail} />
				<Route exact path="/addEmployee" component={User} />
				<Route component={NoMatch} />
			</Switch>
		</div>
	</Router>
	//</RealmAppProvider>
);

export default App;
