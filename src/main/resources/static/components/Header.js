import React from 'react';
import AppBar from 'material-ui/AppBar';
import Login from './Login';
import Logged from './Logged';

var Header = (props) => (
    <AppBar
        title="Mesas UTN Frro"
        iconClassNameLeft={"hide"}
        iconElementRight={ props.logueado ? <Logged/> : <Login/>}
        style={{backgroundColor : "#303F9F"}}
    />
);

Header.propTypes = {
    logueado: React.PropTypes.bool.isRequired
}

export default Header;