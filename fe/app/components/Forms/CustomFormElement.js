import React, {useState} from 'react';
import PropTypes from 'prop-types';
import Input from '@material-ui/core/Input';
import {makeStyles} from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import Chip from '@material-ui/core/Chip';
import MenuItem from '@material-ui/core/MenuItem';
import {format} from 'date-fns';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import {
    MuiPickersUtilsProvider,
    DatePicker,
} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import CalendarTodayIcon from '@material-ui/icons/CalendarToday';
import IconButton from '@material-ui/core/IconButton';
import {useSelector} from 'react-redux';
import messages from "enl-api/user/manageUserMessages";
import {useIntl} from "react-intl";
import InputLabel from "@material-ui/core/InputLabel";

const useStyles = makeStyles({
    formElement: {
        display: 'flex',
        flexDirection: 'column',
        rowGap: 8,
    },
    requiredElement: {
        color: 'red',
    },
    InputText: {
        width: '100%',
        //maxWidth: 450,
        fontSize: 16,
        marginBottom: 30,
        height: 36.6,
    },
    textArea: {
        marginBottom: 0,
        minHeight: 121,
        '& .MuiInput-underline:before': {
            border: 'none', // Remove the underline
        },
        '& .MuiInput-underline:hover:not(.Mui-disabled):before': {
            border: 'none', // Remove the underline on hover
        },
        '& .MuiInput-underline:after': {
            border: 'none', // Remove the underline after input
        },
    },
    inputDate: {
        width: '100%',
        //maxWidth: 450,
        fontSize: 16,
        marginBottom: 30,
    },
    typo: {
        paddingLeft: 8,
        textTransform: 'capitalize'
    },
    chipWrapper: {
        display: 'flex',
        flexWrap: 'wrap',
        gap: 8
    },
    multiSelect: {
        //maxWidth:450,
        marginBottom: 30,
    }

});

