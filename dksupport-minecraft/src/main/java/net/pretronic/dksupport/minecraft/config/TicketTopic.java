package net.pretronic.dksupport.minecraft.config;

import net.pretronic.dkconnect.api.voiceadapter.Emoji;
import net.pretronic.dkconnect.api.voiceadapter.VoiceAdapter;

public class TicketTopic {

    private final String name;
    private final String displayName;
    private final String[] aliases;
    private final String discordEmoji;

    public TicketTopic(String name, String displayName, String discordEmoji, String... aliases) {
        this.name = name;
        this.discordEmoji = discordEmoji;
        this.aliases = aliases;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDiscordEmoji() {
        return discordEmoji;
    }

    public Emoji getDiscordEmoji(VoiceAdapter voiceAdapter) {
        return voiceAdapter.parseEmoji(discordEmoji);
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSame(String name) {
        if(this.name.equalsIgnoreCase(name)) return true;
        if(this.aliases != null) {
            for (String alias : this.aliases) {
                if(name.equalsIgnoreCase(alias)) return true;
            }
        }
        return false;
    }
}
