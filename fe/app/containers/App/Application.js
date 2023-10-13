import React, { useContext } from 'react';
import { PropTypes } from 'prop-types';
import { Switch, Route } from 'react-router-dom';
import Dashboard from '../Templates/Dashboard';
import { AppContext } from './ThemeWrapper';
import {
  DashboardPage,
  BlankPage,
  Error,
  NotFound,
  Form,
  Table,
  Parent,
  Holiday,
} from "../pageListAsync";

function Application(props) {
  const { history } = props;
  const changeMode = useContext(AppContext);

  return (
    <Dashboard history={history} changeMode={changeMode}>
      <Switch>
        {/* Home */}
        <Route exact path="/app" component={BlankPage} />
        <Route path="/app/pages/dashboard" component={DashboardPage} />
        <Route path="/app/pages/form" component={Form} />
        <Route path="/app/pages/table" component={Table} />
        <Route path="/app/pages/page-list" component={Parent} />
        <Route path="/app/pages/pages/not-found" component={NotFound} />
        <Route path="/app/pages/pages/error" component={Error} />

        {/* New Design */}
        <Route path="/app/pages/home" component={DashboardPage} />
        <Route path="/app/pages/user" component={Table} />
        <Route path="/app/pages/add/user" component={Form} />
        <Route path="/app/pages/import/user" component={DashboardPage} />
        <Route path="/app/pages/leave/manage" component={Table} />
        <Route path="/app/pages/leave/apply" component={DashboardPage} />

        {/* Holiday management */}
        <Route path="/app/pages/holidays" component={Holiday}/>

        <Route component={NotFound} />
      </Switch>
    </Dashboard>
  );
}

Application.propTypes = {
  history: PropTypes.object.isRequired,
};

export default Application;
