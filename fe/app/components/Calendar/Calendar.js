import React, { useEffect } from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { Calendar as BigCalendar, momentLocalizer, Views } from "react-big-calendar";
import Popup from "react-popup";
import moment from "moment";
import ApplyLeaveForm from "../Forms/ApplyLeaveForm";
import { createLeave, deleteLeave, fetchLeaves, updateLeave } from "enl-redux/actions/applyLeaveActions";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "./style.css";
import "./popup.css";

const localizer = momentLocalizer(moment);

const Calendar = ({ leaves, fetchLeaves, createLeave, deleteLeave }) => {
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
    const formValues = { ...values };
    const start = new Date(formValues.start);
    const end = new Date(formValues.end);

    const title = `${start.getDate()} ${start.toLocaleString("default", { month: "long" })} ${start.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit"
    })} - ${end.getDate()} ${end.toLocaleString("default", { month: "long" })} ${end.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit"
    })}`;

    createLeave({ ...formValues, start, end, title });
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
    const leaveInfo1 = { ...leaveInfo };
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
        <ApplyLeaveForm onSubmit={(values) => onSubmit(values)} onCancel={() => Popup.close()} />
      ),
    });
  };

  const eventStyleGetter = (event, start, end, isSelected) => {
    const current_time = moment().format("YYYY MM DD");
    const event_time = moment(event.start).format("YYYY MM DD");
    const background = current_time > event_time ? "#DE6987" : "#8CBD4C";
    return {
      style: {
        backgroundColor: background,
      },
    };
  };

  return (
    <div className="calendar-container">
      <BigCalendar
        popup
        selectable
        localizer={localizer}
        defaultView={Views.MONTH}
        style={{ height: 600 }}
        events={leaves}
        eventPropGetter={eventStyleGetter}
        onSelectEvent={(slotInfo) => onSelectEventHandler(slotInfo)}
        onSelectSlot={(leaveInfo) => onSelectEventSlotHandler(leaveInfo)}
      />
      <Popup />
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

export default connect(mapStateToProps, mapDispatchToProps)(Calendar);
