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

function obtenerData() {
    return {
        "id": 3,
        "anio": 2015,
        "numero": 8,
        "mesas": [{
                "fecha": 1441231200000,
                "examenes": [{
                        "fecha": 1441285230000,
                        "aula": "234",
                        "materia": {
                            "id": 2,
                            "nombre": "Ingenieria y sociedad 2015 2",
                            "especialidad": "ISI"
                        },
                        "id": 3
                    }, {
                        "fecha": 1441299600000,
                        "aula": "333",
                        "materia": {
                            "id": 3,
                            "nombre": "Fisica 2015 2",
                            "especialidad": "ISI"
                        },
                        "id": 4
                    }, {
                        "fecha": 1441292400000,
                        "aula": "321",
                        "materia": {
                            "id": 5,
                            "nombre": "Circuitos y electricidad 2015 2",
                            "especialidad": "IC"
                        },
                        "id": 5
                    }
                ],
                "llamado": 3,
                "id": 3
            }, {
                "fecha": 1441144800000,
                "examenes": [{
                        "fecha": 1441378800000,
                        "aula": "334",
                        "materia": {
                            "id": 6,
                            "nombre": "Algoritmos 2015 2 Mesa 4",
                            "especialidad": "ISI"
                        },
                        "id": 6
                    }
                ],
                "llamado": 3,
                "id": 4
            }
        ]
    };
}
function formatearFecha(milisegundos) {
    var fecha = new Date(milisegundos);
    return (fecha.getDay() + '/' + fecha.getMonth() + '/' + fecha.getFullYear());
}

function Examen(data) {
    var self = this;
    ko.mapping.fromJS(data, mapping, self);
    
    self.pasaFiltros = ko.computed(function() {
        var e = viewModel.filtroEspecialidad();
        return e === self.materia.especialidad() || e === "" || e === undefined;
    }, self, {deferEvaluation: true});

    self.fechaFormateada = ko.computed(function () {
        return formatearFecha(this.fecha());
    }, self);
}

function Mesa(data) {
    var self = this;
    ko.mapping.fromJS(data, mapping, self);
    
    self.fechaFormateada = ko.computed(function () {
        return formatearFecha(this.fecha());
    }, self);
}

function ViewModel(data, options) {
    var self = this;
    
    ko.mapping.fromJS(data, options, self);
    
    self.filtroEspecialidad = ko.observable();
    
    self.aplicarFiltros = function() {
        var especialidad = $("#filtroEspecialidad").val();
        self.filtroEspecialidad(especialidad);
    };
    
    self.mesas = self.mesas.sort(function(left, right) {
        return left.fecha() > right.fecha() ? 1 : -1;
    });
    
}

$(document).ready(function () {
    iniMaterialThings();
    data = obtenerData();
    viewModel = new ViewModel(data, mapping);
    ko.applyBindings(viewModel);
});
