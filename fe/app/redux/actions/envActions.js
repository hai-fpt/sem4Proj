import { SET_ENV_DATA } from '../constants/envActionConstants';

export const setEnvData = (envData) => {
  return {
    type: SET_ENV_DATA,
    payload: envData,
  };
};
