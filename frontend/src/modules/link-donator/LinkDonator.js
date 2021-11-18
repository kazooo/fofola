import {LinkDonatorForm} from "./LinkDonatorForm";
import {LinkDonatorPanel} from "./LinkDonatorPanel";
import {FeatureMenu} from "../../components/page/FeatureMenu";
import {FeatureContent} from "../../components/page/FeatureContent";
import {SplitPageContainer} from "../../components/page/SplitPageContainer";
import {FofolaPage} from "../../components/page/FofolaPage";
import {LinkDonatorUuidsTable} from "./LinkDonatorUuidsTable";

export const LinkDonator = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<LinkDonatorMenu />}
            rightSide={<LinkDonatorTable />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const LinkDonatorMenu = () => (
    <FeatureMenu>
        <LinkDonatorForm />
        <LinkDonatorPanel />
    </FeatureMenu>
);

const LinkDonatorTable = () => (
    <FeatureContent>
        <LinkDonatorUuidsTable />
    </FeatureContent>
);
