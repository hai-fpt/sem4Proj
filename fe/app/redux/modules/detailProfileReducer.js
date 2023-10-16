import { SET_DETAIL_PROFILE } from '../constants/detailProfileConstants';

const initialState = {
};

const envDataReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_DETAIL_PROFILE:
      return {
        ...state,
        ...action.payload,
      };
    default:
      return state;
  }
};

export default envDataReducer;
