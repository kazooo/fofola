import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';

import {ServiceContainer} from './ServiceContainer';
import {ServiceCard} from './ServiceCard';
import {Service} from './Service';
import {requestEnvInfo} from './saga';
import {getBuildTime, getCommitId, getGitBranch, getStartupTime, getVersion} from './slice';

export const Home = () => {
    const dispatch = useDispatch();
    const startupTime = useSelector(state => getStartupTime(state));
    const buildTime = useSelector(state => getBuildTime(state));
    const version = useSelector(state => getVersion(state));
    const gitBranch = useSelector(state => getGitBranch(state));
    const commitId = useSelector(state => getCommitId(state));

    useEffect(() => {
        dispatch(requestEnvInfo());
    }, [dispatch]);

    const info = {
        startupTime,
        buildTime,
        version,
        gitBranch,
        commitId,
    };

    return <ServiceContainer title='Fofola' info={info}>
        <ServiceCard title='Zobrazování metadat'>
            <Service link='/uuid-info'> Vypsat základní info o UUID </Service>
            <Service link='/solr-query'> Dotaz na Solr </Service>
            <Service link='/kramerius-processes'> Správa procesů </Service>
            <Service link='/pdf'> Generování PDF </Service>
        </ServiceCard>
        <ServiceCard title='Editace metadat'>
            <Service link='/access'> Změnit viditelnost dokumentů </Service>
            <Service link='/reindex'> Reindexace dokumentů </Service>
            <Service link='/delete'> Mazání dokumentů </Service>
            <Service link='/perio-parts-publish'> Zveřejnit části periodik </Service>
            {/*<Service link='/set-image'> Editace obrázků </Service>*/}
        </ServiceCard>
        <ServiceCard title='Virtuální sbírky'>
            <Service link='/vc'> Založit/editovat virtuální sbírku</Service>
            <Service link='/link-vc'> Přídat/odstranit do/z virtuální sbírky </Service>
            <Service link='/link-donator'> Přídat/odstranit příznak donátoru </Service>
            <Service link='/check-donator'> Kontrola příznaku donátoru </Service>
        </ServiceCard>
        <ServiceCard title='DNNT'>
            <Service link='/dnnt-mark'>Přídat/odstranit DNNT label</Service>
            <Service link='/dnnt-transition'>Přehled interních změn v DNNT labelech</Service>
            <Service link='/dnnt-session'>DNNT processy</Service>
        </ServiceCard>
        <ServiceCard title='Interní věci'>
            <Service link='/internal-processes'> Processy Fofoly </Service>
        </ServiceCard>
    </ServiceContainer>;
};
