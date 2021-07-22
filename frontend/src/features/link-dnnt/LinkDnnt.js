import {FofolaPage} from "../../components/temporary/FofolaPage";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {LinkDnntForm} from "./LinkDnntForm";
import {LinkDnntPanel} from "./LinkDnntPanel";
import {LinkDnntUuidsTable} from "./LinkDnntUuidsTable";

export const LinkDnnt = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<LinkDnntMenu />}
            rightSide={<LinkDnntTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const LinkDnntMenu = () => (
    <FeatureMenu>
        <LinkDnntForm />
        <LinkDnntPanel />
    </FeatureMenu>
);

const LinkDnntTable = () => (
    <FeatureContent>
        <LinkDnntUuidsTable />
    </FeatureContent>
);
