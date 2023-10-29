import { SET_DAY_LEAVE, RESET_LEAVE_STATE } from '../constants/leaveManageConstants';

const initialState = null;

const leaveManageReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_DAY_LEAVE:
      return {
        ...state,
        ...action.payload,
      };
    case RESET_LEAVE_STATE:
      return initialState;
    default:
      return state;
  }
};

export default leaveManageReducer;