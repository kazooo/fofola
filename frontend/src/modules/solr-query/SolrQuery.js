import {SolrQueryForm} from "./SolrQueryForm";
import {SolrQueryFileTable} from "./SolrQueryFileTable";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";

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
