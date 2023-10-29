import React, {memo, useLayoutEffect, useRef, useState} from 'react';
import moment from "moment/moment";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import {TabPanel, TabsNavigation, Loading} from 'enl-components';
import {useEventComments} from "./hooks/useEventComments";
import CircularProgress from "@material-ui/core/CircularProgress";
import Input from "@material-ui/core/Input";
import LeaveRequestFile from "./LeaveRequestFile";
import {useIntl} from "react-intl";
import messages from "enl-api/leaveManagement/manageLeaveMessages";


const EventInfo = ({eventInfo, requestDecision}) => {
    const intl = useIntl();
    const [isLoading, setIsLoading] = useState(false);
    const [reject, setReject] = useState(false);
    const [reason, setReason] = useState("");

    return (
        <div style={{marginTop: 20}}>
            <div style={{display: "flex", justifyContent: "space-between"}}>
                <div style={{flex: 1}}>
                    <p className="event-item">
                        {
                            eventInfo.title ? (
                                <>
                                    <span className="event-label">{intl.formatMessage(messages.requestedBy)}: </span> {eventInfo.title}
                                </>
                            ) : null
                        }
                    </p>
                    <p className="event-item">
                <span
                    className="event-label">{intl.formatMessage(messages.from)}: </span> {moment(eventInfo.start).format('YYYY-MM-DD dddd hh:mm:ss')}
                    </p>
                    <p className="event-item">
                <span
                    className="event-label">{intl.formatMessage(messages.to)}: </span>{moment(eventInfo.end).format('YYYY-MM-DD dddd hh:mm:ss')}
                    </p>
                    <p className="event-item">
                        <span className="event-label">{intl.formatMessage(messages.reason)}: </span> {eventInfo.reason ? eventInfo.reason : '...'}
                    </p>
                    <p className="event-item">
                        <span className="event-label">{intl.formatMessage(messages.status)}: </span><span
                        className={"status " + eventInfo.status}>{intl.formatMessage(messages.statusOptions[eventInfo.status.toLowerCase()])}</span>
                    </p>
                    <p className="event-item">
                        <span className="event-label">{intl.formatMessage(messages.overallStatus)}: </span><span
                        className={"status " + eventInfo.overallStatus}>{intl.formatMessage(messages.statusOptions[eventInfo.status.toLowerCase()])}</span>
                    </p>
                    {
                        eventInfo && eventInfo.file.length > 0 &&
                        <div className="event-item">
                            <span className="event-label">{intl.formatMessage(messages.files)}: </span>
                            <LeaveRequestFile files={eventInfo.file} id={eventInfo.id}></LeaveRequestFile>
                        </div>
                    }
                    {
                        reject ?
                            <div className="event-item">
                                <span className="event-label">{intl.formatMessage(messages.rejectReason)}: </span>
                                <Input
                                    fullWidth={true}
                                    style={{lineHeight: "1.4"}}
                                    defaultValue={reason}
                                    onChange={(e) => setReason(e.target.value)}
                                />
                            </div>
                            : null
                    }
                </div>
                <div style={{flex: 1}}>
                    {eventInfo.managers ?
                        <ManagerList managers={eventInfo.managers}/>
                        :
                        null
                    }
                </div>
            </div>
            {
                eventInfo.status === 'PENDING' && requestDecision &&
                (
                    reject === false ? (isLoading ? <CircularProgress/> :
                        <div className="decision-button">
                            <Button className="approve" onClick={() => {
                                setIsLoading(true)
                                requestDecision('APPROVED', eventInfo.id)
                            }}>{intl.formatMessage(messages.approve)}</Button>
                            <Button className="reject" onClick={() => {
                                setReject(true)
                            }}>{intl.formatMessage(messages.reject)}</Button>
                        </div>) : (
                        <div className="decision-button">
                            <Button className="cancel" onClick={() => {
                                setReject(false);
                            }}>{intl.formatMessage(messages.cancel)}</Button>
                            <Button className="reject" onClick={() => {
                                requestDecision("REJECTED", eventInfo.id, reason)
                            }}>{intl.formatMessage(messages.reject)}</Button>
                        </div>
                    )
                )
            }
        </div>
    )
}

const CommentItem = memo(({item}) => {
    return (
        <div className={'comment-item'}>
            <div className={"comment-header"}>
                <span className={"user-name"}>{item.author}</span>
                <span>{moment(item.createdAt).format("DD/MM/YYYY HH:mm")}</span>
            </div>
            <div className={'comment-content'} dangerouslySetInnerHTML={{__html: item.comment}}/>
        </div>

    )
})
const ManagerList = ({managers}) => {
    const intl = useIntl();
    return (
        <div style={{flex: 1, display: "flex", flexDirection: "column", alignItems: "flex-end"}}>
            <span className="event-label" style={{textAlign: "left", justifyContent: "flex-end", display: "flex"}}>{intl.formatMessage(messages.manager)}</span>
            {managers.map((manager, index) => (
                <div key={index} style={{textAlign: "left", justifyContent: "flex-end",alignItems: "flex-end", display: "flex", flexDirection: "column"}}>
                    <p style={{fontWeight: "bold"}}>{manager.name}</p>
                    <p className={"status " + manager.status}>{intl.formatMessage(messages.statusOptions[manager.status.toLowerCase()])}</p>
                </div>
            ))}
        </div>
    )
}
const EventComment = ({eventInfo}) => {
    const {comments, loading, handleAddComment, sending} = useEventComments(eventInfo.id)
    const [comment, setComment] = useState("")
    const lastRef = useRef()
    const intl = useIntl();
    const onSubmit = () => {
        if (!comment || sending)
            return;
        handleAddComment(comment)
        setComment("")
    }

    useLayoutEffect(() => {
        if (!lastRef.current)
            return;
        lastRef.current.scrollIntoView()
    }, [comments])

    return (
        <div style={{marginTop: 20}}>
            <div className={"comment-space"}>
                {
                    loading ?
                        <Loading/>
                        :
                        comments.map(item => <CommentItem key={item.id} item={item}/>)
                }
                <div ref={lastRef}/>
            </div>
            <div className={"comment-text-area"}>
                <TextField
                    fullWidth={true}
                    multiline={true}
                    rows={3}
                    value={comment}
                    onChange={e => setComment(e.target.value)}
                    onKeyDown={event => {
                        if (event.key == "Enter" && (event.ctrlKey || event.metaKey) && comment) {
                            onSubmit()
                        }
                    }}
                />
                <div className={"action"}>
                    <Button
                        disabled={sending}
                        color={"primary"}
                        variant={"contained"}
                        onClick={onSubmit}
                    >
                        {intl.formatMessage(messages.submit)}
                    </Button>
                </div>
            </div>
        </div>
    )
}

function EventInfoDialog({eventInfo, requestDecision, tabDefault = 0}) {
    const [tab, setTab] = useState(tabDefault)
    const intl = useIntl();
    return (
        <div>
            <TabsNavigation tabItems={[{label: intl.formatMessage(messages.information), index: 0}, {label: intl.formatMessage(messages.comment), index: 1}]}
                            tabValuePropsFromChild={setTab} tabValuePropsFromParent={tab}/>
            <div className={"p10"}>
                <TabPanel tabIndex={0} tabValue={tab}>
                    <EventInfo eventInfo={eventInfo} requestDecision={requestDecision}/>
                </TabPanel>
                <TabPanel tabIndex={1} tabValue={tab}>
                    <EventComment eventInfo={eventInfo}/>
                </TabPanel>
            </div>
        </div>
    );
}

export default EventInfoDialog;