import React, { useEffect } from 'react';
import { Router, Switch, Route } from 'react-router-dom';
import { PropTypes } from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import  { setEnvData }  from 'enl-redux/actions/envActions';
import { setDetailProfile } from 'enl-redux/actions/detailProfileActions'
import NotFound from '../Pages/Standalone/NotFoundDedicated';
import LoginDedicated from '../Pages/Standalone/LoginDedicated';
import Application from './Application';
import ThemeWrapper from './ThemeWrapper';
import { fetchProfile } from 'enl-api/user/myProfile';
import axios from 'axios';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { CustomNotification, WarningDialog } from 'enl-components';

window.__MUI_USE_NEXT_TYPOGRAPHY_VARIANTS__ = true;

function App(props) {
  const { history } = props;
  const dispatch = useDispatch();
  const envData = useSelector(state => state.env);
  const userDetail = JSON.parse(localStorage.getItem('userDetail'));

  useEffect(() => {
    const fetchEnvData = async () => {
      try {
        const response = await axios.get('/api/env');
        await dispatch(setEnvData(response.data));
      } catch (error) {
        console.error('Failed to fetch environment data:', error);
      }
    };
    fetchEnvData();
  }, [dispatch]);

  useEffect(() => {
    if (!localStorage.getItem('userDetail') && !localStorage.getItem('jwtToken')) {
      history.push('/');
      return;
    }
    const getDetailProfile = async () => {
      try {
        const detail = await fetchProfile(userDetail.id, envData.BASE_API_URL);
        if (envData.BASE_API_URL) {
          await dispatch(setDetailProfile(detail.data));
        }
      } catch (error) {
        throw Error('error fetch detail profile!', error)
      }
    };
    getDetailProfile();
  }, [envData, dispatch, userDetail]);


  return (
    <GoogleOAuthProvider clientId={envData.GOOGLE_CLIENT_ID}>
      <ThemeWrapper>
        <Router history={history}>
          <Switch>
            <Route path="/" exact component={LoginDedicated} />
            <Route path="/app" component={Application} />
            <Route component={NotFound} />
          </Switch>
        </Router>
      </ThemeWrapper>
      {/* <WarningDialog open={dialogOpen}
      dialogMessage={'Are you sure change your profile information?'}
      onClose={handleCancel}
      onConfirm={handleConfirm}
      />

      <CustomNotification
        open={openNotification}
        close={() => {
          setOpenNotification(false);
        }}
        notificationMessage={notification}
        severity={severity ? severity : 'success'}
      /> */}
    </GoogleOAuthProvider>
  );
}

App.propTypes = {
  history: PropTypes.object.isRequired,
};


export default App;
