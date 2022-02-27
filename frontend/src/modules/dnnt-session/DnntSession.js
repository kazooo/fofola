import {Box} from '@material-ui/core';

import {FofolaPage} from '../../components/page/FofolaPage';
import {SplitPageContainer} from '../../components/page/SplitPageContainer';
import {FeatureMenu} from '../../components/page/FeatureMenu';
import {VerticalEmptyGap} from '../../components/layout/VerticalEmptyGap';
import {FeatureContent} from '../../components/page/FeatureContent';

import {Filter} from './Filter';
import {Table} from './Table';

export const DnntSession = () => (
    <FofolaPage>
        <SplitPageContainer
            direction='column'
            alignItems='center'
            leftSide={<DnntSessionFilter/>}
            rightSide={<DnntSessionTable/>}
            leftSideColumns={12}
            rightSideColumns={12}
        />
    </FofolaPage>
);

const DnntSessionFilter = () => (
    <FeatureMenu>
        <Filter />
    </FeatureMenu>
);

const DnntSessionTable = () => (
    <Box>
        <VerticalEmptyGap height={5}/>
        <FeatureContent>
            <Table />
        </FeatureContent>
    </Box>
);
