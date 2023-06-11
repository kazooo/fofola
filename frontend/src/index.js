import React from 'react';
import { createRoot } from 'react-dom/client';
import {Skeleton} from '@material-ui/lab';

import {Root} from './app/Root';
import './utils/i18n';
import './index.css';

const container = document.getElementById('root');
const root = createRoot(container); // createRoot(container!) if you use TypeScript
root.render(
    <React.Suspense fallback={<Skeleton animation='wave' />}>
        <Root />
    </React.Suspense>
);
