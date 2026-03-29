package com.oshibi.oshibi.domain.entity;

public enum UnitType {
    PIN("ピン"),
    COMBI("コンビ"),
    TRIO("トリオ"),
    GROUP("グループ");

    private final String label;

    UnitType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}