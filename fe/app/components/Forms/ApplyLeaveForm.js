import React, {useEffect, useState} from "react";
import {withStyles} from "@material-ui/core/styles";
import {Field, reduxForm} from "redux-form";
import Grid from "@material-ui/core/Grid";
import red from "@material-ui/core/colors/red";
import moment from "moment";
import FileUpload from "react-material-file-upload";

import {bindActionCreators} from "redux";
import {connect, useDispatch, useSelector} from "react-redux";
import Button from "@material-ui/core/Button";
import Box from "@material-ui/core/Box";
import {clearAction, initAction} from "enl-redux/actions/reduxFormActions";
import FormControl from "@material-ui/core/FormControl";
import {DateTimePickerFieldRedux, SelectRedux, TextFieldRedux,} from "./ReduxFormMUI";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import Chip from "@material-ui/core/Chip";
import ApplyLeaveService from "../../containers/Pages/Leaves/ApplyLeaveService";
import {setUsers} from "../../redux/actions/teamActions";
import {setLeaveTypes} from "../../redux/actions/applyLeaveApiActions";
import CircularProgress from "@material-ui/core/CircularProgress";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import dayjs from 'dayjs'
import {injectIntl} from 'react-intl';
import messages from "enl-api/applyleave/applyLeaveMessages";
// validation functions
const required = (value) => (value == null ? "Required" : undefined);

const styles = (theme) => ({
    root: {
        flexGrow: 1,
        // padding: 30,
    },
    field: {
        width: "90%",
        marginBottom: 20,
        borderColor: theme.palette.error.main,
        '&:hover': {
            borderColor: theme.palette.error.main,
        },
        '&.Mui-focused': {
            borderColor: theme.palette.error.main,
        },
    },
    dateTime:{
        "& .MuiInputBase-root":{
            alignItems:"baseline !important"
        }
    },
    fieldBasic: {
        width: "90%",
        marginBottom: 20,
        marginTop: 10,
    },
    inlineWrap: {
        display: "flex",
        flexDirection: "row",
    },
    buttonInit: {
        margin: theme.spacing(4),
        textAlign: "center",
    },
    formControl: {
        width: "100%",
        marginBottom: theme.spacing(1),
    },
    formControlCanlendar: {
        width: "100%",
        marginTop: 20,
    },
    btnArea: {
        display: "flex",
        justifyContent: "end",
        margin: `${theme.spacing(4)}px 0 ${theme.spacing(2)}px`,
        fontSize: 12,
        "& $label": {
            fontSize: 12,
            "& span": {
                fontSize: 12,
            },
        },
        "& button": {
            margin: `0 ${theme.spacing(1)}px`,
        },
        [theme.breakpoints.down("xs")]: {
            flexDirection: "column",
            "& button": {
                width: "100%",
                margin: 5,
            },
        },
    },
    btn: {
        width: "100px",
    },
    redBtn: {
        color: theme.palette.common.white,
        background: red[500],
        "&:hover": {
            background: red[700],
        },
        width: "100px",
    },
});

