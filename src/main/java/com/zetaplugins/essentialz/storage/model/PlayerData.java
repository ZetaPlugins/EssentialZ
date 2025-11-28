package com.zetaplugins.essentialz.storage.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private final String uuid;
    private boolean enableTeamchat = true;
    private boolean enableDms = true;
    private double balance = 0.0;

    private final Set<String> modifiedFields = new HashSet<>(); // Track modified fields

    public PlayerData(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isEnableTeamchat() {
        return enableTeamchat;
    }

    public void setEnableTeamchat(boolean enableTeamchat) {
        if (this.enableTeamchat != enableTeamchat) {
            this.enableTeamchat = enableTeamchat;
            modifiedFields.add("enableTeamchat");
        }
    }

    public boolean isEnableDms() {
        return enableDms;
    }

    public void setEnableDms(boolean enableDms) {
        if (this.enableDms != enableDms) {
            this.enableDms = enableDms;
            modifiedFields.add("enableDms");
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (this.balance != balance) {
            this.balance = balance;
            modifiedFields.add("balance");
        }
    }

    public boolean hasChanges() {
        return !modifiedFields.isEmpty();
    }

    public Set<String> getModifiedFields() {
        return new HashSet<>(modifiedFields);
    }

    public void clearModifiedFields() {
        modifiedFields.clear();
    }
}