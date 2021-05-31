package net.pretronic.dksupport.minecraft.config;

public class TicketTopic {

    private final String name;
    private final String displayName;
    private final String[] aliases;

    public TicketTopic(String name, String displayName, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
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
