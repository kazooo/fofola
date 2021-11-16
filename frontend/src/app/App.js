import React from 'react';
import {HashRouter, Route, Switch} from 'react-router-dom'
import {Home} from "./Home";
import {Reindex} from "../modules/reindex/Reindex";
import {Delete} from "../modules/delete/Delete";
import {ChangeAccess} from "../modules/change-access/ChangeAccess";
import {LinkDonator} from "../modules/link-donator/LinkDonator";
import {LinkVc} from "../modules/link-vc/LinkVc";
import {UuidInfo} from "../modules/uuid-info/UuidInfo";
import {KrameriusProcesses} from "../modules/kramerius-procesess/KrameriusProcesses";
import {CheckDonator} from "../modules/check-donator/CheckDonator";
import {InternalProcesses} from "../modules/internal-processes/InternalProcesses";
import {Pdf} from "../modules/pdf/Pdf";
import {SolrQuery} from "../modules/solr-query/SolrQuery";
import {PerioPartsPublish} from "../modules/perio-parts/PerioPartsPublish";
import {SetImage} from "../modules/set-image/SetImage";
import {VcManagement} from "../modules/vc-management/VcManagement";
import {LinkDnnt} from "../modules/link-dnnt/LinkDnnt";
import {Error404} from "../modules/404/Error404";

export const App = () => (
    <HashRouter>
            <Switch>
                    <Route exact path='/' component={Home} />
                    <Route path='/uuid-info' component={UuidInfo} />
                    <Route path='/access' component={ChangeAccess} />
                    <Route path='/reindex' component={Reindex} />
                    <Route path='/delete' component={Delete} />
                    <Route path='/link-vc' component={LinkVc} />
                    <Route path='/link-donator' component={LinkDonator} />
                    <Route path='/kramerius-processes' component={KrameriusProcesses} />
                    <Route path='/check-donator' component={CheckDonator} />
                    <Route path='/internal-processes' component={InternalProcesses} />
                    <Route path="/pdf" component={Pdf} />
                    <Route path="/solr-query" component={SolrQuery} />
                    <Route path="/perio-parts-publish" component={PerioPartsPublish} />
                    {/*<Route path="/set-image" component={SetImage} />*/}
                    <Route path="/vc" component={VcManagement} />
                    <Route path='/link-dnnt' component={LinkDnnt} />
                    <Route component={Error404} />
            </Switch>
    </HashRouter>
);
