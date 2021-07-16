import {useDispatch} from "react-redux";
import {useEffect} from "react";

import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {CheckDonatorForm} from "./CheckDonatorForm";
import {CheckDonatorPanel} from "./CheckDonatorPanel";
import {CheckDonatorFileTable} from "./CheckDonatorFileTable";
import {loadVirtualCollections} from "./saga";

export const CheckDonator = () => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadVirtualCollections());
    }, []);

    return <FofolaPage>
        <SplitPageContainer
            leftSide={<CheckDonatorMenu/>}
            rightSide={<CheckDonatorTable/>}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
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
