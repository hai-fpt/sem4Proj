import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Input from '@material-ui/core/Input';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import Chip from '@material-ui/core/Chip';
import MenuItem from '@material-ui/core/MenuItem';
import { format } from 'date-fns';
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
import { useSelector } from 'react-redux';

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
      const transformedArray = event.target.value.map((team) => ({ teamName: team }));
      props.valueFormProps(transformedArray, 'teams');
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

    //logic to check when the detail information pass in this component there will be afull data, this function push data in the field we pass
    const checkDetailValueProp = () =>{
        if (!props.detailFormData || props.detailFormData === {}) {
          return ;
        }
        if (props.field === 'userTeams'){
          return props.detailFormData['userTeams'].map(({ team }) => team.teamName );
        }
        return props.detailFormData[props.field];
    }

    let inputElement = null;
    switch (props.formElementType) {
        case 'text' || 'email':
            inputElement = (
                <Input className={classes.InputText}
                onChange={handleChange}
                required = {props.isRequired ? true : false}
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
                required = {props.isRequired ? true : false}
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
                required = {props.isRequired ? true : false}
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
                       <CalendarTodayIcon />
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
                required = {props.isRequired ? true : false}
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
              required = {props.isRequired ? true : false}
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
              required = {props.isRequired ? true : false}
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
              required = {props.isRequired ? true : false}
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
            multiple
            defaultValue={props.detailFormData ? checkDetailValueProp() : []}
            onChange={handleChangeTeam}
            required = {props.isRequired ? true : false}
            input={<Input/>}
            renderValue={(selected) => (
              <div className={classes.chipWrapper}>
                {selected.map((value, index) => {
                    return (
                      <Chip key={index} label={value} />
                    )
                  }
                )}
              </div>
            )}
          >
            { teamData &&
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
                    required = {props.isRequired ? true : false}
                    input={<Input/>}
                    renderValue={(selected) => (
                        <div className={classes.chipWrapper}>
                            {
                                selected.map((value, index) => {
                                const users = userData.data.content.find((users) => users.id === value);
                                if (users) {
                                    return <Chip key={index} label={users.name} />;
                                }
                                return null;
                            })
                            }
                        </div>
                    )}
                >
                    { userData &&
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
              <FormControlLabel value={true} control={<Radio color='primary'/>} label="Active" />
              <FormControlLabel value={false} control={<Radio color='primary'/>} label="Inactive" />
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
                    className={classes.selectEmpty}
                >
                    <option value="">None</option>
                    {
                        departmentData &&
                        departmentData.data.map((manager, index) => {
                            return (
                                <option value={manager.id} key={index}>{manager.name}</option>
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
                    required = {props.isRequired ? true : false}
                    className={`${classes.selectEmpty} ${classes.InputText}`}
                >
                    <option value= "true">True</option>
                    <option value= "false">False</option>
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
    const { label, isRequired } = props;
    return (
      <div className={classes.formElement}>
        <Typography className={classes.typo}>
          {label}
          {isRequired && (
            <span className={classes.requiredElement}>(*)</span>
          )}
        </Typography>
        {checkTypeForInput(props)}
      </div>
    );
  },
);

CustomFormElement.propTypes ={
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