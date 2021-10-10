package com.queendev.punishplugin.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PunishModel {

    private String player;
    private String author;
    private String type;
    private String reason;
    private String prova;
    private Integer time;
}
