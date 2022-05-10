import {Box, Button, Grid, Modal, Typography} from '@material-ui/core';
import {useTranslation} from 'react-i18next';
import React, {useState} from 'react';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '1px solid rgb(212, 101, 3)',
    borderRadius: '5px',
    boxShadow: 24,
    p: 4,
};

export const ModalWrapper = ({children, callback, title, titleColor, description, okMsg, cancelMsg}) => {
    const [open, setOpen] = useState(false);
    const {t} = useTranslation();

    const openModal = () => setOpen(true);
    const closeModal = () => setOpen(false);

    const handleOk = () => {
        callback();
        closeModal();
    }

    const handleCancel = () => closeModal();

    return (
        <div>
            {
                React.cloneElement(
                    children,
                    {onClick: openModal}
                )
            }
            <Modal
                open={open}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
            >
                <Box sx={style}>
                    <Grid container direction={'column'} spacing={3}>
                        <Grid item>
                            <Typography id='modal-modal-title' color={titleColor} align={'center'}>
                                {t(title)}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Typography id='modal-modal-description' align={'center'}>
                                {t(description)}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Grid container direction={'row'}>
                                <Grid item xs={6} align='center'>
                                    <Button onClick={handleCancel} color={'primary'}>
                                        {t(cancelMsg)}
                                    </Button>
                                </Grid>
                                <Grid item xs={6} align='center'>
                                    <Button onClick={handleOk} color={'primary'}>
                                        {t(okMsg)}
                                    </Button>
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </Box>
            </Modal>
        </div>
    );
};
