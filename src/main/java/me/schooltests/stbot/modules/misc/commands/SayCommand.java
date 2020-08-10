package me.schooltests.stbot.modules.misc.commands;

import me.schooltests.stbot.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.Set;

public class SayCommand implements ICommand {
    @Override
    public String getId() {
        return "say";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.emptySet();
    }

    @Override
    public String getUsage() {
        return getId() + " <message>";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) return;
        String message = String.join(" ", args);
        event.getMessage().delete().reason("Executed the say command").queue();
        event.getChannel().sendMessage(message).queue();
        System.out.println("[SAY - " + event.getMember().getEffectiveName() + "] " + message);
    }
}
