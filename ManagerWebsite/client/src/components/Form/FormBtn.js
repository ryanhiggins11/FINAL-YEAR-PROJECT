import React from "react";

/*
*named export that exports a const declaration
*button here with styling applied
*/
export const FormBtn = props => (
  <button {...props} style={{ float: "right", marginBottom: 10 }} className="btn btn-success">
    {props.children}
  </button>
);
