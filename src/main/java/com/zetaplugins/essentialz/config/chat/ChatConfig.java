package com.zetaplugins.essentialz.config.chat;

import com.zetaplugins.zetacore.annotations.PluginConfig;

@PluginConfig("chat.yml")
public class ChatConfig {
    private boolean enableCustomChat = true;
    private String chatFormat = "&7{player_displayname} &8» &f{message}";
    private int maxNicknameLength = 16;
    private String broadcastFormat = "&8[<#F06292>Broadcast&8] &7{message}";
    private boolean enableJoinMessages = true;
    private boolean enableLeaveMessages = true;
    private boolean specialWelcomeJoinMessage = true;
    private boolean enableDeathMessages = true;
    private boolean enableAdvancementMessages = true;

    public boolean isEnableCustomChat() {
        return enableCustomChat;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public int getMaxNicknameLength() {
        return maxNicknameLength;
    }

    public String getBroadcastFormat() {
        return broadcastFormat;
    }

    public boolean isEnableJoinMessages() {
        return enableJoinMessages;
    }

    public boolean isEnableLeaveMessages() {
        return enableLeaveMessages;
    }

    public boolean isSpecialWelcomeJoinMessage() {
        return specialWelcomeJoinMessage;
    }

    public boolean isEnableDeathMessages() {
        return enableDeathMessages;
    }

    public boolean isEnableAdvancementMessages() {
        return enableAdvancementMessages;
    }
}
