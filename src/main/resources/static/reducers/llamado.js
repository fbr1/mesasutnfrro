import * as ActionTypes from '../actiontypes/llamado';
import Data from '../Data';

const initialState = Data.getData();

export default function LLamado(state=initialState, action) {
    switch(action.type) {
        case ActionTypes.EDIT_EXAMEN :
            return [

            ];
    }
}