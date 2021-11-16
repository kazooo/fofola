import {PdfForm} from "./PdfForm";
import {PdfFileList} from "./PdfFileList";
import {PdfPanel} from "./PdfPanel";
import {FeatureMenu} from "../../components/temporary/FeatureMenu";
import {FeatureContent} from "../../components/temporary/FeatureContent";
import {SplitPageContainer} from "../../components/temporary/SplitPageContainer";
import {FofolaPage} from "../../components/temporary/FofolaPage";
import {PdfUuidList} from "./PdfUuidList";
import {VerticalEmptyGap} from "../../components/temporary/VerticalEmptyGap";

export const Pdf = () => (
    <FofolaPage>
        <SplitPageContainer
            leftSide={<PdfMenu />}
            rightSide={<PdfUuidAndFilePanel />}
            leftSideColumns={5}
            rightSideColumns={7}
        />
    </FofolaPage>
);

const PdfMenu = () => (
    <FeatureMenu>
        <PdfForm />
        <PdfPanel />
    </FeatureMenu>
);

const PdfUuidAndFilePanel = () => (
    <FeatureContent>
        <PdfFileList />
        <VerticalEmptyGap height={10} />
        <PdfUuidList />
    </FeatureContent>
);
