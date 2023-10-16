import { SET_API_DATA } from '../constants/teamConstants';

const initialState = [];

const teamReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_API_DATA:
      return {
        ...state,
        ...action.payload,
      };
    default:
      return state;
  }
};

export default teamReducer;
