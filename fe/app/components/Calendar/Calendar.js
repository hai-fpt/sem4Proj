import React, {useEffect, useState} from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Calendar, momentLocalizer} from "react-big-calendar";
import Popup from "react-popup";
import moment from "moment";
import ApplyLeaveForm from "../Forms/ApplyLeaveForm";
import {createLeave, deleteLeave, fetchLeaves, updateLeave} from "enl-redux/actions/applyLeaveActions";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./style.css";
import "./popup.css";
import {injectIntl} from 'react-intl';
import messages from "enl-api/applyleave/applyLeaveMessages";

const localizer = momentLocalizer(moment);

const CalendarApplyLeave = ({leaves, fetchLeaves, createLeave, deleteLeave, intl}) => {
    const [currentMonthView, setCurrentMonthView] = useState(moment().format('MM'));
    const [currentYearView, setCurrentYearView] = useState(moment().format('YYYY'));
    useEffect(() => {
        fetchLeaves();
    }, []);

    const renderEventContent = (slotInfo) => {
        return (
            <div>
                <p>
                    Duration: <strong>{slotInfo.title}</strong>
                </p>
            </div>
        );
    };

    const onSelectEventHandler = (leaveInfo) => {
        Popup.create({
            title: leaveInfo.title,
            content: renderEventContent(leaveInfo),
        });
    };

    const onSubmit = (values) => {
        const formValues = {...values};
        const start = new Date(formValues.start);
        const end = new Date(formValues.end);

        const title = `${start.getDate()} ${start.toLocaleString("default", {month: "long"})} ${start.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        })} - ${end.getDate()} ${end.toLocaleString("default", {month: "long"})} ${end.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        })}`;

        createLeave({...formValues, start, end, title});
        Popup.close();
    };

    const onSelectEventSlotHandler = (leaveInfo) => {
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
            popupTitle = intl.formatMessage(messages.title);
            newEvent = true;
        }

        Popup.create({
            title: popupTitle,
            content: (
                <ApplyLeaveForm leaveInfo={leaveInfo1} onSubmit={(values) => onSubmit(values)}
                                onCancel={() => Popup.close()}/>
            ),
        });
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


    const handleViewMonthChange = (date) => {
        setCurrentMonthView(moment(date).format('MM'));
        setCurrentYearView(moment(date).format('YYYY'));
    };

    return (
        <div className="calendar-container">
            <Calendar
                popup
                selectable
                localizer={localizer}
                defaultView={'month'}
                events={leaves}
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

const mapStateToProps = (state) => ({
    leaves: state.applyLeaveReducer.leaves,
});

const mapDispatchToProps = (dispatch) => ({
    fetchLeaves: bindActionCreators(fetchLeaves, dispatch),
    createLeave: bindActionCreators(createLeave, dispatch),
    updateLeave: bindActionCreators(updateLeave, dispatch),
    deleteLeave: bindActionCreators(deleteLeave, dispatch),
});

export default connect(mapStateToProps, mapDispatchToProps)(injectIntl(CalendarApplyLeave));
