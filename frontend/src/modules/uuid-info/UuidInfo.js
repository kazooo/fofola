import {UuidInfoForm} from "./UuidInfoForm";
import {UuidInfoPanel} from "./UuidInfoPanel";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";
import {Box} from "@material-ui/core";
import {VerticalEmptyGap} from "../../components/layout/VerticalEmptyGap";

export const UuidInfo = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            leftSide={<UuidInfoMenu />}
            rightSide={<UuidInfoTable />}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);

const UuidInfoMenu = () => (
    <FeatureMenu>
        <UuidInfoForm />
    </FeatureMenu>
);

const UuidInfoTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <UuidInfoPanel />
        </FeatureContent>
    </Box>
);
