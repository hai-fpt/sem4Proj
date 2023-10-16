import {SET_API_DATA_MANAGER} from "../constants/managerConstants";


const initialState = [];

const managerReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_API_DATA_MANAGER:
            return {
                ...state,
                ...action.payload,
            };
        default:
            return state;
    }
};

export default managerReducer;
