import React from "react";
import {useSelector} from "react-redux";

import {LoadingComponent} from "../../components/page/LoadingComponent";
import {Error} from "../../components/page/Error";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {LinkVcForm} from "./LinkVcForm";
import {LinkVcPanel} from "./LinkVcPanel";
import {FeatureContent} from "../../components/page/FeatureContent";
import {LinkVcUuidsTable} from "./LinkVcUuidsTable";
import {getIsLoading, getIsLoadingError} from "./slice";
import {FofolaPage} from "../../components/page/FofolaPage";
import {CenteredBox} from "../../components/layout/CenteredBox";

export const Content = ({classes}) => {
    const isLoading = useSelector(state => getIsLoading(state));
    const isLoadingError = useSelector(state => getIsLoadingError(state));

    const loading = (
        <CenteredBox classes={classes.root}>
            <LoadingComponent label={'Načítám virtuální sbírky...'} />
        </CenteredBox>
    );

    const error = (
        <CenteredBox classes={classes.root}>
            <Error label={'Chyba při načtení virtuálních sbírek!'} />
        </CenteredBox>
    );

    const container = (
        <SplitPageContainer
            leftSide={<LinkVcMenu/>}
            rightSide={<LinkVcTable/>}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    );

    return <FofolaPage>
        {
            isLoading && loading
        }
        {
            isLoadingError && error
        }
        {
            !isLoading && !isLoadingError && container
        }
    </FofolaPage>;
};

const LinkVcMenu = () => (
    <FeatureMenu>
        <LinkVcForm />
        <LinkVcPanel />
    </FeatureMenu>
);

const LinkVcTable = () => (
    <FeatureContent>
        <LinkVcUuidsTable />
    </FeatureContent>
);
