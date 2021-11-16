import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {PerioPartsPublishForm} from "./PerioPartsPublishForm";
import {PerioPartsPublishPanel} from "./PerioPartsPublishPanel";
import {PerioPartsPublishUuidTable} from "./PerioPartsPublishUuidTable";

export const PerioPartsPublish = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<PerioPartsPublishMenu />}
            rightSide={<PerioPartsPublishTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

export const PerioPartsPublishMenu = () => (
    <FeatureMenu>
        <PerioPartsPublishForm />
        <PerioPartsPublishPanel />
    </FeatureMenu>
);

export const PerioPartsPublishTable = () => (
    <FeatureContent>
        <PerioPartsPublishUuidTable />
    </FeatureContent>
);
