import React from 'react';

const Nav = () => (
	<nav className="navbar navbar-inverse navbar-top">
		<div className="container-fluid">
			<div className="navbar-header">
				<a href="/" className="navbar-brand">
					Home
				</a>
				<a href="/addEmployee" className="navbar-brand">
					Add Employee
				</a>
			</div>
		</div>
	</nav>
);

export default Nav;
