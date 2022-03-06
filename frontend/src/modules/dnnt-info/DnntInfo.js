import {Box} from '@material-ui/core';

import {SplitPageContainer} from '../../components/page/SplitPageContainer';
import {VerticalEmptyGap} from '../../components/layout/VerticalEmptyGap';
import {FeatureContent} from '../../components/page/FeatureContent';
import {FeatureMenu} from '../../components/page/FeatureMenu';
import {FofolaPage} from '../../components/page/FofolaPage';

import {Filter} from './Filter';
import {Table} from './Table';

export const DnntInfo = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            alignItems='center'
            leftSide={<DnntInfoFilter/>}
            rightSide={<DnntInfoTable/>}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);


const DnntInfoFilter = () => (
    <FeatureMenu>
        <Filter />
    </FeatureMenu>
);

const DnntInfoTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <Table />
        </FeatureContent>
    </Box>
);
