import React from 'react';
import ReactDOM from 'react-dom';
import {Skeleton} from '@material-ui/lab';

import {Root} from './app/Root';
import './utils/i18n';
import './index.css';

ReactDOM.render(
    <React.Suspense fallback={<Skeleton animation='wave' />}>
        <Root />
    </React.Suspense>,
  document.getElementById('root')
);
