import { SET_ENV_DATA } from '../constants/envActionConstants';

const initialState = {
  data: null,
  isLoading: false,
};

const envDataReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_ENV_DATA:
      return {
        ...state,
        ...action.payload,
      };
    default:
      return state;
  }
};

export default envDataReducer;
