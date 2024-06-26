package com.kmini.store.domain.converter;

import com.kmini.store.domain.type.Gender;
import org.springframework.core.convert.converter.Converter;

public class StringToGenderConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(String source) {

        if (source.toUpperCase().equals(Gender.MAN.name())) {
            return Gender.MAN;
        }
        else if (source.toUpperCase().equals(Gender.WOMAN.name())) {
            return Gender.WOMAN;
        }
        return null;
    }
}
