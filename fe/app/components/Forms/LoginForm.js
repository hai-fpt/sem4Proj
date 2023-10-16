import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Hidden from '@material-ui/core/Hidden';
import { reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import brand from 'enl-api/dummy/brand';
import logo from 'enl-images/logo.svg';
import { injectIntl, FormattedMessage } from 'react-intl';
import { closeMsgAction } from 'enl-redux/actions/authActions';
import messages from './messages';
import styles from './user-jss';
import { GoogleLogin } from '@react-oauth/google';
import { fetchVerify, postLogin } from 'enl-api/login/googleLoginAPI';
import { useSelector } from 'react-redux';
import jwtDecode from 'jwt-decode';
import { useHistory } from "react-router-dom";

function LoginForm(props) {
  const {
    classes
  } = props;
  const history = useHistory();
  const baseApiUrl = useSelector(state => state.env.BASE_API_URL);

  useEffect(() => {
  }, [baseApiUrl]);

  const handleGoogleLoginSuccess = async (response) => {
    try {
      const verifyResponse =  await fetchVerify(response.credential, baseApiUrl);
      
      if (verifyResponse.status === 200) {
        localStorage.setItem('jwtToken', verifyResponse.data);
        const loginResponse = await postLogin(jwtDecode(response.credential), baseApiUrl);

        if (loginResponse.status === 201) {
          localStorage.setItem('userDetail', JSON.stringify(loginResponse.data));
          setTimeout(() => {
            history.push("/app/pages/user/profile");
          }, 1500);
        }
        else if (loginResponse.status === 200) {
          localStorage.setItem('userDetail', JSON.stringify(loginResponse.data));
          setTimeout(() => {
            history.push("/app/pages/my%20leave");
          }, 1500);
        }
      }
    } catch (error) {
      console.error('Error verifying:', error.message);
    }
  };

  const handleGoogleLoginFailure = (error) => {
    alert(error);
  };

  return (
    <Paper className={classes.sideWrap}>
      <Hidden mdUp>
        <div className={classes.headLogo}>
          <div className={classes.brand}>
            <img src={logo} alt={brand.name} />
            {brand.name}
          </div>
        </div>
      </Hidden>
      <div className={classes.topBar}>
        <Typography variant="h4" className={classes.title}>
          <FormattedMessage {...messages.login} />
        </Typography>
      </div>
      <GoogleLogin
        onSuccess={handleGoogleLoginSuccess}
        onError={handleGoogleLoginFailure}
        size='large'
        width='250'
        shape='pill'
      />
    </Paper>
  );
}

LoginForm.propTypes = {
  classes: PropTypes.object.isRequired,
  loading: PropTypes.bool,
};

LoginForm.defaultProps = {
  loading: false
};

const LoginFormReduxed = reduxForm({
  form: 'loginForm',
  enableReinitialize: true,
})(LoginForm);

const mapDispatchToProps = {
  closeMsg: closeMsgAction
};

const mapStateToProps = state => ({
  messagesAuth: state.authReducer.message,
  loading: state.authReducer.loading
});

const LoginFormMapped = connect(
  mapStateToProps,
  mapDispatchToProps
)(LoginFormReduxed);

export default withStyles(styles)(injectIntl(LoginFormMapped));
