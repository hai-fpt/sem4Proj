import React from 'react';
import { Switch, Route } from 'react-router-dom';
import NotFound from 'containers/Pages/Standalone/NotFoundDedicated';
import Outer from '../Templates/Outer';
import {
  Login, Register,
  LoginFullstack, RegisterFullstack,
  ResetPassword, ResetPasswordFullstack,
  ComingSoon, Maintenance
} from '../pageListAsync';

function Auth() {
  return (
    <Outer>
      <Switch>
        <Route component={NotFound} />
      </Switch>
    </Outer>
  );
}

export default Auth;
