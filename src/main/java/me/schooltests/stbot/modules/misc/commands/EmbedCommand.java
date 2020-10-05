package me.schooltests.stbot.modules.misc.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.schooltests.stbot.Pair;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class EmbedCommand implements ICommand {

    @Override
    public String getId() {
        return "embed";
    }

    @Override
    public Set<String> getAliases() {
        return Collections.singleton("embedmessage");
    }

    @Override
    public String getUsage() {
        return getId() + " <channel mention> <embed json> (https://discord.club/embedg/)";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("You are lacking the required permissions to do this command!").queue();
            return;
        }

        if (args.length <= 1)
            event.getChannel().sendMessage("Usage: " + getUsage()).queue();
        else if (event.getMessage().getMentionedChannels().size() > 0) {
            try {
                String jsonString = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                TextChannel channel = event.getMessage().getMentionedChannels().get(0);

                Pair<String, EmbedBuilder> result = EmbedUtil.parseEmbedFromJSON(jsonString);
                if (result.getFirst() == null || result.getSecond() == null)
                    throw new IllegalArgumentException();

                if (!result.getFirst().isEmpty())
                    channel.sendMessage(result.getFirst()).queue();

                channel.sendMessage(result.getSecond().build()).queue();
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage("Invalid JSON Object! Use https://discord.club/embedg/ to get embed json! Please use valid URLs!").queue();
            }

        } else event.getChannel().sendMessage("Usage: " + getUsage()).queue();
    }
}