import React from "react";
import {ServiceContainer} from "../components/service/ServiceContainer";
import {ServiceCard} from "../components/service/ServiceCard";
import {Service} from "../components/service/Service";

export const Home = () => (
    <ServiceContainer title="Fofola">
        <ServiceCard title="Zobrazování metadat">
            <Service link="/uuid-info"> Vypsat základní info o UUID </Service>
            <Service link="/tree"> Zobrazit strom dokumentu </Service>
            <Service link="/solr-query"> Dotaz na Solr </Service>
            <Service link="/kramerius-processes"> Správa procesů </Service>
            <Service link="/pdf"> Generování PDF </Service>
        </ServiceCard>
        <ServiceCard title="Editace metadat">
            <Service link="/access"> Změnit viditelnost dokumentů </Service>
            <Service link="/reindex"> Reindexace dokumentů </Service>
            <Service link="/delete"> Mazání dokumentů </Service>
            <Service link="/perio-parts-publish"> Zveřejnit části periodik </Service>
            <Service link="/img-editing"> Editace obrázků </Service>
        </ServiceCard>
        <ServiceCard title="Virtuální sbírky">
            <Service link="/link-vc"> Přídat/odstranit do/z virtuální sbírky </Service>
            <Service link="/link-donator"> Přídat/odstranit příznak donátoru </Service>
            <Service link="/check-donator"> Kontrola příznaku donátoru </Service>
        </ServiceCard>
        <ServiceCard title="Interní věci">
            <Service link="/internal-processes"> Processy Fofoly </Service>
        </ServiceCard>
    </ServiceContainer>
);
