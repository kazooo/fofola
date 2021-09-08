import React from "react";
import {useSelector} from "react-redux";

import {LoadingComponent} from "../../components/temporary/LoadingComponent";
import {Error} from "../../components/temporary/Error";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {LinkVcForm} from "./LinkVcForm";
import {LinkVcPanel} from "./LinkVcPanel";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {LinkVcUuidsTable} from "./LinkVcUuidsTable";
import {getIsLoading, getIsLoadingError} from "./slice";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {CenteredBox} from "../../components/temporary/CenteredBox";

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
