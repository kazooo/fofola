import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";
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
