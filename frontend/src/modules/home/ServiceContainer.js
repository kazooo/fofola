import React from 'react';
import {Box} from '@material-ui/core';
import {useTranslation} from 'react-i18next';

export const ServiceContainer = ({title, info, children}) => (
    <Box>
        <Box sx={{ marginBottom: '5%'}}>
            <Box sx={{ paddingTop: '12%', paddingLeft: '25%', width: 'max-content', float:'left'}}>
                <Title title={title} />
            </Box>
            <Box sx={{ paddingBottom: '16%', paddingLeft: '85%'}}>
                <InfoBlock {...info} />
            </Box>
        </Box>

        <Box>
            <Content content={children} />
        </Box>
    </Box>
);

const Title = ({title}) => {
    const {t} = useTranslation();

    return (
        <Box
            component='h1'
            sx={{
                fontFamily: 'foral_proextrabold, serif',
                textTransform: 'uppercase',
                color: '#6F3317',
            }}
        >
            {t(title)}
        </Box>
    );
};

const InfoBlock = ({startupTime, buildTime, version, gitBranch, commitId}) => (
    <Box sx={{float:'left'}}>
        {
            startupTime && (
                <Box component={'p'}>
                    Startup Time: {startupTime}
                </Box>
            )
        }
        {
            buildTime && (
                <Box component={'p'}>
                    Build Time: {buildTime}
                </Box>
            )
        }
        {
            gitBranch && (
                <Box component={'p'}>
                    Git branch: {gitBranch}
                </Box>
            )
        }
        {
            commitId && (
                <Box component={'p'}>
                    Commit ID: {commitId}
                </Box>
            )
        }
        {
            version && (
                <Box component={'p'}>
                    Version: {version}
                </Box>
            )
        }
    </Box>
);

const Content = ({content}) => (
    <Box sx={{ margin: '3%' }}>
        {content}
    </Box>
);
