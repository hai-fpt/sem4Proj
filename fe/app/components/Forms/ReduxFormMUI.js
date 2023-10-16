import React from "react";
import PropTypes from "prop-types";
import TextField from "@material-ui/core/TextField";
import Select from "@material-ui/core/Select";
import Checkbox from "@material-ui/core/Checkbox";
import Switch from "@material-ui/core/Switch";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import moment from "moment";

/* Textfield */
export const TextFieldRedux = ({
  meta: { touched, error },
  input,
  ...rest
}) => <TextField {...rest} {...input} error={touched && Boolean(error)} />;

TextFieldRedux.propTypes = {
  input: PropTypes.object.isRequired,
  meta: PropTypes.object,
};

TextFieldRedux.defaultProps = {
  meta: null,
};
/* End */

/* Select */
export const SelectRedux = ({ input, children, ...rest }) => (
  <Select {...input} {...rest}>
    {children}
  </Select>
);

SelectRedux.propTypes = {
  input: PropTypes.object.isRequired,
  children: PropTypes.node.isRequired,
};
/* End */

/* Checkbox */
export const CheckboxRedux = ({ input, ...rest }) => (
  <Checkbox
    checked={input.value === "" ? false : input.value}
    {...input}
    {...rest}
  />
);

CheckboxRedux.propTypes = {
  input: PropTypes.object.isRequired,
};
/* End */

/* Switch */
export const SwitchRedux = ({ input, ...rest }) => (
  <Switch
    checked={input.value === "" ? false : input.value}
    {...input}
    {...rest}
  />
);

SwitchRedux.propTypes = {
  input: PropTypes.object.isRequired,
};
/* End */

/* Date Time Picker */
export const DateTimePickerFieldRedux = ({
  inputFormat,
  dateParser,
  input,
  label,
  meta: { touched, invalid, error },
  dateFormatter,
  ...rest
}) => {
  return (
    <DateTimePicker
      error={touched && invalid}
      // format={inputFormat}
      helperText={touched && error}
      label={label}
      onChange={(val) => input.onChange(val)}
      value={input.value}
      sx={{
        "& .MuiInputAdornment-root": {
          height: "100%",
        },
      }}
      {...rest}
    />
  );
};

DateTimePickerFieldRedux.propTypes = {
  dateFormatter: PropTypes.func,
  dateParser: PropTypes.func,
  input: PropTypes.object.isRequired,
  inputFormat: PropTypes.string,
  label: PropTypes.string.isRequired,
  meta: PropTypes.object.isRequired,
};

// DateTimePickerFieldRedux.defaultProps = {
//   dateFormatter: (v) => v.replace(/-/g, "/"),
//   dateParser: (v) =>
//     v.getFullYear() +
//     "-" +
//     ("0" + (v.getMonth() + 1)).slice(-2) +
//     "-" +
//     ("0" + v.getDate()).slice(-2),
//   inputFormat: "MMMM D, YYYY HH:mm:ss",
// };
/* End */
