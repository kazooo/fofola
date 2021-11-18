import {ReindexPanel} from "./ReindexPanel";
import {FofolaPage} from "../../components/page/FofolaPage";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {ReindexForm} from "./ReindexForm";
import {FeatureContent} from "../../components/page/FeatureContent";
import {ReindexUuidsTable} from "./ReindexUuidsTable";

export const Reindex = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<ReindexMenu />}
            rightSide={<ReindexTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const ReindexMenu = () => (
    <FeatureMenu>
        <ReindexForm />
        <ReindexPanel />
    </FeatureMenu>
);

const ReindexTable = () => (
    <FeatureContent>
        <ReindexUuidsTable />
    </FeatureContent>
);
