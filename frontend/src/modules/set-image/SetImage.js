import {SetImageForm} from "./SetImageForm";
import {SetImgPanel} from "./SetImgPanel";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";

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
