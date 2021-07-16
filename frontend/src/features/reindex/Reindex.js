import {ReindexPanel} from "./ReindexPanel";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {ReindexForm} from "./ReindexForm";
import {FeatureContent} from "../../components/temporary/FeatureContent";
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
