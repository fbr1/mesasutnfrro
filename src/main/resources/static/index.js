import React from 'react';
import { render } from 'react-dom';
import injectTapEventPlugin from 'react-tap-event-plugin';

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import MesasUtnFrro from './containers/MesasUtnFrro';

//css
import './css/estilos.css';
import 'material-design-icons';
import Data from './Data';

//util


const App = () => (
    <MuiThemeProvider>
        <MesasUtnFrro/>
    </MuiThemeProvider>
);

render(
    <App/>,
    document.getElementById('root')
);