import React, {useEffect, useState} from "react";
import {Calendar, momentLocalizer} from "react-big-calendar";
import Popup from "react-popup";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./style.css";
import "./popup.css";
import EventInfoDialog from "./EventInfoDialog";
import {fetchLeavesMothly} from "enl-api/LeaveManagement/index"


const localizer = momentLocalizer(moment);

const LeaveManagementCalendar = ({baseApiUrl, id, requestDecision}) => {
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
                status: obj.userLeave.status,
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
            title: leaveInfo.title,
            content: <EventInfoDialog eventInfo={leaveInfo} requestDecision={requestDecision}/>,
        });
    };


    const onSelectEventSlotHandler = (leaveInfo) => {
        console.log(leaveInfo);
        const data = {
            start: leaveInfo.start,
            end: leaveInfo.end,
        };
        openPopupForm(data);
    };

    const openPopupForm = (leaveInfo) => {
        const leaveInfo1 = {...leaveInfo};
        let newEvent = false;
        let popupTitle = "Update Event";

        if (!leaveInfo.hasOwnProperty("id")) {
            leaveInfo1.id = moment().format("x");
            leaveInfo1.title = null;
            leaveInfo1.location = null;
            popupTitle = "Request for leave";
            newEvent = true;
        }

        Popup.create({
            title: popupTitle,
            content: (
                <div></div>
            ),
        });
    };

    const eventStyleGetter = (event, start, end, isSelected) => {
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
            case 'CANCELED':
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
