package com.kmini.store.global.converter;

import com.kmini.store.global.constants.Gender;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.kmini.store.global.constants.Gender.MAN;
import static com.kmini.store.global.constants.Gender.WOMAN;

@Converter
public class GenderAttributeConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender != null) {
            if (gender.equals(MAN)) {
                return "M";
            } else if (gender.equals(WOMAN)) {
                return "W";
            }
        }
        return "";
    }

    @Override
    public Gender convertToEntityAttribute(String genderString) {
        if (StringUtils.hasText(genderString)) {
            if (genderString.equals("M")) {
                return MAN;
            } else if (genderString.equals("W")) {
                return WOMAN;
            }
        }
        return null;
    }
}
