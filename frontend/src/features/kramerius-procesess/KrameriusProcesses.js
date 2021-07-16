import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {KrameriusProcessTable} from "./KrameriusProcessTable";
import {Box} from "@material-ui/core";
import {VerticalEmptyGap} from "../../components/temporary/VerticalEmptyGap";
import {KrameriusProcessMenu} from "./KrameriusProcessMenu";

export const KrameriusProcesses = () => (
    <FofolaPage>
        <SplitPageContainer
            direction="column"
            alignItems="center"
            leftSide={<KrameriusProcessHeader />}
            rightSide={<KrameriusProcessPanel />}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);

const KrameriusProcessHeader = () => (
    <FeatureMenu>
        <KrameriusProcessMenu />
    </FeatureMenu>
);

const KrameriusProcessPanel = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <KrameriusProcessTable/>
        </FeatureContent>
    </Box>
)
