import {UuidInfoForm} from "./UuidInfoForm";
import {UuidInfoPanel} from "./UuidInfoPanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {Box} from "@material-ui/core";
import {VerticalEmptyGap} from "../../components/temporary/VerticalEmptyGap";

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
