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
}