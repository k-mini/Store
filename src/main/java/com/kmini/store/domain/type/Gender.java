package com.kmini.store.domain.type;

public enum Gender {
    MAN("남자"), WOMAN("여자");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
