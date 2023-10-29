import { SET_DAY_LEAVE, RESET_LEAVE_STATE } from "../constants/leaveManageConstants";

export const setDayLeave = (data) => {
    return {
        type: SET_DAY_LEAVE,
        payload: data
    }
};

export const resetLeaveState = () => {
    return {
      type: RESET_LEAVE_STATE,
    };
};