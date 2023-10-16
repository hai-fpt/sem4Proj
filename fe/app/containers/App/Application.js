import React, { useContext } from 'react';
import { PropTypes } from 'prop-types';
import { Switch, Route, Redirect } from 'react-router-dom';
import Dashboard from '../Templates/Dashboard';
import { AppContext } from './ThemeWrapper';
import {
  DashboardPage,
  BlankPage,
  Error,
  NotFound,
  Form,
  Table,
  Holiday,
  UserProfile,
  Team,
  Roles,
  LeaveType,
  Users,
  ApplyLeave,
  MyLeave,
  LeaveManagement,
  Department,
} from "../pageListAsync";

function Application(props) {
  const { history } = props;
  const changeMode = useContext(AppContext);

  return (
    <Dashboard history={history} changeMode={changeMode}>
      <Switch>
        {/* Home */}
        <Redirect exact path="/app" to="/app/pages/my leave" component={BlankPage} />
        <Route path="/app/pages/pages/not-found" component={NotFound} />
        <Route path="/app/pages/pages/error" component={Error} />

        {/* New Design */}
        <Route path="/app/pages/users" component={Users} />
        <Route path="/app/pages/user/profile" component={UserProfile} />
        <Route path="/app/pages/add/user" component={Form} />
        <Route path="/app/pages/import/user" component={DashboardPage} />
        <Route path="/app/pages/leave/manage" component={LeaveManagement} />
        <Route path="/app/pages/leave/apply" component={ApplyLeave} />

        {/* System information */}
        <Route path="/app/pages/holidays" component={Holiday}/>
        <Route path="/app/pages/team" component={Team}/>
        <Route path="/app/pages/roles" component={Roles}/>
        <Route path="/app/pages/leave type" component={LeaveType}/>
        <Route path="/app/pages/my leave" component={MyLeave}/>
        <Route path="/app/pages/department" component={Department}/>

        <Route component={NotFound} />
      </Switch>
    </Dashboard>
  );
}

Application.propTypes = {
  history: PropTypes.object.isRequired,
};

export default Application;
