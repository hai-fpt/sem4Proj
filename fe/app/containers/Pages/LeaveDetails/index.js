import React, {useEffect, useState} from 'react';
import Outer from "../../Templates/Outer";
import moment from "moment";
import {fetchLeaveById} from "../../../api/leaveType/leaveType";
import {useSelector} from "react-redux";
import Loading from "../../../components/Loading";
import ArrowBack from "@material-ui/icons/ArrowBack";
import {NavLink} from "react-router-dom";

const LeaveDetails = ({match}) => {
    const id = match?.params?.id
    const [eventInfo, setEventInfo] = useState(null)
    const [loading, setLoading] = useState(true)
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);

    useEffect(() => {
        if (!baseApiUrl || !id) {
            return;
        }
        fetchLeaveById(baseApiUrl, id)
            .then(data => {
                setEventInfo({
                    username: data.user.name,
                    email: data.user.email,
                    start: data.fromDate,
                    end: data.endDate,
                    reason: data.reason,
                    status: data.status,
                    type: data.leave.name,
                    attachedFiles: data.attachedFiles
                })
            })
            .catch((err) => {

            })
            .finally(() => {
                setLoading(false)
            })
    }, [id, baseApiUrl])
    if (loading) {
        return <Loading/>
    }
    if (eventInfo)
        return (
            <Outer>
                <div style={{backgroundColor: "#ffffff", padding: 10, borderRadius: 5}}>
                    <p className="event-item">
                        <span className="event-label">Requested by: </span> {eventInfo.username}
                    </p>
                    <p className="event-item">
                        <span className="event-label">email: </span> {eventInfo.email}
                    </p>
                    <p className="event-item">
                <span
                    className="event-label">LeaveType: </span> {eventInfo.type}
                    </p>
                    <p className="event-item">
                <span
                    className="event-label">From date: </span> {moment(eventInfo.start).format('YYYY-MM-DD dddd hh:mm:ss')}
                    </p>
                    <p className="event-item">
                <span
                    className="event-label">To date: </span>{moment(eventInfo.end).format('YYYY-MM-DD dddd hh:mm:ss')}
                    </p>
                    <p className="event-item">
                        <span className="event-label">Reason: </span> {eventInfo.reason}
                    </p>
                    <p className="event-item">
                        <span className="event-label">Status: </span><span
                        className={"status " + eventInfo.status}>{eventInfo.status}</span>
                    </p>
                    <div style={{position: 'relative'}}>
                        <NavLink to="/">
                            <ArrowBack/>
                            &nbsp;back to site
                        </NavLink>
                    </div>

                </div>

            </Outer>
        )
    return (
        <Outer>
            <div style={{backgroundColor: "#ffffff", padding: 10, borderRadius: 5, color: "red"}}>
                <p>Error </p>
                <p>Cannot fetch data </p>
                <div style={{position: 'relative'}}>
                    <NavLink to="/">
                        <ArrowBack/>
                        &nbsp;back to site
                    </NavLink>
                </div>
            </div>
        </Outer>
    )
}

export default LeaveDetails;