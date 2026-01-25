package com.zetaplugins.essentialz.config.moderation;

import com.zetaplugins.zetacore.annotations.PluginConfig;

@PluginConfig("moderation.yml")
public class ModerationConfig {
    private String kickMessageTemplate = """
            <red><b>You have been temporarily kicked from the server.</b></red>
             \s
              &8Reason: &7{reason}""";

    public String getKickMessageTemplate() {
        return kickMessageTemplate;
    }
}
