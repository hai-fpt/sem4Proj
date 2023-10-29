import React,{useState} from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";

const RejectReason = ({eventInfo, requestDecision}) => {
    const [reason, setReason] = useState("");
    const [sending, setSending] = useState(false);

    const submit = () => {
        setSending(true)
        requestDecision(eventInfo.decision, eventInfo.id, reason)
    }

    return (
        <div className={'comment-text-area'}>
            <TextField
                fullWidth={true}
                multiline
                defaultValue={reason}
                onChange={(e) => setReason(e.target.value)}
                />
            <div className={'action'}>
                <Button
                    disabled={sending}
                    color={'primary'}
                    variant={'contained'}
                    onClick={submit}
                    >Submit</Button>
            </div>
        </div>
    )
}

export default RejectReason;