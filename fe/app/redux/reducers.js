/**
 * Combine all reducers in this file and export the combined reducers.
 */
import { reducer as form } from 'redux-form';
import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';

// Global Reducers
import languageProviderReducer from 'containers/LanguageProvider/reducer';
import history from '../utils/history';
import authReducer from './modules/authReducer';
import uiReducer from './modules/uiReducer';
import initval from './modules/initFormReducer';
import applyLeaveReducer from './modules/applyLeaveReducer';
import envDataReducer from './modules/envDataReducer';
import teamReducer from './modules/teamReducers'
import detailProfileReducer from './modules/detailProfileReducer'
import departmentReducer from "./modules/departmentReducer";
import managerReducer from "./modules/managerReducers";
import userReducer from "./modules/userReducers";
import applyLeaveApiReducer from "./modules/applyLeaveApiReducer";
import leaveManageReducer from "./modules/leaveManageReducer"

/**
 * Creates the main reducer with the dynamically injected ones
 */
export default function createReducer(injectedReducers = {}) {
  const rootReducer = combineReducers({
    form,
    ui: uiReducer,
    initval,
    authReducer,
    applyLeaveReducer,
    language: languageProviderReducer,
    router: connectRouter(history),
    env: envDataReducer,
    team: teamReducer,
    department: departmentReducer,
    manager: managerReducer,
    users: userReducer,
    manageLeaves:  leaveManageReducer,
    applyLeaveApi: applyLeaveApiReducer,
    detailProfile: detailProfileReducer,
    ...injectedReducers,
  });

  // Wrap the root reducer and return a new root reducer with router state
  const mergeWithRouterState = connectRouter(history);
  return mergeWithRouterState(rootReducer);
}
