package me.schooltests.stbot.interfaces;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Set;

public interface ICommand {
    String getId();
    Set<String> getAliases();
    String getUsage();
    void run(GuildMessageReceivedEvent event, String[] args);
}
