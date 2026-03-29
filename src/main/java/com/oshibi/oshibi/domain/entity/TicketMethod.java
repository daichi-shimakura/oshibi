package com.oshibi.oshibi.domain.entity;

public enum TicketMethod {
    OKICHIKE("取り置き(置きチケ)"),
    TICKET_SITE("チケットサイト"),
    CONVENIENCE("コンビニ発券"),
    DOOR("当日券"),
    OTHER("その他");

    private final String label;

    TicketMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}