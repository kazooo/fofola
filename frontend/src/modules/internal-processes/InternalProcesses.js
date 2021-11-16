import {Box} from "@material-ui/core";

import {InternalProcessMenu as Header} from "./InternalProcessMenu";
import {InternalProcessesTable as Table} from "./InternalProcessesTable";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {VerticalEmptyGap} from "../../components/temporary/VerticalEmptyGap";

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
