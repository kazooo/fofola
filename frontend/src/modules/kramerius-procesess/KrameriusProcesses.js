import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";
import {KrameriusProcessTable} from "./KrameriusProcessTable";
import {Box} from "@material-ui/core";
import {VerticalEmptyGap} from "../../components/layout/VerticalEmptyGap";
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
