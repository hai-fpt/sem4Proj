import {SET_LEAVE_API} from "../constants/applyLeaveConstants";

export const setLeaveTypes = (data) => {
    return {
        type: SET_LEAVE_API,
        payload: data
    }
};