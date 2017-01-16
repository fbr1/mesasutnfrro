import React from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
var moment = require('moment');
import Util from '../Util';

const Listado = (props) => (
    <div>
        {
            props.mesas.map(
                (mesa) =>
                    (
                        <Card
                            key={mesa.id}
                            containerStyle={{marginTop: '10px'}}
                        >
                            <CardHeader>
                                <h1>{"Mesa día " + mesa.weekDay  + " " + moment(mesa.fecha).format("DD/MM/YY") }</h1>
                            </CardHeader>
                            <Table selectable={false}>
                                <TableHeader displaySelectAll={false}>
                                    <TableRow>
                                        <TableHeaderColumn className='left-align teal lighten-2'>Materia</TableHeaderColumn>
                                        <TableHeaderColumn>Especialidad</TableHeaderColumn>
                                        <TableHeaderColumn>Aula</TableHeaderColumn>
                                        <TableHeaderColumn>Hora</TableHeaderColumn>
                                    </TableRow>
                                </TableHeader>
                                <TableBody displayRowCheckbox={false}>
                                    {
                                        mesa.examenes.map(
                                            (examen) =>
                                            {
                                                var hide = true;
                                                if(
                                                    (props.especialidad === 'ALL' || props.especialidad === examen.materia.especialidad) //Filtro especialidad
                                                    && (props.materia === '' || Util.getCleanedString(props.materia) === Util.getCleanedString(examen.materia.nombre).substring(0,props.materia.length))
                                                ) {
                                                    hide = false;
                                                }
                                                return (
                                                    <TableRow key={examen.id} className={hide ? 'hide' : ''}>
                                                        <TableRowColumn>{examen.materia.nombre}</TableRowColumn>
                                                        <TableRowColumn>{examen.materia.especialidad}</TableRowColumn>
                                                        <TableRowColumn>{examen.aula}</TableRowColumn>
                                                        <TableRowColumn>{moment(examen.fecha).format("HH:mm")}</TableRowColumn>
                                                    </TableRow>
                                                )

                                            }
                                        )
                                    }

                                </TableBody>
                            </Table>
                        </Card>
                    )
            )
        }
    </div>
);

Listado.propTypes = {
    mesas: React.PropTypes.arrayOf(React.PropTypes.object).isRequired,
    especialidad: React.PropTypes.string,
    materia: React.PropTypes.string,
};

export default Listado;