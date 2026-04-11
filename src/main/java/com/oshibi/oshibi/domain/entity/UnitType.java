package com.oshibi.oshibi.domain.entity;

public enum UnitType {
    PIN("ピン"),
    COMBI("コンビ"),
    TRIO("トリオ"),
    GROUP("グループ"),
    STAFF("スタッフ"),
    OTHER("その他");

    private final String label;

    UnitType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return switch (this) {
            case PIN -> "ut-pin";
            case COMBI -> "ut-combi";
            case TRIO -> "ut-trio";
            case GROUP -> "ut-group";
            case STAFF -> "ut-staff";
            case OTHER -> "ut-other";
        };
    }
}