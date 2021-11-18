import {DeleteForm} from "./DeleteForm";
import {DeletePanel} from "./DeletePanel";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";
import {DeleteUuidsTable} from "./DeleteUuidsTable";

export const Delete = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<DeleteMenu />}
            rightSide={<DeleteTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const DeleteMenu = () => (
    <FeatureMenu>
        <DeleteForm />
        <DeletePanel />
    </FeatureMenu>
);

const DeleteTable = () => (
    <FeatureContent>
        <DeleteUuidsTable />
    </FeatureContent>
);
