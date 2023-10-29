import React, {useEffect, useState} from "react";
import {Calendar, momentLocalizer} from "react-big-calendar";
import Popup from "react-popup";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./style.css";
import "./popup.css";
import EventInfoDialog from "./EventInfoDialog";
import {fetchLeavesMothly, fetchLeavesDaily} from "enl-api/LeaveManagement/index"
import { useDispatch } from 'react-redux';
import {setDayLeave} from 'enl-redux/actions/leaveManageActions';
import {injectIntl, useIntl} from 'react-intl';
import messages from "enl-api/leaveManagement/manageLeaveMessages";

const localizer = momentLocalizer(moment);

const LeaveManagementCalendar = ({baseApiUrl, id, requestDecision, handleTabValueProps}) => {
    const dispatch = useDispatch();
    const intl = useIntl();
    const [currentMonthView, setCurrentMonthView] = useState(moment().format('MM'));
    const [currentYearView, setCurrentYearView] = useState(moment().format('YYYY'));
    const [formedLeaves, setFormedLeaves] = useState([]);
    useEffect(() => {
        fetchLeaveMonthRequestData();
    }, [currentMonthView]);

    useEffect(() => {
    }, [formedLeaves]);

    const fetchLeaveMonthRequestData = async () => {
        try {
            const res = await fetchLeavesMothly(baseApiUrl, id, currentMonthView, currentYearView, 0, 100);
            const reFormedLeave = res.data.content.map((obj) => ({
                id: obj.userLeave.id,
                title: obj.userLeave.user.name,
                start: new Date(obj.userLeave.fromDate),
                end: new Date(obj.userLeave.toDate),
                status : obj.status,
                reason : obj.userLeave.reason,
                overallStatus: obj.userLeave.status,
                file: obj.userLeave.attachedFiles,
            }));
            setFormedLeaves(reFormedLeave);
        } catch (error) {
            console.error(error);
        }
    };

    const handleViewMonthChange = (date) => {
        setCurrentMonthView(moment(date).format('MM'));
        setCurrentYearView(moment(date).format('YYYY'));
    };

    const onSelectEventHandler = (leaveInfo) => {
        Popup.create({
            title: intl.formatMessage(messages.title),
            content: <EventInfoDialog eventInfo={leaveInfo} requestDecision={requestDecision}/>,
        });
    };


    const onSelectEventSlotHandler = async (leaveInfo) => {
        try {
            const res = await fetchLeavesDaily(baseApiUrl, id, moment(leaveInfo.start).format("YYYY-MM-DD HH:mm:ss"))
            if (res.data.content.length !== 0) {
                handleTabValueProps(0)
                await dispatch(setDayLeave({data:  res.data.content, date: moment(leaveInfo.start).format("YYYY-MM-DD dddd")}));
            }
        } catch (error) {
            throw Error(error);
        }
    };

    const eventStyleGetter = (event) => {
        let background = 'transparent';

        switch (event.status) {
            case 'APPROVED':
                background = '#4CAF50';
                break;
            case 'REJECTED':
                background = '#e65100';
                break;
            case 'PENDING':
                background = '#ffc107';
                break;
            case 'CANCELLED':
                background = '#bdbdbd';
                break;
            default:
                break;
        }
        return {
            style: {
                backgroundColor: background,
            },
        };
    };


    return (
        <div className="calendar-container">
            <Calendar
                popup
                selectable
                localizer={localizer}
                defaultView={'month'}
                events={formedLeaves}
                onSelectEvent={(slotInfo) => onSelectEventHandler(slotInfo)}
                onSelectSlot={(leaveInfo) => onSelectEventSlotHandler(leaveInfo)}
                onNavigate={(date) => handleViewMonthChange(date)}
                eventPropGetter={eventStyleGetter}
                style={{minHeight: '100vh'}}
                views={['month']}
            />
            <Popup/>
        </div>
    );
};


export default LeaveManagementCalendar;
