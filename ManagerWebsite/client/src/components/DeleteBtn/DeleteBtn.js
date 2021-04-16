import React from 'react';
import './DeleteBtn.css';

const DeleteBtn = props => (
	<span className="delete-btn" {...props}>
		- Remove ❌
	</span>
);

export default DeleteBtn;
