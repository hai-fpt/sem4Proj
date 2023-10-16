import React, { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import PropTypes from "prop-types";
import brand from "enl-api/dummy/brand";
import Calendar from "../../../components/Calendar/Calendar";
import ApplyLeaveService from "./ApplyLeaveService";
import {useSelector} from "react-redux";



const ApplyLeave = () => {
    const title = brand.name + " - Apply Leave";
    const description = brand.desc;
    const userDetail = JSON.parse(localStorage.getItem("userDetail"));
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const [selfLeaves, setSelfLeaves] = useState([]);

    const getSelfLeaves = async (id, baseApiUrl) => {
        const res = await ApplyLeaveService.getSelfLeave(id, baseApiUrl);
        setSelfLeaves(res.data)
    }
    useEffect(() => {
        getSelfLeaves(userDetail.id, baseApiUrl)
    }, []);
    if (selfLeaves.length !== 0) {
        localStorage.setItem("leaves", JSON.stringify(selfLeaves));
    }


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
