package com.oshibi.oshibi.domain.entity;

public enum LiveType {
    NETA("ネタ"),
    TALK("トーク"),
    KIKAKU("企画"),
    YOSEN("賞レース"),
    BATTLE("バトル"),
    TANDOKU("単独"),
    TWOMAN("ツーマン"),
    UNIT("ユニット"),
    OTHER("その他");


    private final String label;

    LiveType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