const ApplyLeaveForm = (props) => {
    const {leaveInfo, intl} = props;
    const userDetail = JSON.parse(localStorage.getItem("userDetail"));
    const [sending, setSending] = useState(false);
    const [validated, setValidated] = useState(false);

    const dispatch = useDispatch();
    const [files, setFiles] = useState([]);
    const [formData, setFormData] = useState({
        user: {
            id: userDetail.id
        },
        leave: {
            id: null
        },
        fromDate: moment(new Date(leaveInfo.start)).format('YYYY-MM-DD hh:mm:ss'),
        toDate: moment(new Date(leaveInfo.end)).format('YYYY-MM-DD hh:mm:ss'),
        reason: '',
        teamLeads: [],
        updatedBy: userDetail.email,
    })
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const leaveType = useSelector((state) => state.applyLeaveApi)
    const users = useSelector((state) => state.users);

    const formatDate = (value) => {
        return value.toLocaleString("en-US", {
            year: 'numeric',
            month: '2-digit',
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
            hour12: false
        }).replace(/,/g, '').replace(/(\d+)\/(\d+)\/(\d+)/, '$3-$1-$2');
    };

    const handleFormValueProps = (data, field) => {
        if (field === "fromDate" || field === "toDate") {
            data = formatDate(new Date(data));
        }
        setFormData((prevFormData) => ({
            ...prevFormData,
            [field]: data,
        }));
    };


    const getLeaveType = async () => {
        try {
            const response = await ApplyLeaveService.getLeaveType(baseApiUrl);
            dispatch(setLeaveTypes(response.data))
        } catch (error) {
            throw new Error(error);
        }
    }

    const getUsers = async () => {
        try {
            const res = await ApplyLeaveService.getUsers(baseApiUrl);
            dispatch(setUsers(res.data));
        } catch (error) {
            throw new Error(error);
        }
    }

    const handleFileChange = (e) => {
        setFiles(e.target.files);
    }

    const postApplyLeave = async () => {
        setSending(true)
        if (files.length > 0) {
            await ApplyLeaveService.postApplyLeave(formData, baseApiUrl)
                .then(res => ApplyLeaveService.postAttachment(files, res, baseApiUrl)
                    .then(res => setSending(false)))
                .catch(e=>{
                    setSending(false)
                })
        } else {
            await ApplyLeaveService.postApplyLeave(formData, baseApiUrl)
                .then(res => setSending(false))
                .catch(e=>{
                    setSending(false)
                })
        }
    }

    const fieldValidation = (formData) => {
        const keys = Object.keys(formData);
        for (const key of keys) {
            const value = formData[key];
            if (value === null || value === '') {
                return false;
            }
        }
        return true;
    }

    useEffect(() => {
        getLeaveType();
        getUsers();
        setValidated(fieldValidation(formData));
    }, [formData]);

    const trueBool = true;
    const {classes, handleSubmit, onCancel} = props;

    const customHandleSubmit = (e) => {
        postApplyLeave();
        handleSubmit(e);
    };

    return (
        <form onSubmit={customHandleSubmit}>
            <Grid container direction="row" spacing={2}>
                <Grid item xs>
                    <Box>
                        <div>
                            <div>{intl.formatMessage(messages.name)}</div>
                            <div>{userDetail.name}</div>
                        </div>
                    </Box>
                    <Box
                        mt={2}
                        sx={{display: "grid", gridTemplateRows: "repeat(3, 1fr)", gap: 1}}
                    >
                        <div>
                            <FormControl className={classes.field}>
                                <InputLabel htmlFor="leave_type">{intl.formatMessage(messages.type)}</InputLabel>
                                <Field
                                    name="leave_type"
                                    component={SelectRedux}
                                    validate={[required]}
                                    placeholder="Selection"
                                    autoWidth={trueBool}
                                    onChange={(e) => handleFormValueProps({id: e.target.value}, 'leave')}
                                >
                                    {
                                        leaveType.content !== undefined &&
                                        leaveType.content.map((leave, index) => {
                                            return <MenuItem value={leave.id} key={index}>{leave.name}</MenuItem>
                                        })
                                    }
                                </Field>
                            </FormControl>
                        </div>

                        <div>
                            <FormControl className={classes.field}>
                                <InputLabel htmlFor="request_to">{intl.formatMessage(messages.request)}</InputLabel>
                                <Field
                                    name="request_to"
                                    component={SelectRedux}
                                    placeholder="Selection"
                                    validate={[required]}
                                    autoWidth={trueBool}
                                    type="select-multiple"
                                    multiple
                                    value={[
                                        formData.teamLeads
                                    ]}
                                    renderValue={(selected) => (
                                        <div className={classes.chipWrapper}>
                                            {
                                                selected.map((value, index) => {
                                                    const val = users.content.find((user) => user.id === value);
                                                    if (val) {
                                                        return <Chip key={index} label={val.name}/>
                                                    }
                                                })
                                            }
                                        </div>
                                    )}
                                    onChange={(e) => handleFormValueProps(e.target.value, 'teamLeads')}
                                >
                                    {
                                        users.content !== undefined &&
                                        users.content.map((user, index) => {
                                            return <MenuItem value={user.id} key={index}>{user.name}</MenuItem>
                                        })
                                    }
                                </Field>
                            </FormControl>
                        </div>
                        <div>
                            <FormControl className={classes.field}>
                                <InputLabel htmlFor="inform_to">{intl.formatMessage(messages.inform)}</InputLabel>
                                <Field
                                    name="inform_to"
                                    component={SelectRedux}
                                    placeholder="Selection"
                                    validate={[required]}
                                    autoWidth={trueBool}
                                    type="select-multiple"
                                    multiple
                                    value={[
                                        formData.informTo
                                    ]}
                                    renderValue={(selected) => (
                                        <div className={classes.chipWrapper}>
                                            {
                                                selected.map((value, index) => {
                                                    const val = users.content.find((user) => user.id === value);
                                                    if (val) {
                                                        return <Chip key={index} label={val.name}/>
                                                    }
                                                })
                                            }
                                        </div>
                                    )}
                                    onChange={(e) => handleFormValueProps(e.target.value, 'informTo')}
                                >
                                    {
                                        users.content !== undefined &&
                                        users.content.map((user, index) => {
                                            return <MenuItem value={user.id} key={index}>{user.name}</MenuItem>
                                        })
                                    }
                                </Field>
                            </FormControl>
                        </div>
                        <div>
                            <FormControl>
                                <input type={"file"} multiple onChange={handleFileChange} />
                                <span style={{textTransform: 'uppercase', fontWeight: 'bold'}}>{intl.formatMessage(messages.fileSize)}</span>
                            </FormControl>
                        </div>
                    </Box>
                </Grid>

                <Grid item xs>
                    <Box
                        mt={2}
                        sx={{display: "grid", gridTemplateRows: "repeat(2, 1fr)", gap: 1}}
                    >
                        <div>
                            <FormControl className={classes.formControl}>
                                <DateTimePicker
                                    value={dayjs(formData.fromDate)}
                                    onChange={(newDate) =>{
                                        handleFormValueProps(newDate.toISOString(), 'fromDate')
                                    }}
                                    label={intl.formatMessage(messages.from)}
                                    className={classes.dateTime}
                                />
                            </FormControl>
                        </div>
                        <div>
                            <FormControl className={classes.formControlCanlendar}>
                                <DateTimePicker
                                    value={dayjs(formData.toDate)}
                                    onChange={(newDate) =>{
                                        handleFormValueProps(newDate.toISOString(), 'toDate')
                                    }}
                                    label={intl.formatMessage(messages.to)}
                                    className={classes.dateTime}
                                />
                            </FormControl>
                        </div>
                        <div>
                            <FormControl className={classes.formControlCanlendar}>
                                <Field
                                    name="reason"
                                    className={classes.field}
                                    component={TextFieldRedux}
                                    label={intl.formatMessage(messages.reason)}
                                    multiline={trueBool}
                                    rows={4}
                                    onChange={(e) => handleFormValueProps(e.target.value, 'reason')}
                                />
                            </FormControl>
                        </div>
                    </Box>
                </Grid>
            </Grid>
            {sending ? <div style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center'
                }}>
                    <CircularProgress/>
                </div> :
                <div className={classes.btnArea}>
                    <Button
                        className={classes.redBtn}
                        variant="contained"
                        fullWidth
                        color="secondary"
                        size="large"
                        type="button"
                        onClick={onCancel}
                    >
                        {intl.formatMessage(messages.cancel)}
                    </Button>
                    <Button
                        className={classes.btn}
                        variant="contained"
                        disabled={!validated}
                        fullWidth
                        color="primary"
                        size="large"
                        type="submit"
                    >
                        {intl.formatMessage(messages.save)}
                    </Button>
                </div>}
        </form>
    );
};

const mapDispatchToProps = (dispatch) => ({
    init: bindActionCreators(initAction, dispatch),
    clear: () => dispatch(clearAction),
});

const ReduxFormMapped = reduxForm({
    form: "applyLeaveForm",
    enableReinitialize: true,
})(ApplyLeaveForm);

const FormInit = connect(
    (state) => ({
        initialValues: state.formValues,
    }),
    mapDispatchToProps
)(ReduxFormMapped);

export default withStyles(styles)(injectIntl(FormInit));
