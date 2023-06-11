import {useDispatch, useSelector} from 'react-redux';
import {closeAlertWindow, getOpenAlert} from './slice';
import {
    Box,
    Grid,
    Modal,
    Typography
} from '@material-ui/core';
import {useKeyPress} from 'effects/useKeyPress';
import {useClickAway} from '@uidotdev/usehooks';
import {AlertField, AlertIssueTypeTranslationKey, AlertParameter, AlertType, DocumentField} from './constants';
import React, {useState} from 'react';
import i18n from 'utils/i18n';
import {Trans} from 'react-i18next';
import {VerticalTable} from 'components/table/VerticalTable';
import {documentDetailsColumns, sessionDetailsColumns} from './table';
import {LabeledBox} from 'components/surface/LabeledBox';
import {CloseIconButton} from 'components/button/iconbuttons';
import {DoneButton} from 'components/button';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import {solveAlert} from './saga';
import {isNullOrUndefined} from '../utils';
import {ModalWrapper} from "components/form/ModalWrapper";

const documentDetailsStyle = {
    height: '312px',
}

const sessionDetailsStyle = {
    height: '312px',
}

const explanationStyle = {
    height: '71px',
}

export const AlertWindow = ({classes}) => {
    const dispatch = useDispatch();

    const alert = useSelector(getOpenAlert);
    const parameters = alert?.[AlertField.Parameters];
    const issueTypeTranslationKey = AlertIssueTypeTranslationKey[alert.issueType];
    const document = alert?.[AlertField.Document];
    const session = alert?.[AlertField.Session];

    const outsideClickRef = useClickAway(() => {
        dispatch(closeAlertWindow());
    });

    const [alertWindowRef, setAlertWindowRef] = useState(outsideClickRef);

    useKeyPress('Escape', (e) => {
        e.preventDefault();
        dispatch(closeAlertWindow());
    });

    const alertTitle = (
        <Typography variant={'h4'}>
            {i18n.t(`constant.sugo.alert.issueType.${issueTypeTranslationKey}.title`, parameters)}
        </Typography>
    );

    const createAlertIcon = () => {
        const Icon = alert?.[AlertField.Type] === AlertType.Error ? ErrorOutlineIcon : WarningAmberIcon;
        const color = alert?.[AlertField.Type] === AlertType.Error ? 'error' : 'warning';
        return <Icon fontSize={'large'} color={color} />;
    };

    const fullDescription = (
        <LabeledBox label={'feature.dnntAlerts.alert.section.description.title'} boxStyle={explanationStyle}>
            <Trans
                i18n={i18n}
                i18nKey={`constant.sugo.alert.issueType.${issueTypeTranslationKey}.fullDescription`}
                values={parameters}
            />
        </LabeledBox>
    );

    const possibleReasons = (
        <LabeledBox label={'feature.dnntAlerts.alert.section.possibleReasons.title'} boxStyle={explanationStyle}>
            <Trans
                i18n={i18n}
                i18nKey={`constant.sugo.alert.issueType.${issueTypeTranslationKey}.possibleReasons`}
                values={parameters}
            />
        </LabeledBox>
    );

    const possibleSolutions = (
        <LabeledBox label={'feature.dnntAlerts.alert.section.possibleSolutions.title'} boxStyle={explanationStyle}>
            <Trans
                i18n={i18n}
                i18nKey={`constant.sugo.alert.issueType.${issueTypeTranslationKey}.possibleSolutions`}
                values={parameters}
            />
        </LabeledBox>
    );

    const createDocumentDetails = () => {
        const failed = !isNullOrUndefined(parameters?.[DocumentField.Uuid]) && isNullOrUndefined(document);
        return (
            <LabeledBox label={'feature.dnntAlerts.alert.section.documentDetails.title'} boxStyle={documentDetailsStyle}>
                <VerticalTable
                    columns={documentDetailsColumns}
                    rows={isNullOrUndefined(document) ? [] : [document]}
                    notFoundLabel={'feature.dnntAlerts.alert.section.documentDetails.notFound'}
                    errorLabel={'feature.dnntAlerts.alert.section.documentDetails.error'}
                    failed={failed}
                />
            </LabeledBox>
        );
    }

    const createSessionDetails = () => {
        const failed = !isNullOrUndefined(parameters?.[AlertParameter.SessionId]) && isNullOrUndefined(session);
        return (
            <LabeledBox label={'feature.dnntAlerts.alert.section.sessionDetails.title'} boxStyle={sessionDetailsStyle}>
                <VerticalTable
                    columns={sessionDetailsColumns}
                    rows={isNullOrUndefined(session) ? [] : [session]}
                    notFoundLabel={'feature.dnntAlerts.alert.section.sessionDetails.notFound'}
                    errorLabel={'feature.dnntAlerts.alert.section.sessionDetails.error'}
                    failed={failed}
                />
            </LabeledBox>
        )
    };

    const stackTrace = (
        <LabeledBox
            label={'feature.dnntAlerts.alert.section.stackTrace.title'}
            boxStyle={{ height: '270px' }}
            contentStyle={{ textAlign: 'left' }}
        >
            {
                parameters?.[AlertParameter.ErrorMessageStackTrace]?.split('\n')
                    .filter((line) => line)
                    .map((line, index) => (
                        <div key={index}>
                            {index !== 0 && <>&nbsp;&nbsp;&nbsp;&nbsp;</>}
                            {line}
                            <br />
                        </div>
                    ))
            }
        </LabeledBox>
    );

    const createSolveButton = () => {
        const disabled = alert?.[AlertField.Solved] === true;
        const tooltip = disabled ?
            'feature.dnntAlerts.alert.button.solve.alreadySolved' :
            'feature.dnntAlerts.alert.button.solve.tooltip';
        const solve = disabled ? null : () => dispatch(solveAlert(alert.id));
        return (
            <ModalWrapper
                title={'common.title.warning'}
                titleColor={'secondary'}
                description={'feature.dnntAlerts.alert.button.solve.question'}
                okMsg={'common.yes'}
                cancelMsg={'common.no'}
                onOk={solve}
                onOpen={() => setAlertWindowRef(null)}
                onClose={() => setAlertWindowRef(outsideClickRef)}
            >
                <DoneButton
                    disabled={disabled}
                    label={'feature.dnntAlerts.alert.button.solve.title'}
                    tooltip={tooltip}
                    variant={'outlined'}
                />
            </ModalWrapper>
        );
    };

    return (
      <Modal
          open={true}
          aria-labelledby='modal-modal-title'
          aria-describedby='modal-modal-description'
      >
          <Box className={classes.alert} ref={alertWindowRef}>
              <Box className={classes.alertBox}>
                  <Grid container direction={'column'} spacing={3}>
                      <Grid item container direction={'row'} justifyContent={'space-between'}>
                          <Grid item xs={6} container direction={'row'} spacing={1}>
                              <Grid item>
                                  {createAlertIcon()}
                              </Grid>
                              <Grid item>
                                  {alertTitle}
                              </Grid>
                          </Grid>
                          <Grid item xs={6} container direction={'row'} spacing={3} justifyContent={'flex-end'}>
                              <Grid item>
                                  {createSolveButton()}
                              </Grid>
                              <Grid item>
                                  <CloseIconButton onClick={() => dispatch(closeAlertWindow())} />
                              </Grid>
                          </Grid>
                      </Grid>
                      <Grid item container direction={'row'} justifyContent={'space-between'}>
                          <Grid item xs={6} container direction={'column'} spacing={3}>
                              <Grid item>
                                  {fullDescription}
                              </Grid>
                              <Grid item>
                                  {possibleReasons}
                              </Grid>
                              <Grid item>
                                  {possibleSolutions}
                              </Grid>
                          </Grid>
                          <Grid item xs={6} container direction={'row'} columns={12} spacing={3} justifyContent={'flex-end'}>
                              <Grid item xs={6}>
                                  {createDocumentDetails()}
                              </Grid>
                              <Grid item xs={6}>
                                  {createSessionDetails()}
                              </Grid>
                          </Grid>
                      </Grid>
                      { stackTrace && (
                          <Grid item>
                              {stackTrace}
                          </Grid>
                      )}
                  </Grid>
              </Box>
          </Box>
      </Modal>
    );
};
