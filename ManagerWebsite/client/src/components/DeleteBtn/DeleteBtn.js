import React from 'react';
import './DeleteBtn.css';

/*
*Adds in Remove to end of user 
*/
const DeleteBtn = props => (
	<span className="delete-btn" {...props}>
		- Remove ❌
	</span>
);

export default DeleteBtn;
