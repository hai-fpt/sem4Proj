import React, { useState, useEffect } from "react";
import { Helmet } from "react-helmet";
import MyLeaveTools from './MyLeaveTool/index'
import MyLeaveTable from './MyLeaveTable/index'

const MyLeave = () => {

  useEffect(() => {
  }, []);

  return (
    <>
      <Helmet>
        <title>My leaves</title>
      </Helmet>
      <MyLeaveTools/>
      <MyLeaveTable/>
    </>
  );
};

export default MyLeave;
