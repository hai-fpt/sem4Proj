import React, {
    useRef,
    useEffect,
    useState
} from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import classNames from 'classnames';
import Avatar from '@material-ui/core/Avatar';
import {injectIntl} from 'react-intl';
import MainMenu from './MainMenu';
import styles from './sidebar-jss';
import {useSelector} from "react-redux";
import {Dialog, DialogContent} from '@material-ui/core'
import UploadAvatar from 'enl-components/UploadAvatar'

function SidebarContent(props) {
    const {
        classes,
        drawerPaper,
        toggleDrawerOpen,
        loadTransition,
        leftSidebar,
        dataMenu,
        userAttr
    } = props;
    const [transform, setTransform] = useState(0);
    const refSidebar = useRef(null);
    const userDetail = useSelector(state => state.detailProfile)
    const [userName, setUserName] = useState('')
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const [openUploadAvatar, setOpenUploadAvatar] = useState(false)
    // const setStatus = st => {
    //   switch (st) {
    //     case 'online':
    //       return classes.online;
    //     case 'idle':
    //       return classes.idle;
    //     case 'bussy':
    //       return classes.bussy;
    //     default:
    //       return classes.offline;
    //   }
    // };

    const handleScroll = (event) => {
        setTransform(event.target.scrollTop);
    };

    useEffect(() => {
        refSidebar.current.addEventListener('scroll', handleScroll);
        if (userDetail) {
            setUserName(userDetail.name);
        }
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };

    }, [userName]);
    return (
        <div className={classNames(classes.drawerInner, !drawerPaper ? classes.drawerPaperClose : '')}>
            <div className={classes.drawerHeader}>
                <div
                    className={classes.profile}
                    style={{opacity: 1 - (transform / 100), marginTop: transform * -0.3}}
                >
                    <Avatar
                        alt={userName}
                        src={userDetail?.avatar ? `${baseApiUrl}/api/${userDetail?.avatar?.path}` : null}
                        className={classNames(classes.avatar, classes.bigAvatar)}
                        onClick={() => {
                            setOpenUploadAvatar(true)
                        }}
                    />
                    <div>
                        {userName ? (
                            <p>{userName}</p>
                        ) : (
                            <p>{userDetail.name}</p>
                        )}
                        {/* <Button size="small" onClick={openMenuStatus}>
              <i className={classNames(classes.dotStatus, setStatus(status))} />
              <FormattedMessage {...messages[status]} />
            </Button>
            <Menu
              id="status-menu"
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={closeMenuStatus}
              className={classes.statusMenu}
            >
              <MenuItem onClick={() => changeStatus('online')}>
                <i className={classNames(classes.dotStatus, classes.online)} />
                <FormattedMessage {...messages.online} />
              </MenuItem>
              <MenuItem onClick={() => changeStatus('idle')}>
                <i className={classNames(classes.dotStatus, classes.idle)} />
                <FormattedMessage {...messages.idle} />
              </MenuItem>
              <MenuItem onClick={() => changeStatus('bussy')}>
                <i className={classNames(classes.dotStatus, classes.bussy)} />
                <FormattedMessage {...messages.bussy} />
              </MenuItem>
              <MenuItem onClick={() => changeStatus('offline')}>
                <i className={classNames(classes.dotStatus, classes.offline)} />
                <FormattedMessage {...messages.offline} />
              </MenuItem>
            </Menu> */}
                    </div>
                </div>
            </div>
            <div
                id="sidebar"
                ref={refSidebar}
                className={
                    classNames(
                        classes.menuContainer,
                        leftSidebar && classes.rounded,
                        classes.withProfile
                    )
                }
            >
                <MainMenu loadTransition={loadTransition} dataMenu={dataMenu} toggleDrawerOpen={toggleDrawerOpen}/>
            </div>
            {
                openUploadAvatar &&
                <UploadAvatar open={openUploadAvatar} onClose={() => setOpenUploadAvatar(false)}/>
            }

        </div>
    );
}

SidebarContent.propTypes = {
    classes: PropTypes.object.isRequired,
    userAttr: PropTypes.object.isRequired,
    drawerPaper: PropTypes.bool.isRequired,
    toggleDrawerOpen: PropTypes.func,
    loadTransition: PropTypes.func,
    leftSidebar: PropTypes.bool.isRequired,
    dataMenu: PropTypes.array.isRequired,
    status: PropTypes.string.isRequired,
    anchorEl: PropTypes.object,
    openMenuStatus: PropTypes.func.isRequired,
    closeMenuStatus: PropTypes.func.isRequired,
    changeStatus: PropTypes.func.isRequired,
};

SidebarContent.defaultProps = {
    toggleDrawerOpen: () => {
    },
    toggleDrawerClose: () => {
    },
    loadTransition: () => {
    },
    anchorEl: null,
};

export default withStyles(styles)(injectIntl(SidebarContent));