const checkTypeForInput = (props) => {
    const classes = useStyles();
    const intl = useIntl();
    const teamData = useSelector((state) => state.team.content)
    const departmentData = useSelector((state) => state.department);
    const managerData = useSelector((state) => state.manager);
    const userData = useSelector((state) => state.users);
    const [dateData, setDateData] = useState(props.detailFormData ? props.detailFormData[props.field] : new Date());


    const formatTodayDateTime = () => {
        const formattedDateTime = format(new Date(), 'yyyy-MM-dd HH:mm:ss');
        return formattedDateTime;
    }

    const handleChange = (event) => {
        props.valueFormProps(event.target.value, props.field);
    }

    const handleChangeTeam = (event) => {
        props.valueFormProps([{teamName: event.target.value}], 'teams');
    }

    const handleChangeDate = (value) => {
        const date = new Date(value);
        setDateData(value)
        const formattedDate = format(date, "yyyy-MM-dd HH:mm:ss");
        props.valueFormProps(formattedDate, props.field);
    }

    const handleChangeStatus = (status) => {
        props.valueFormProps(status, 'status');
    }


    const handleChangeRole = (e) => {
        const selectedRoles = e.target.value;
        let seenRoleNames = {};
        const roleObjects = selectedRoles.reduce((result, role) => {
            const roleName = role.role.name;
            if (!seenRoleNames.hasOwnProperty(roleName)) {
                seenRoleNames[roleName] = result.length;
                result.push({role: {name: roleName}});
            } else {
                const existingIndex = seenRoleNames[roleName];
                result.splice(existingIndex, 1);
            }
            return result;
        },[])
        props.valueFormProps(roleObjects, props.field);
        setRenderSelected(roleObjects);
    };

    //logic to check when the detail information pass in this component there will be afull data, this function push data in the field we pass
    const checkDetailValueProp = () => {
        if (!props.detailFormData || props.detailFormData === {}) {
            return;
        }
        if (props.field === 'userTeams') {
            return props.detailFormData['userTeams']?.map(({team}) => team.teamName)[0]
        }
        return props.detailFormData[props.field];
    }
    const [renderSelected, setRenderSelected] = useState(
        props.detailFormData ? checkDetailValueProp() : []
    )

    let inputElement = null;
    switch (props.formElementType) {
        case 'text' || 'email':
            inputElement = (
                <Input className={classes.InputText}
                       onChange={handleChange}
                       required={props.isRequired ? true : false}
                       type={props.formElementType}
                       defaultValue={checkDetailValueProp()}
                       disabled={props.disabled}
                />
            );
            break;

        case 'tel':
            inputElement = (
                <Input className={classes.InputText}
                       onChange={handleChange}
                       required={props.isRequired ? true : false}
                       type={props.formElementType}
                       inputProps={{
                           pattern: '^[0-9]+$',
                       }}
                       defaultValue={checkDetailValueProp()}
                       placeholder="Enter phone number (e.g., 123-456-7890)"
                       disabled={props.disabled}
                />
            );
            break;

        case 'textarea':
            inputElement = (
                <TextField
                    onChange={handleChange}
                    className={`${classes.InputText} ${classes.textArea}`}
                    multiline
                    maxRows={5}
                    required={props.isRequired ? true : false}
                    defaultValue={checkDetailValueProp()}
                    disabled={props.disabled}
                    InputProps={{
                        style: {
                            alignItems: 'flex-start',
                            minHeight: 121,
                            paddingTop: 10,
                        },
                    }}
                />
            );
            break;

        case 'date':
            inputElement = (
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                    <DatePicker
                        className={classes.inputFromDate}
                        variant="inline"
                        format="yyyy-MM-dd"
                        required={props.isRequired ? true : false}
                        value={dateData}
                        onChange={handleChangeDate}
                        disabled={props.disabled}
                        InputProps={{
                            endAdornment: (
                                <IconButton>
                                    <CalendarTodayIcon/>
                                </IconButton>
                            ),
                        }}
                    />
                </MuiPickersUtilsProvider>
            );
            break;

        case 'today_date':
            inputElement = (
                <TextField
                    className={classes.inputDate}
                    type="text"
                    required={props.isRequired ? true : false}
                    value={props.detailFormData ? checkDetailValueProp() : formatTodayDateTime()}
                    disabled
                />
            );
            break;

        case 'select_department':
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={classes.selectEmpty}
                >
                    {
                        departmentData &&
                        Object.values(departmentData).map((department, index) => {
                            return (
                                <option key={index} value={department.name}>{department.name}</option>
                            )
                        })
                    }
                </Select>
            );
            break;

        case 'select_rank':
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={classes.selectEmpty}
                >
                    <option value="EMPLOYEE">EMPLOYEE</option>
                    <option value="SENIOR_MANAGER">SENIOR_MANAGER</option>
                    <option value="MANAGER">MANAGER</option>
                    <option value="ASSISTANT_MANAGER">ASSISTANT_MANAGER</option>
                </Select>
            );
            break;

        case 'select':
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={classes.selectEmpty}
                >
                    <option value="">None</option>
                    <option value={10}>Ten</option>
                    <option value={20}>Twenty</option>
                    <option value={30}>Thirty</option>
                </Select>
            );
            break;

        case 'multi_select_team':
            inputElement = (
                <Select
                    className={classes.multiSelect}
                    // multiple
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChangeTeam}
                    required={props.isRequired ? true : false}
                    input={<Input/>}
                    // renderValue={(selected) => (
                    //   <div className={classes.chipWrapper}>
                    //     {selected.map((value, index) => {
                    //         return (
                    //           <Chip key={index} label={value} />
                    //         )
                    //       }
                    //     )}
                    //   </div>
                    // )}
                >
                    {teamData &&
                        teamData.map((team) => {
                            return (
                                <MenuItem key={team.id} value={team.teamName}>
                                    {team.teamName}
                                </MenuItem>
                            )
                        })
                    }
                </Select>
            );
            break;

        case 'multi_select_users':
            inputElement = (
                <Select
                    className={classes.multiSelect}
                    multiple
                    defaultValue={props.detailFormData ? checkDetailValueProp() : []}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    input={<Input/>}
                    renderValue={(selected) => (
                        <div className={classes.chipWrapper}>
                            {
                                selected.map((value, index) => {
                                    const users = userData.data.content.find((users) => users.id === value);
                                    if (users) {
                                        return <Chip key={index} label={users.name}/>;
                                    }
                                    return null;
                                })
                            }
                        </div>
                    )}
                >
                    {userData &&
                        userData.data.content.map((user) => {
                            return (
                                <MenuItem value={user.id}>
                                    {user.name}
                                </MenuItem>
                            )
                        })
                    }
                </Select>
            );
            break;

        case "manager_select":
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={classes.selectEmpty}
                >
                    <option value="">None</option>
                    {
                        managerData &&
                        managerData.data.map((manager, index) => {
                            return (
                                <option value={manager.id} key={index}>{manager.name}</option>
                            )
                        })
                    }
                </Select>
            );
            break;

        case 'status_radio': {
            const [status, setStatus] = React.useState(props.detailFormData ? checkDetailValueProp() : true);
            inputElement = (
                <RadioGroup
                    value={status}
                    onChange={(e) => {
                        const newStatus = e.target.value === 'true';
                        setStatus(newStatus);
                        handleChangeStatus(newStatus);
                    }}
                >
                    <FormControlLabel value={true} control={<Radio color='primary'/>} label={intl.formatMessage(messages.active)}/>
                    <FormControlLabel value={false} control={<Radio color='primary'/>} label={intl.formatMessage(messages.inactive)}/>
                </RadioGroup>
            );
        }
            break;

        case "department_manager_select":
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={classes.multiSelect}
                >
                    <option value="">None</option>
                    {
                        departmentData &&
                        departmentData.data.content.map((value, index) => {
                            return (
                                <option value={value.id} key={index}>{value.manager.name}</option>
                            )
                        })
                    }
                </Select>
            );
            break;
        case 'department_multi_select_team':
            inputElement = (
                <Select
                    className={classes.multiSelect}
                    multiple
                    defaultValue={props.detailFormData ? checkDetailValueProp() : []}
                    onChange={handleChange}
                    required = {props.isRequired ? true : false}
                    input={<Input/>}
                    renderValue={(selected) => (
                        <div className={classes.chipWrapper}>
                            {selected.map((value, index) => {
                                const team = departmentData.data.content.find(team => team.id === value)
                                if (team) {
                                    return (
                                        <Chip key={index} label={team.teamName} />
                                    )
                                }
                                return null;
                                }
                            )}
                        </div>
                    )}
                >
                    { departmentData.data.content &&
                        departmentData.data.content.map((team) => {
                            return (
                                <MenuItem key={team.id} value={team.id}>
                                    {team.teamName}
                                </MenuItem>
                            )
                        })
                    }
                </Select>
            );
            break;

        case 'selectAffectsDaysOff':
            inputElement = (
                <Select
                    native
                    defaultValue={checkDetailValueProp()}
                    onChange={handleChange}
                    required={props.isRequired ? true : false}
                    className={`${classes.selectEmpty} ${classes.InputText}`}
                >
                    <option value="true">True</option>
                    <option value="false">False</option>
                </Select>
            );
            break;

        case 'multi_select_role':
            inputElement = (
                <Select
                    className={classes.multiSelect}
                    multiple
                    // defaultValue={props.detailFormData ? checkDetailValueProp() : []}
                    value={renderSelected}
                    onChange={handleChangeRole}
                    required={props.isRequired ? true : false}
                    input={<Input/>}
                    renderValue={(selected) => (
                        <div className={classes.chipWrapper}>
                            {selected &&
                                selected
                                    .filter(
                                        (role, index, self) =>
                                            index ===
                                            self.findIndex(
                                                (r) => r.role.name === role.role.name
                                            )
                                    )
                                    .map((value, index) => (
                                        <Chip key={index} label={value?.role.name} />
                                    ))}
                        </div>
                    )}
                >
                    <MenuItem key={"ADMIN"} value={{
                        role: {
                            name: "ADMIN"
                        }
                    }}>ADMIN</MenuItem>
                    <MenuItem key={"MANAGER"} value={{
                        role: {
                            name: "MANAGER"
                        }
                    }}>MANAGER</MenuItem>
                    <MenuItem key={"USER"} value={{
                        role: {
                            name: "USER"
                        }
                    }}>USER</MenuItem>
                </Select>
            );
            break;

        default:
            inputElement = null;
            break;
    }

    return inputElement;
};

