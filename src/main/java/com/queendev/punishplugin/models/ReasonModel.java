package com.queendev.punishplugin.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ReasonModel {

    String name;
    String type;
    Integer time;
    String permission;
    String label;
    String description;
}
