import {SET_LEAVE_API} from "../constants/applyLeaveConstants";

const initialState = [];

const applyLeaveApiReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_LEAVE_API:
        return {
          ...state,
          ...action.payload
        }
        default:
            return state;
    }
};

export default applyLeaveApiReducer;