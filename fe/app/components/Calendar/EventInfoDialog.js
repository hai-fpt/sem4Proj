import React, {memo, useLayoutEffect, useRef, useState} from 'react';
import moment from "moment/moment";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";

import {TabPanel, TabsNavigation, Loading} from 'enl-components';
import {useEventComments} from "./hooks/useEventComments";

const EventInfo = ({eventInfo, requestDecision}) => {
    return (
        <>
            <p className="event-item">
                <span className="event-label">Requested by: </span> {eventInfo.title}
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
                <span className="event-label">Status: </span><span
                className={"status " + eventInfo.status}>{eventInfo.status}</span>
            </p>
            {
                eventInfo.status === 'PENDING' && requestDecision &&
                (
                    <div className="decision-button">
                        <Button className="approve" onClick={() => {
                            requestDecision('APPROVED', eventInfo.id)
                        }}>Approve</Button>
                        <Button className="reject" onClick={() => {
                            requestDecision('REJECTED', eventInfo.id)
                        }}>Reject</Button>
                    </div>
                )
            }
        </>
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
const EventComment = ({eventInfo}) => {
    const {comments, loading, handleAddComment, sending} = useEventComments(eventInfo.id)
    const [comment, setComment] = useState("")
    const lastRef = useRef()
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
        <div>
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
                        Submit
                    </Button>
                </div>
            </div>
        </div>
    )
}

function EventInfoDialog({eventInfo, requestDecision, tabDefault = 0}) {
    const [tab, setTab] = useState(tabDefault)

    return (
        <div>
            <TabsNavigation tabItems={[{label: "Information", index: 0}, {label: "Comment", index: 1}]}
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