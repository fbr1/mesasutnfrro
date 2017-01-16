import React from 'react';
import FlatButton from 'material-ui/FlatButton';

const Login = (props) => (
    <FlatButton {...props} label="Iniciar sesión"/>
);

Login.muiName = 'FlatButton';

export default Login;