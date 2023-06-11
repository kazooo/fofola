import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';

import {ServiceContainer} from './ServiceContainer';
import {ServiceCard} from './ServiceCard';
import {Service} from './Service';
import {requestAlertStats, requestEnvInfo} from './saga';
import {getAppInfo} from './slice';
import {sections} from './services';

export const Home = () => {
    const dispatch = useDispatch();
    const appInfo = useSelector(getAppInfo);

    useEffect(() => {
        dispatch(requestEnvInfo());
        dispatch(requestAlertStats());
    }, [dispatch]);

    return (
        <ServiceContainer title='home.title' info={appInfo}>
            {sections.map((section, i) => (
                <ServiceCard key={i} title={section.name}>
                    {section.services.map((service, j) => (
                            <Service key={j} title={service.name} link={service.route} />
                        ))
                    }
                </ServiceCard>
            ))}
        </ServiceContainer>
    );
};
