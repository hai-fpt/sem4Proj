import React, { useState, useEffect } from "react";
import LeaveCalendar from '../Calendar';
import LeaveCounter from "../LeaveCounter";
import Holidays from '../Holidays/index';
import Grid from '@material-ui/core/Grid';
import Grow from "@material-ui/core/Grow";
import { useSelector } from 'react-redux';


const MyLeaveTools = () => {
  const [isGrow, setIsGrow] = useState(false);
  const currentYear = new Date().getFullYear();
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const id = useSelector((state) => state.detailProfile.id)

  useEffect(() => {
    const delay = setTimeout(() => {
      setIsGrow(true);
    }, 700);

    return () => clearTimeout(delay);
  }, []);

  return (
    <Grid container spacing={2}>
        <Grow in={isGrow} timeout={400}>
          <Grid item xs={12} sm={6} md={4}>
            <LeaveCalendar year={currentYear} id={id} baseApiUrl={baseApiUrl}/>
          </Grid>
        </Grow>
        <Grow in={isGrow} timeout={800}>
          <Grid item xs={12} sm={6} md={4}>
            <LeaveCounter year={currentYear} id={id} baseApiUrl={baseApiUrl}/>
          </Grid>
        </Grow>
        <Grow in={isGrow} timeout={1200}>
          <Grid item xs={12} sm={12} md={4}>
            <Holidays year={currentYear} id={id} baseApiUrl={baseApiUrl}/>
          </Grid>
        </Grow>
    </Grid>
  );
};

export default MyLeaveTools;
