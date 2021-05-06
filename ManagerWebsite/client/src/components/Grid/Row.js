import React from "react";

/*
*sorts rows in table
*/

export const Row = ({ fluid, children }) => (
  <div className={`row${fluid ? "-fluid" : ""}`}>
    {children}
  </div>
);
