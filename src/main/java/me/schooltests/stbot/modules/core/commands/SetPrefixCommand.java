package me.schooltests.stbot.modules.core.commands;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.modules.core.CoreModule;
import me.schooltests.stbot.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class SetPrefixCommand implements ICommand {
    public CoreModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get();
    }

    @Override
    public String getId() {
        return "setprefix";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.emptySet();
    }

    @Override
    public String getUsage() {
        return getId() + " <prefix>";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        else if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) event.getChannel().sendMessage("You are missing the required permissions to do this command!").queue();
        if (args.length <= 0) event.getChannel().sendMessage(getUsage()).queue();
        else if (args.length > 1) event.getChannel().sendMessage("Prefixes may not contain spaces!").queue();
        else {
            final String prefix = args[0].toLowerCase();
            getModule().getCoreData().setGuildPrefix(event.getGuild().getIdLong(), prefix);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Changed Settings");
            builder.addField("Setting", "prefix", true);
            builder.addField("Value", prefix, true);
            builder.setThumbnail(event.getGuild().getIconUrl());
            builder.setTimestamp(Instant.now());
            builder.setColor(Color.GREEN);

            event.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
