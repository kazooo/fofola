import React from "react";
import {useSelector} from "react-redux";

import {LoadingComponent} from "../../components/page/LoadingComponent";
import {Error} from "../../components/page/Error";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {CheckDonatorForm} from "./CheckDonatorForm";
import {CheckDonatorPanel} from "./CheckDonatorPanel";
import {FeatureContent} from "../../components/page/FeatureContent";
import {CheckDonatorFileTable} from "./CheckDonatorFileTable";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {getIsLoading, getIsLoadingError} from "./slice";
import {CenteredBox} from "../../components/layout/CenteredBox";
import {FofolaPage} from "../../components/page/FofolaPage";

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
            leftSide={<CheckDonatorMenu/>}
            rightSide={<CheckDonatorTable/>}
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

const CheckDonatorMenu = () => (
    <FeatureMenu>
        <CheckDonatorForm />
        <CheckDonatorPanel />
    </FeatureMenu>
);

const CheckDonatorTable = () => (
    <FeatureContent>
        <CheckDonatorFileTable />
    </FeatureContent>
);
