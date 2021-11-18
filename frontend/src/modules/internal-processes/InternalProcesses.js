import {Box} from "@material-ui/core";

import {InternalProcessMenu as Header} from "./InternalProcessMenu";
import {InternalProcessesTable as Table} from "./InternalProcessesTable";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {FofolaPage} from "../../components/page/FofolaPage";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {VerticalEmptyGap} from "../../components/layout/VerticalEmptyGap";

export const InternalProcesses = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            alignItems='center'
            leftSide={<InternalProcessesMenu/>}
            rightSide={<InternalProcessesTable/>}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);

const InternalProcessesMenu = () => (
    <FeatureMenu>
        <Header />
    </FeatureMenu>
);

const InternalProcessesTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <Table />
        </FeatureContent>
    </Box>
);
