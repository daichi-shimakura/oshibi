package com.oshibi.oshibi.domain.entity;

public enum PerformerStatus {
    CONFIRMED("出演確定"),
    TENTATIVE("確認待ち");

    private final String label;

    PerformerStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}