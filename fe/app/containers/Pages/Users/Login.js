import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import brand from 'enl-api/dummy/brand';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography';
import Hidden from '@material-ui/core/Hidden';
import { NavLink } from 'react-router-dom';
import { withStyles } from '@material-ui/core/styles';
import { LoginForm, SelectLanguage } from 'enl-components';
import logo from 'enl-images/logo.svg';
import ArrowBack from '@material-ui/icons/ArrowBack';
import styles from 'enl-components/Forms/user-jss';
import { FormattedMessage } from 'react-intl';
import messages from './messages';
import { loginWithEmail } from '../../../redux/actions/authActions';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

function Login(props) {
  const { classes } = props;
  const title = brand.name + ' - Login';
  const description = brand.desc;
  const [valueForm, setValueForm] = useState(null);

  const submitForm = (values) => setValueForm(values);
  useEffect(() => {
    if (valueForm) {
      console.log(`You submitted:\n\n${valueForm.email}`);
      props.handleLoginWithEmail(valueForm.email, valueForm.password)
      // window.location.href = '/app';
    }
  }, [valueForm]);

  return (
    <div className={classes.rootFull}>
      <Helmet>
        <title>{title}</title>
        <meta name="description" content={description} />
        <meta property="og:title" content={title} />
        <meta property="og:description" content={description} />
        <meta property="twitter:title" content={title} />
        <meta property="twitter:description" content={description} />
      </Helmet>
      <div className={classes.containerSide}>
        <Hidden smDown>
          <div className={classes.opening}>
            <div className={classes.openingWrap}>
              <div className={classes.openingHead}>
                <NavLink to="/" className={classes.brand}>
                  <img src={logo} alt={brand.name} />
                  <FormattedMessage {...messages.brandName}/>
                </NavLink>
              </div>
              <Typography variant="h3" component="h1" gutterBottom>
                <FormattedMessage {...messages.welcomeTitle} />
                &nbsp;
                <FormattedMessage {...messages.brandName}/>
              </Typography>
              <Typography variant="h6" component="p" className={classes.subpening}>
                <FormattedMessage {...messages.welcomeSubtitle} />
              </Typography>
            </div>
            <div className={classes.openingFooter}>
              <div className={classes.lang}>
                <SelectLanguage />
              </div>
            </div>
          </div>
        </Hidden>
        <div className={classes.sideFormWrap}>
          <LoginForm onSubmit={(values) => submitForm(values)} />
        </div>
      </div>
    </div>
  );
}

Login.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapStateToProps = state => ({
  state: state.authReducer
});

const mapDispatchToProps = dispatch => ({
  handleLoginWithEmail: bindActionCreators(loginWithEmail, dispatch)
});

const LoginMapped = connect(
  mapStateToProps,
  mapDispatchToProps
)(Login);
    

export default withStyles(styles)(LoginMapped);
