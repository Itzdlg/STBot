package me.schooltests.stbot.core.commands;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.core.CoreModule;
import me.schooltests.stbot.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageChannelsCommand implements ICommand {
    private List<String> possibleChanges = new ArrayList<String>() {{
        add("qotd");
    }};

    private String changeMessage = "";

    public ManageChannelsCommand() {
        StringBuilder builder = new StringBuilder();
        for (String s : possibleChanges)
            builder.append("\n").append(s);
        changeMessage = "Available Channel Changes: " + builder.toString();
    }

    public CoreModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get();
    }

    @Override
    public String getId() {
        return "managechannels";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.singleton("mchannels");
    }

    @Override
    public String getUsage() {
        return getId() + " <set|list> [target] [channel id]";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        else if (!event.getMember().hasPermission(Permission.MANAGE_SERVER))
            event.getChannel().sendMessage("You are missing the required permissions to do this command!").queue();

        else if (args.length < 1) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
        else {
            try {
                if ("set".equals(args[0].toLowerCase())) {
                    if (args.length < 3) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
                    else {
                        Map<String, Long> map = getModule().getCoreData().getChannels(event.getGuild().getIdLong());
                        String target = args[1].toLowerCase();
                        Long id = Long.parseLong(args[2]);
                        if (!possibleChanges.contains(target))
                            event.getChannel().sendMessage(changeMessage).queue();
                        else {
                            map.put(target, id);

                            getModule().getCoreData().setChannels(event.getGuild().getIdLong(), map);

                            event.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle("Changed Settings")
                                    .addField("Channels", target, true)
                                    .addField("Set", id.toString(), true)
                                    .setThumbnail(event.getGuild().getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .setColor(Color.YELLOW)
                                    .build()
                            ).queue();
                        }
                    }
                } else {
                    event.getChannel().sendMessage(changeMessage).queue();
                }
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Usage: " + getUsage()).queue();
            }
        }
    }
}
