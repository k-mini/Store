package com.kmini.store.global.constants;

public enum UserStatus {
    SIGNUP("가입"), WITHDREW("탈퇴");

    private final String label;

    UserStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
