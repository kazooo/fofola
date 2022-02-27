import {FofolaPage} from "../../components/page/FofolaPage";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
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