const CustomFormElement = React.memo(
    (props) => {
        const classes = useStyles();
        const intl = useIntl();
        const {label, isRequired} = props;
        const formattedLabel = (label) => {
            switch (label) {
                case 'name':
                    return intl.formatMessage(messages.name);
                case 'email':
                    return intl.formatMessage(messages.email);
                case 'team':
                    return intl.formatMessage(messages.team);
                case 'joned date':
                    return intl.formatMessage(messages.joined);
                case 'duration of employment':
                    return intl.formatMessage(messages.duration);
                case 'action':
                    return intl.formatMessage(messages.action);
                case 'active':
                    return intl.formatMessage(messages.active);
                case 'inactive':
                    return intl.formatMessage(messages.inactive);
                case 'list':
                    return intl.formatMessage(messages.list);
                case 'create':
                    return intl.formatMessage(messages.create);
                case 'update':
                    return intl.formatMessage(messages.update);
                case 'birthday':
                    return intl.formatMessage(messages.dob);
                case 'phone':
                    return intl.formatMessage(messages.phone);
                case 'University':
                    return intl.formatMessage(messages.university);
                case 'University code':
                    return intl.formatMessage(messages.universityCode);
                case 'Graduated date':
                    return intl.formatMessage(messages.graduate);
                case 'exp':
                    return intl.formatMessage(messages.exp);
                case 'level':
                    return intl.formatMessage(messages.rank);
                case 'department':
                    return intl.formatMessage(messages.department);
                case 'resigned date':
                    return intl.formatMessage(messages.resign);
                case 'Created date':
                    return intl.formatMessage(messages.created);
                case 'updatedBy':
                    return intl.formatMessage(messages.updatedBy);
                case 'Updated date':
                    return intl.formatMessage(messages.updated);
                case 'skills':
                    return intl.formatMessage(messages.skill);
                case 'status':
                    return intl.formatMessage(messages.status);
                case 'roles':
                    return intl.formatMessage(messages.roles);
                default:
                    return label;
            }
        };

        return (
            <div className={classes.formElement}>
                <Typography className={classes.typo}>
                    {formattedLabel(label)}
                    {isRequired && (
                        <span className={classes.requiredElement}>(*)</span>
                    )}
                </Typography>
                {checkTypeForInput(props)}
            </div>
        );
    },
);

CustomFormElement.propTypes = {
    label: PropTypes.string.isRequired,
    isRequired: PropTypes.bool,
    formElementType: PropTypes.string,
    field: PropTypes.string.isRequired,
    detailFormData: PropTypes.any,
    disabled: PropTypes.bool,
}

CustomFormElement.defaultProps = {
    isRequired: false,
    formElementType: 'text',
    disabled: false,
};

export default CustomFormElement;