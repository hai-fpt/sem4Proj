import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import classNames from 'classnames';
import Typography from '@material-ui/core/Typography';
import Hidden from '@material-ui/core/Hidden';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import FullscreenOutlined from '@material-ui/icons/FullscreenOutlined';
import FullscreenExitOutlined from '@material-ui/icons/FullscreenExitOutlined';
import InvertColors from '@material-ui/icons/InvertColorsOutlined';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import MenuIcon from '@material-ui/icons/Menu';
import Button from '@material-ui/core/Button';
import { NavLink } from 'react-router-dom';
import brand from 'enl-api/dummy/brand';
import logo from 'enl-images/logo.svg';
import { injectIntl, FormattedMessage } from 'react-intl';
import menuMessages from 'enl-api/ui/menuMessages';
import SelectLanguage from '../SelectLanguage';
import messages from './messages';
import styles from './header-jss';
import { WarningDialog, CustomNotification } from 'enl-components';

const elem = document.documentElement;

function Header(props) {
  const {
    changeMode,
    classes,
    toggleDrawerOpen,
    margin,
    mode,
    title,
    history,
    dense,
    intl
  } = props;
  const [open] = useState(false);
  const [fullScreen, setFullScreen] = useState(false);
  const [turnDarker, setTurnDarker] = useState(false);
  const [showTitle, setShowTitle] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [openNotification, setOpenNotification] = useState(false);
  // Initial header style
  let flagDarker = false;
  let flagTitle = false;

  const handleScroll = () => {
    const doc = document.documentElement;
    const scroll = (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);
    const newFlagDarker = (scroll > 30);
    const newFlagTitle = (scroll > 40);
    if (flagDarker !== newFlagDarker) {
      setTurnDarker(newFlagDarker);
      flagDarker = newFlagDarker;
    }
    if (flagTitle !== newFlagTitle) {
      setShowTitle(newFlagTitle);
      flagTitle = newFlagTitle;
    }
  };

  const openFullScreen = () => {
    setFullScreen(true);
    if (elem.requestFullscreen) {
      elem.requestFullscreen();
    } else if (elem.mozRequestFullScreen) {
      /* Firefox */
      elem.mozRequestFullScreen();
    } else if (elem.webkitRequestFullscreen) {
      /* Chrome, Safari & Opera */
      elem.webkitRequestFullscreen();
    } else if (elem.msRequestFullscreen) {
      /* IE/Edge */
      elem.msRequestFullscreen();
    }
  };

  const closeFullScreen = () => {
    setFullScreen(false);
    if (document.exitFullscreen) {
      document.exitFullscreen();
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) {
      document.webkitExitFullscreen();
    } else if (document.msExitFullscreen) {
      document.msExitFullscreen();
    }
  };

  const turnMode = newMode => {
    if (newMode === 'light') {
      changeMode('dark');
    } else {
      changeMode('light');
    }
  };

  const handleSignOut = () => {
    setDialogOpen(true)
  }

  const handleSignOutCancel = () => {
    setDialogOpen(false)
  }

  const handleSignOutConfirm = async () => {
    setDialogOpen(false)
    localStorage.clear();
    setOpenNotification(true)
    setTimeout(() => {
      history.push('/');
    }, 2000);
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  return (
    <AppBar
      className={classNames(
        classes.appBar,
        classes.floatingBar,
        margin && classes.appBarShift,
        turnDarker && classes.darker,
      )}
    >
      <Toolbar disableGutters={!open}>
        <div className={classNames(classes.brandWrap, dense && classes.dense)}>
          <span>
            <IconButton
              className={classes.menuButton}
              aria-label="Menu"
              onClick={toggleDrawerOpen}
            >
              <MenuIcon />
            </IconButton>
          </span>
          <Hidden smDown>
            <NavLink to="/app" className={classNames(classes.brand, classes.brandBar)}>
              <img src={logo} alt={brand.name} />
              {intl.formatMessage(messages.brandName)}
            </NavLink>
          </Hidden>
        </div>
        <Hidden smDown>
          <div className={classes.headerProperties}>
            <div
              className={classNames(
                classes.headerAction,
                showTitle && classes.fadeOut,
              )}
            >
              {fullScreen ? (
                <Tooltip title={intl.formatMessage(messages.fullScreen)} placement="bottom">
                  <IconButton
                    className={classes.button}
                    onClick={closeFullScreen}
                  >
                    <FullscreenExitOutlined />
                  </IconButton>
                </Tooltip>
              ) : (
                <Tooltip title={intl.formatMessage(messages.exitFullScreen)} placement="bottom">
                  <IconButton
                    className={classes.button}
                    onClick={openFullScreen}
                  >
                    <FullscreenOutlined />
                  </IconButton>
                </Tooltip>
              )}
              <Tooltip title={intl.formatMessage(messages.lamp)} placement="bottom">
                <IconButton
                  className={classes.button}
                  onClick={() => turnMode(mode)}
                >
                  <InvertColors />
                </IconButton>
              </Tooltip>
            </div>
            <Typography
              component="h2"
              className={classNames(
                classes.headerTitle,
                showTitle && classes.show,
              )}
            >
              {menuMessages[title] !== undefined ? <FormattedMessage {...menuMessages[title]} /> : title}
            </Typography>
          </div>
        </Hidden>
        {/* <div className={classes.searchWrapper}>
          <div className={classes.wrapper}>
            <div className={classes.search}>
              <SearchIcon />
            </div>
            <SearchUi history={history} />
          </div>
        </div> */}
        <Hidden xsDown>
          <span className={classes.separatorV} />
        </Hidden>
        <div className={classes.userToolbar}>
          <SelectLanguage />
          <Button
              color="primary"
              className={classes.buttonTop}
              variant="contained"
              onClick={handleSignOut}
              >
              <ExitToAppIcon />
            <Typography component={'strong'}>{intl.formatMessage(messages.signOutButton)}</Typography>
          </Button>
        </div>
      </Toolbar>
      <WarningDialog open={dialogOpen}
      dialogMessage={intl.formatMessage(messages.logoutConfirm)}
      onClose={handleSignOutCancel}
      onConfirm={handleSignOutConfirm}
      />
      <CustomNotification
        open={openNotification}
        close={() => {setOpenNotification(false)}}
        notificationMessage={intl.formatMessage(messages.logoutSuccess)}
        severity={'success'}
      />
    </AppBar>
  );
}

Header.propTypes = {
  classes: PropTypes.object.isRequired,
  toggleDrawerOpen: PropTypes.func.isRequired,
  avatar: PropTypes.string.isRequired,
  margin: PropTypes.bool.isRequired,
  isLogin: PropTypes.bool,
  dense: PropTypes.bool,
  mode: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  changeMode: PropTypes.func.isRequired,
  openGuide: PropTypes.func.isRequired,
  signOut: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
  intl: PropTypes.object.isRequired
};

Header.defaultProps = {
  dense: false,
  isLogin: false
};

export default withStyles(styles)(injectIntl(Header));
