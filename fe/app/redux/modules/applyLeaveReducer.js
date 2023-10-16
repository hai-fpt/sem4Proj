import produce from "immer";
import {
  FETCH_LEAVES,
  CREATE_LEAVE,
  UPDATE_LEAVE,
  DELETE_LEAVE,
} from "../constants/applyLeaveConstants";

const initialState = {
  formValues: {},
  leaves: [],
};

/* eslint-disable default-case, no-param-reassign */
const applyLeaveReducer = (state = initialState, action = {}) =>
  produce(state, (draft) => {
    switch (action.type) {
      case FETCH_LEAVES:
        draft.leaves = action.payload;
        break;
      case CREATE_LEAVE:
      case UPDATE_LEAVE:
      case DELETE_LEAVE:
        draft.formValues = action.data;
        draft.leaves = action.payload;
        break;
      default:
        break;
    }
  });

export default applyLeaveReducer;
