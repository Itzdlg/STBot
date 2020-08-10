package me.schooltests.stbot.modules.misc.commands;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.modules.core.CoreData;
import me.schooltests.stbot.modules.core.CoreModule;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.modules.misc.MiscModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class QOTDCommand implements ICommand {
    public final Map<Long, Instant> next = new HashMap<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd 'at' h:mm a z")
            .withLocale(Locale.US)
            .withZone(ZoneId.of("US/Eastern"));

    public MiscModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(MiscModule.class).get();
    }

    public CoreData getCoreData() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get().getCoreData();
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
        if (!next.containsKey(event.getGuild().getIdLong())) next.put(event.getGuild().getIdLong(), Instant.now());
        Map<String, List<Long>> map = getCoreData().getRoles(event.getGuild().getIdLong());
        Map<String, Long> channelMap = getCoreData().getChannels(event.getGuild().getIdLong());
        if (!map.containsKey("sendqotd") || !map.containsKey("qotdtag") || !channelMap.containsKey("qotd")) {
            event.getChannel().sendMessage("This command hasn't been setup yet!").queue();
            return;
        }

        List<Long> roleIDs = map.get("sendqotd");
        if (event.getMember().getRoles().stream()
                .noneMatch(r -> roleIDs.contains(r.getIdLong())))
            event.getChannel().sendMessage("You are missing the required permissions to do this command!").queue();
        else if (args.length <= 0) event.getChannel().sendMessage("Usage: " + getUsage()).queue();
        else if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL) && Instant.now().isBefore(next.get(event.getGuild().getIdLong()))) event.getChannel().sendMessage("You must wait at least 1 day before the last QOTD!").queue();
        else {
            Long qotdID = map.get("qotdtag").get(0);
            String qotd = String.join(" ", args);
            qotd = qotd.replace("@everyone", "").replace("@here", "");
            TextChannel channel = event.getGuild().getTextChannelById(channelMap.get("qotd"));
            if (channel == null) channel = event.getChannel();

            next.put(event.getGuild().getIdLong(), Instant.now().plus(1, ChronoUnit.DAYS));
            MessageEmbed embed = new EmbedBuilder()
                    .setDescription(qotd)
                    .setFooter("Next QOTD: " + formatter.format(next.get(event.getGuild().getIdLong())), event.getAuthor().getEffectiveAvatarUrl())
                    .build();
            channel.sendMessage("<@&" + qotdID + ">").queue();
            channel.sendMessage(embed).queue();
        }
    }
}
