import {DeleteForm} from "./DeleteForm";
import {DeletePanel} from "./DeletePanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
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
