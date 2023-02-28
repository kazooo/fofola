import {Box} from "@material-ui/core";

import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {VerticalEmptyGap} from "../../components/layout/VerticalEmptyGap";
import {FeatureContent} from "../../components/page/FeatureContent";
import {FofolaPage} from "../../components/page/FofolaPage";
import {Table} from "./Table";
import {Header} from "./Header";

export const DnntJob = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            alignItems='center'
            leftSide={<DnntJobHeader />}
            rightSide={<DnntJobTable />}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);

const DnntJobTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <Table />
        </FeatureContent>
    </Box>
);

const DnntJobHeader = () => (
    <FeatureContent>
        <Header />
    </FeatureContent>
);
