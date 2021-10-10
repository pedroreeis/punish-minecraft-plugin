package com.queendev.punishplugin.managers;

import com.queendev.punishplugin.models.ReasonModel;

import java.util.HashMap;

public class ReasonManager {

    public HashMap<String, ReasonModel> reasons;

    public ReasonManager() {
        this.reasons = new HashMap<>();
    }

    public ReasonModel addReason(String name, String type, Integer time, String permission, String label, String description) {
        ReasonModel reason = new ReasonModel(name, type, time, permission, label, description);
        this.reasons.put(name, reason);
        return reason;
    }

    public ReasonModel getReason(String name) {
        if(this.reasons.containsKey(name)) {
            return this.reasons.get(name);
        }
        return null;
    }

    public HashMap<String, ReasonModel> getReasons() {
        return reasons;
    }
}
