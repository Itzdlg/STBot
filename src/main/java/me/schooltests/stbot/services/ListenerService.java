package me.schooltests.stbot.services;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.core.CoreData;
import me.schooltests.stbot.core.CoreModule;
import me.schooltests.stbot.Module;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.interfaces.IEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class ListenerService implements EventListener {
    private final STBot bot = STBot.getInstance();

    public ListenerService() {
        bot.getClient().addEventListener(this);
    }

    @Override
    public void onEvent(GenericEvent genericEvent) {
        if (genericEvent instanceof GenericGuildEvent) {
            CoreData data = bot.getModuleService().getRegistration(CoreModule.class).get().getCoreData();
            GenericGuildEvent guildEvent = (GenericGuildEvent) genericEvent;
            if (genericEvent instanceof GuildMessageReceivedEvent && !((GuildMessageReceivedEvent) genericEvent).getAuthor().isBot()) {
                final GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) genericEvent;
                final String message = event.getMessage().getContentDisplay();
                final String commandWithPrefix = message.split(" ")[0].toLowerCase();
                final String guildPrefix = data.getGuildPrefix(event.getGuild().getIdLong());
                if (commandWithPrefix.startsWith(guildPrefix)) {
                    final String command = commandWithPrefix.substring(guildPrefix.length());
                    final String[] args = Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length);
                    Optional<ICommand> matchedCommand = bot.getModuleService().getCommands().stream().filter(c ->
                            c.getId().equalsIgnoreCase(command) || c.getAliases().contains(command.toLowerCase())
                    ).findFirst();

                    if (matchedCommand.isPresent()) {
                        Module registration = bot.getModuleService().getCommandMap().get(matchedCommand.get());
                        if (data.getDisabledModules(event.getGuild().getIdLong()).contains(registration)
                                || data.getDisabledCommands(event.getGuild().getIdLong()).contains(matchedCommand.get()))
                            return;

                        matchedCommand.get().run(event, args);
                        return;
                    }
                }
            }

            bot.getModuleService().getEvents().stream()
                    .filter(e -> !data.getDisabledModules(guildEvent.getGuild().getIdLong()).contains(bot.getModuleService().getEventMap().get(e)))
                    .sorted(Comparator.comparing(IEvent::getPriority))
                    .forEach(e -> e.run(guildEvent));
        }
    }
}
