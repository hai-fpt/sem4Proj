import { SET_DETAIL_PROFILE } from '../constants/detailProfileConstants';

export const setDetailProfile = (detail) => {
  return {
    type: SET_DETAIL_PROFILE,
    payload: detail,
  };
};
