import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import classNames from 'classnames';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from "@material-ui/core/Typography";
import Collapse from '@material-ui/core/Collapse';
import Chip from '@material-ui/core/Chip';
import Icon from '@material-ui/core/Icon';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import styles from './sidebar-jss';
import { useSelector } from 'react-redux';


const LinkBtn = React.forwardRef(function LinkBtn(props, ref) { // eslint-disable-line
  return <NavLink to={props.to} {...props} innerRef={ref} />; // eslint-disable-line
});

// eslint-disable-next-line
function MainMenu(props) {
  const userRole = useSelector(state => state.detailProfile.userRoles)?.map(item => item.role.name);
  const {
    classes,
    openSubMenu,
    open,
    dataMenu,
    toggleDrawerOpen,
    loadTransition
  } = props;

  const handleClick = () => {
    toggleDrawerOpen();
    loadTransition(false);
  };

  const getMenus = menuArray => menuArray.map((item, index) => {
    if (!filterRoles(item.roles)) {
      return ;
    }
    if (item.child || item.linkParent) {
      return (
        <div key={index.toString()}>
          <ListItem
            button
            component={LinkBtn}
            to={item.linkParent ? item.linkParent : '#'}
            className={
              classNames(
                classes.head,
                item.icon ? classes.iconed : '',
                open.indexOf(item.key) > -1 ? classes.opened : '',
              )
            }
            onClick={() => openSubMenu(item.key, item.keyParent)}
          >
            {item.icon && (
              <ListItemIcon className={classes.icon}>
                <Icon>{item.icon}</Icon>
              </ListItemIcon>
            )}
            <ListItemText classes={{ primary: classes.primary }} primaryTypographyProps={{ className: classes.primary }}>
              <Typography variant="body1" style={{ fontSize: '14px' }}>
                {item.name}
              </Typography>
            </ListItemText>
            { !item.linkParent && (
              <span>
                { open.indexOf(item.key) > -1 ? <ExpandLess /> : <ExpandMore /> }
              </span>
            )}
          </ListItem>
          { !item.linkParent && (
            <Collapse
              component="div"
              className={classNames(
                (item.keyParent ? classes.child : ''),
              )}
              in={open.indexOf(item.key) > -1}
              timeout="auto"
              unmountOnExit
            >
              <List className={classes.head} component="nav">
                { getMenus(item.child, 'key') }
              </List>
            </Collapse>
          )}
        </div>
      );
    }
    return (
      <ListItem
        key={index.toString()}
        button
        exact
        className={classes.head}
        activeClassName={classes.active}
        component={LinkBtn}
        to={item.link}
        onClick={() => handleClick()}
      > 
        {item.icon && (
          <ListItemIcon className={classes.icon}>
            <Icon>{item.icon}</Icon>
          </ListItemIcon>
        )}
        <ListItemText classes={{ primary: classes.primary }}>
          <Typography variant="body1" style={{ fontSize: '14px' }}>
            {item.name}
          </Typography>
        </ListItemText>
        {item.badge && (
          <Chip color="primary" label={item.badge} className={classes.badge} />
        )}
      </ListItem>
    );
  });

  const filterRoles = (roles) => {
    if (userRole?.includes('ADMIN')) {
      return true;
    }
    else {
      const hasAccess = roles?.every((value) => userRole?.includes(value));
      return hasAccess;
    }
  }

  return (
    <div>
      {getMenus(dataMenu)}
    </div>
  );
}

MainMenu.propTypes = {
  classes: PropTypes.object.isRequired,
  open: PropTypes.array.isRequired,
  openSubMenu: PropTypes.func.isRequired,
  toggleDrawerOpen: PropTypes.func.isRequired,
  loadTransition: PropTypes.func.isRequired,
  dataMenu: PropTypes.array.isRequired,
};

const openAction = (key, keyParent) => ({ type: 'OPEN_SUBMENU', key, keyParent });

const mapStateToProps = state => ({
  open: state.ui.subMenuOpen
});

const mapDispatchToProps = dispatch => ({
  openSubMenu: bindActionCreators(openAction, dispatch)
});

const MainMenuMapped = connect(
  mapStateToProps,
  mapDispatchToProps
)(MainMenu);

export default withStyles(styles)(MainMenuMapped);
