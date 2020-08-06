package me.schooltests.stbot.core.commands;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.core.CoreModule;
import me.schooltests.stbot.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class QOTDCommand implements ICommand {
    private Instant next = Instant.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd 'at' h:mm a z")
            .withLocale(Locale.US)
            .withZone(ZoneId.of("US/Eastern"));

    public CoreModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get();
    }

    @Override
    public String getId() {
        return "qotd";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.singleton("questionoftheday");
    }

    @Override
    public String getUsage() {
        return getId() + " <message>";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        Map<String, List<Long>> map = getModule().getCoreData().getRoles(event.getGuild().getIdLong());
        Map<String, Long> channelMap = getModule().getCoreData().getChannels(event.getGuild().getIdLong());
        if (!map.containsKey("sendqotd") || !map.containsKey("qotdtag") || !channelMap.containsKey("qotd")) {
            event.getChannel().sendMessage("This command hasn't been setup yet!").queue();
            return;
        }

        List<Long> roleIDs = map.get("sendqotd");
        if (event.getMember().getRoles().stream()
                .noneMatch(r -> roleIDs.contains(r.getIdLong())))
            event.getChannel().sendMessage("You are missing the required permissions to do this command!").queue();
        else if (args.length <= 0) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
        else if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL) && Instant.now().isBefore(next)) event.getChannel().sendMessage("You must wait at least 1 day before the last QOTD!").queue();
        else {
            Long qotdID = map.get("qotdtag").get(0);
            String qotd = String.join(" ", args);
            qotd = qotd.replace("@everyone", "").replace("@here", "");
            TextChannel channel = event.getGuild().getTextChannelById(channelMap.get("qotd"));
            if (channel == null) channel = event.getChannel();

            next = Instant.now().plus(1, ChronoUnit.DAYS);
            MessageEmbed embed = new EmbedBuilder()
                    .setDescription(qotd)
                    .setFooter("Next QOTD: " + formatter.format(next), event.getAuthor().getEffectiveAvatarUrl())
                    .build();
            channel.sendMessage("<@&" + qotdID + ">").queue();
            channel.sendMessage(embed).queue();
        }
    }
}
