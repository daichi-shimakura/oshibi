package com.oshibi.oshibi.domain.entity;

public enum AccountType {
    FAN("ファン"),
    LIVE_STAFF("ライブ関係者");

    private final String label;

    AccountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}