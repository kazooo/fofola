import {ChangeAccessForm} from "./ChangeAccessForm";
import {ChangeAccessPanel} from "./ChangeAccessPanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {ChangeAccessUuidsTable} from "./ChangeAccessUuidsTable";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";

export const ChangeAccess = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<ChangeAccessMenu />}
            rightSide={<ChangeAccessTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const ChangeAccessMenu = () => (
    <FeatureMenu>
        <ChangeAccessForm />
        <ChangeAccessPanel />
    </FeatureMenu>
);

const ChangeAccessTable = () => (
    <FeatureContent>
        <ChangeAccessUuidsTable />
    </FeatureContent>
);
