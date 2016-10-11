package com.fbr1.mesasutnfrro.util;

import com.fbr1.mesasutnfrro.model.entity.Mesa;

import javax.persistence.AttributeConverter;

public class WeekDayConverter implements AttributeConverter<Mesa.WeekDay,Integer>{

    @Override
    public Integer convertToDatabaseColumn(Mesa.WeekDay weekDay) {
        return weekDay.getWeekDayValue();
    }

    @Override
    public Mesa.WeekDay convertToEntityAttribute(Integer integer) throws IllegalArgumentException{

        if(integer == null){
            return null;
        }

        for (Mesa.WeekDay mesa : Mesa.WeekDay.values()) {
            if (mesa.getWeekDayValue() == integer) {
                return mesa;
            }
        }

        throw new IllegalArgumentException("Unknown " + integer);
    }
}
