var viewModel;

function iniMaterialThings() {
    $(".button-collapse").sideNav();
	$('select').material_select();
}

var mapping = {
    'mesas': {
        key: function (data) {
            return ko.utils.unwrapObservable(data.id);
        },
        create: function (options) {
            return new Mesa(options.data);
        }
    },
    'examenes': {
        key: function (data) {
            return ko.utils.unwrapObservable(data.id);
        },
        create: function (options) {
            return new Examen(options.data);
        }
    }
};

function getDia(dayNumber) {
    var weekday = new Array(7);
    weekday[0] = "Domingo";
    weekday[1] = "Lunes";
    weekday[2] = "Martes";
    weekday[3] = "Miércoles";
    weekday[4] = "Jueves";
    weekday[5] = "Viernes";
    weekday[6] = "Sábado";

    return weekday[dayNumber];
}

function formatearFecha(milisegundos) {
    var fecha = new Date(milisegundos);
    var salida = getDia(fecha.getDay()) + ' ' +fecha.getDate() + '/' + fecha.getMonth() + '/' + fecha.getFullYear();
    return salida;
}

function formatearFechaHora(milisegundos) {
    var fecha = new Date(milisegundos);
    var salida = fecha.getDate() + '/' + fecha.getMonth() + '/' + fecha.getFullYear();
    salida += ' ' + fecha.getHours() + ':'
            + (fecha.getMinutes() < 10 ? fecha.getMinutes() + '0' : fecha.getMinutes());
    return salida;
}

function Examen(data) {
    var self = this;
    ko.mapping.fromJS(data, mapping, self);
        
    self.pasaFiltros = ko.computed(function() {
        var pasaFiltroEspecialidad = function(especialidad) {
            return especialidad === self.materia.especialidad() 
                    || especialidad === "" 
                    || especialidad === undefined;
        };
        var pasaFiltroMateria = function(materia) {
            
            var materiaEspecialidad = self.materia.nombre();
            if (materiaEspecialidad !== undefined) {
                materiaEspecialidad = materiaEspecialidad.toLowerCase();
            }
            m = materia;
            if (materia !== undefined) {
                m = materia.toLowerCase();
            }
            var coincide = materiaEspecialidad.indexOf(m) >= 0;
            return  coincide || m === undefined || m === "";
        };
        var e = viewModel.filtroEspecialidad();
        var m = viewModel.filtroMateria();
        return pasaFiltroEspecialidad(e) && pasaFiltroMateria(m);
    }, self, {deferEvaluation: true});

    self.fechaFormateada = ko.computed(function () {
        return formatearFechaHora(this.fecha());
    }, self);
}

function Mesa(data) {
    var self = this;
    ko.mapping.fromJS(data, mapping, self);
    
    self.examenesFiltrados = ko.computed(function() {
        return self.examenes().filter(function(examen) {
            return examen.pasaFiltros();
        });
    }, self, {deferEvaluation: true});
    
    self.fechaFormateada = ko.computed(function () {
        return formatearFecha(this.fecha());
    }, self);
}

function ViewModel(data, options) {
    var self = this;
    
    ko.mapping.fromJS(data, options, self);
    
    self.filtroEspecialidad = ko.observable();
    
    self.aplicarFiltroEspecialidad = function() {
        var especialidad = $("#filtroEspecialidad").val();
        self.filtroEspecialidad(especialidad);
    };
    
    self.filtroMateria = ko.observable();
    
    self.aplicarFiltroMateria = function () {
        var materia = $('filtroMateria').val();
        self.filtroMateria(materia);
    };
    
    self.mesas = self.mesas.sort(function(left, right) {
        return left.fecha() > right.fecha() ? 1 : -1;
    });
    
}

$(document).ready(function () {
    iniMaterialThings();
    var settings = {
        url: "/rest",
        dataType: "json",
        beforeSend: function(jqXHR, settings) {
            $("#loader").show();
        },
        success: function(data, status, jqXHR) {
            viewModel = new ViewModel(data, mapping);
            ko.applyBindings(viewModel);
        },
        error: function(jqXHR, status, error) {
            alert("Ocurrio un error al obtener los datos" + error.toString());
        },
        complete: function(jqXHR, status) {
            $("#loader").hide();
        }
    }; 
    $.ajax(settings);
    
});
