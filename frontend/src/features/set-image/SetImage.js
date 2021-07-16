import {SetImageForm} from "./SetImageForm";
import {SetImgPanel} from "./SetImgPanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";

export const SetImage = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<SetImageMenu />}
            rightSide={<SetImageTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const SetImageMenu = () => (
    <FeatureMenu>
        <SetImageForm />
        <SetImgPanel />
    </FeatureMenu>
);

const SetImageTable = () => (
    <FeatureContent>
    </FeatureContent>
);
