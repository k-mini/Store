package com.kmini.store.domain.converter;

import com.kmini.store.domain.type.Gender;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.Gender.WOMAN;

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
