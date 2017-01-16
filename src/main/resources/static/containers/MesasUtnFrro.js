import React from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import Filtros from '../components/Filtros';
import Header from '../components/Header';
import Listado from '../components/Listado';
import CircularProgress from 'material-ui/CircularProgress';

import Data from '../Data';

var moment = require('moment');
import Util from '../Util';

class MesasUtnFrro extends React.Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.setState({
            llamado: Data.getData(),
            loading: false
        });
    }

    componentWillUnmount() {

    }

    state = {
        loading: true,
        llamado : {},
        logueado: false,
        especialidad : 'ALL',
        materia : '',
        materias: []
    };

    handleMateriaUpdateInput = (value) => {
        this.setState({
            materia : value,
            materias: [
                value
            ]
        })
    };

    handleEspecialidadChange = (event, index, especialidad) => this.setState({especialidad});

    render() {
        return this.state.loading ?
            <div style={{textAlign: 'center', paddingTop: '30vh'}}><CircularProgress size={80} thickness={5}/></div>
                :
        (
            <div>
                <Header logueado={this.state.logueado}/>
                <Card
                style={{
                    backgroundColor: "#3F51B5",
                    marginTop: '10px'
                }}
               >
                   <CardHeader
                       title={"Llamado a examen número " + this.state.llamado.numero}
                       subtitle={"Año " + this.state.llamado.año}
                       titleStyle= {{
                            fontSize: "20px",
                            color: '#FFFFFF'
                       }}
                       subtitleColor="#FFFFFF"
                   />
                   <CardActions>
                       <Filtros
                           materias={this.state.materias}
                           handleEspecialidadChange={this.handleEspecialidadChange}
                           handleMateriaUpdateInput={this.handleMateriaUpdateInput}
                       />
                   </CardActions>
                   <CardText>

                   </CardText>
               </Card>
                <Listado mesas={this.state.llamado.mesas} especialidad={this.state.especialidad} materia={this.state.materia}/>
            </div>
        );
    }

}

export default MesasUtnFrro;