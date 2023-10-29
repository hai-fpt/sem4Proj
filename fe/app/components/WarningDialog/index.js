import React from 'react';
import PropTypes from 'prop-types';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Button from '@material-ui/core/Button';
import {injectIntl} from 'react-intl'
import messages from "./messages";
function WarningDialog({ open, onClose, onConfirm, dialogMessage ,intl}) {
  const handleClose = () => {
    onClose();
  };

  const handleConfirm = () => {
    onConfirm();
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{intl.formatMessage(messages.title)}</DialogTitle>
      <DialogContent>
        {dialogMessage}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">
            {intl.formatMessage(messages.close)}
        </Button>
        <Button onClick={handleConfirm} color="primary" autoFocus>
            {intl.formatMessage(messages.confirm)}
        </Button>
      </DialogActions>
    </Dialog>
  );
}

WarningDialog.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
  dialogMessage: PropTypes.any.isRequired,
};

export default injectIntl(WarningDialog)
