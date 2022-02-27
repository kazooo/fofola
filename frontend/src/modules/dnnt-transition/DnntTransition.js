import {Box} from '@material-ui/core';

import {SplitPageContainer} from '../../components/page/SplitPageContainer';
import {FofolaPage} from '../../components/page/FofolaPage';
import {FeatureMenu} from '../../components/page/FeatureMenu';
import {VerticalEmptyGap} from '../../components/layout/VerticalEmptyGap';
import {FeatureContent} from '../../components/page/FeatureContent';
import {Filter} from './Filter';
import {Table} from './Table';

export const DnntTransition = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            alignItems='center'
            leftSide={<DnntTransitionFilter/>}
            rightSide={<DnntTransitionTable/>}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);


const DnntTransitionFilter = () => (
    <FeatureMenu>
        <Filter />
    </FeatureMenu>
);

const DnntTransitionTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <Table />
        </FeatureContent>
    </Box>
);
