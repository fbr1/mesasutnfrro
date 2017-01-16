import React from 'react';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import {Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle} from 'material-ui/Toolbar';

const Filtros = (props) => (
<Toolbar style={{backgroundColor: "#3F51B5"}}>
    <ToolbarGroup>
        <SelectField
            floatingLabelText="Especialidad"
            value="ALL"
            onChange={props.handleEspecialidadChange}
        >
            <MenuItem value="ALL" primaryText="Todas"/>
            <MenuItem value="ISI" primaryText="Ingeniería en sistemas"/>
            <MenuItem value="IQ" primaryText="Ingeniería química"/>
            <MenuItem value="IM" primaryText="Ingeniería mecánica"/>
            <MenuItem value="IE" primaryText="Ingeniería eléctrica"/>
        </SelectField>
        <AutoComplete
            floatingLabelText="Materia"
            dataSource={props.materias}
            onUpdateInput={props.handleMateriaUpdateInput}
            style={{marginLeft: '10px'}}
        />
    </ToolbarGroup>
</Toolbar>
);

Filtros.propTypes = {
    materias: React.PropTypes.array.isRequired,
    handleEspecialidadChange: React.PropTypes.func.isRequired,
    handleMateriaUpdateInput: React.PropTypes.func.isRequired
}

export default Filtros;