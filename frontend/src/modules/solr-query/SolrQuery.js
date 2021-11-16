import {SolrQueryForm} from "./SolrQueryForm";
import {SolrQueryFileTable} from "./SolrQueryFileTable";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";

export const SolrQuery = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='row'
            leftSide={<SolrQueryMenu />}
            rightSide={<SolrQueryFileList />}
            leftSideColumns={4}
            rightSideColumns={6}
        />
    </FofolaPage>
);

const SolrQueryMenu = () => (
    <FeatureMenu>
        <SolrQueryForm />
    </FeatureMenu>
);

const SolrQueryFileList = () => (
    <FeatureContent>
        <SolrQueryFileTable />
    </FeatureContent>
);
