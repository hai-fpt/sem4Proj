import React, { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import PropTypes from "prop-types";
import brand from "enl-api/dummy/brand";
import Calendar from "../../../components/Calendar/Calendar";
import ApplyLeaveService from "./ApplyLeaveService";
import {useDispatch, useSelector} from "react-redux";
import {fetchLeaves} from "../../../redux/actions/applyLeaveActions";



const ApplyLeave = () => {
    const title = brand.name + " - Apply Leave";
    const description = brand.desc;
    const userDetail = JSON.parse(localStorage.getItem("userDetail"));
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const [selfLeaves, setSelfLeaves] = useState([]);
    const dispatch = useDispatch()
    const getSelfLeaves = async (id, baseApiUrl) => {
        const res = await ApplyLeaveService.getSelfLeave(id, baseApiUrl);
        localStorage.setItem("leaves", JSON.stringify(res.data));
        dispatch(fetchLeaves())
        setSelfLeaves(res.data)
    }
    useEffect(() => {
        getSelfLeaves(userDetail.id, baseApiUrl)
    }, []);



  return (
    <div>
      <Helmet>
        <title>{title}</title>
        <meta name="description" content={description} />
        <meta property="og:title" content={title} />
        <meta property="og:description" content={description} />
        <meta property="twitter:title" content={title} />
        <meta property="twitter:description" content={description} />
      </Helmet>
      <Calendar />
    </div>
  );
};

export default ApplyLeave;
