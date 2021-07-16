import {useDispatch} from "react-redux";

import {LinkVcForm} from "./LinkVcForm";
import {LinkVcPanel} from "./LinkVcPanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {LinkVcUuidsTable} from "./LinkVcUuidsTable";
import {useEffect} from "react";
import {loadVirtualCollections} from "./saga";

export const LinkVc = () => {

    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadVirtualCollections());
    }, []);

    return (
        <FofolaPage>
            <SplitPageContainer
                leftSide={<LinkVcMenu/>}
                rightSide={<LinkVcTable/>}
                leftSideColumns={5}
                rightSideColumns={7}
            />
        </FofolaPage>
    );
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
