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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageRolesCommand implements ICommand {
    private List<String> possibleChanges = new ArrayList<String>() {{
        add("sendqotd");
        add("qotdtag");
    }};

    private String changeMessage = "";

    public ManageRolesCommand() {
        StringBuilder builder = new StringBuilder();
        for (String s : possibleChanges)
            builder.append("\n").append(s);
        changeMessage = "Available Role Changes: " + builder.toString();
    }

    public CoreModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get();
    }

    @Override
    public String getId() {
        return "manageroles";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.singleton("mroles");
    }

    @Override
    public String getUsage() {
        return getId() + " <set|add|remove|list> [target] [role id]";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        else if (!event.getMember().hasPermission(Permission.MANAGE_SERVER))
            event.getChannel().sendMessage("You are missing the required permissions to do this command!").queue();
        else if (args.length < 1) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
        else {
            try {
                switch (args[0].toLowerCase()) {
                    case "set": {
                        if (args.length < 3) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
                        else {
                            Map<String, List<Long>> map = getModule().getCoreData().getRoles(event.getGuild().getIdLong());
                            String target = args[1].toLowerCase();
                            Long id = Long.parseLong(args[2]);
                            if (!possibleChanges.contains(target))
                                event.getChannel().sendMessage(changeMessage).queue();
                            else {
                                List<Long> ids = Collections.singletonList(id);
                                map.put(target, ids);

                                getModule().getCoreData().setRoles(event.getGuild().getIdLong(), map);

                                event.getChannel().sendMessage(new EmbedBuilder()
                                        .setTitle("Changed Settings")
                                        .addField("Roles", target, true)
                                        .addField("Set", id.toString(), true)
                                        .setThumbnail(event.getGuild().getIconUrl())
                                        .setTimestamp(Instant.now())
                                        .setColor(Color.YELLOW)
                                        .build()
                                ).queue();
                            }
                        }

                        break;
                    }
                    case "add": {
                        Map<String, List<Long>> map = getModule().getCoreData().getRoles(event.getGuild().getIdLong());
                        String target = args[1].toLowerCase();
                        Long id = Long.parseLong(args[2].toLowerCase());
                        if (!possibleChanges.contains(target)) event.getChannel().sendMessage(changeMessage).queue();
                        else {
                            List<Long> ids = new ArrayList<>(map.get(target));
                            ids.add(id);
                            map.put(target, ids);

                            getModule().getCoreData().setRoles(event.getGuild().getIdLong(), map);

                            event.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle("Changed Settings")
                                    .addField("Roles", target, true)
                                    .addField("Add", id.toString(), true)
                                    .setThumbnail(event.getGuild().getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .setColor(Color.GREEN)
                                    .build()
                            ).queue();
                        }

                        break;
                    }
                    case "remove": {
                        Map<String, List<Long>> map = getModule().getCoreData().getRoles(event.getGuild().getIdLong());
                        String target = args[1].toLowerCase();
                        Long id = Long.parseLong(args[2].toLowerCase());
                        if (!possibleChanges.contains(target)) event.getChannel().sendMessage(changeMessage).queue();
                        else {
                            List<Long> ids = new ArrayList<>(map.get(target));
                            ids.remove(id);
                            map.put(target, ids);

                            getModule().getCoreData().setRoles(event.getGuild().getIdLong(), map);

                            event.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle("Changed Settings")
                                    .addField("Roles", target, true)
                                    .addField("Remove", id.toString(), true)
                                    .setThumbnail(event.getGuild().getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .setColor(Color.RED)
                                    .build()
                            ).queue();
                        }

                        break;
                    }
                    default:
                        event.getChannel().sendMessage(changeMessage).queue();
                }
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Usage: " + getUsage()).queue();
            }
        }
    }
}
