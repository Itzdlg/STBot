package me.schooltests.stbot;

import me.schooltests.stbot.modules.misc.commands.QOTDCommand;
import me.schooltests.stbot.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Guild;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Scanner;

public class Program {
    public static void main(String[] appArgs) {
        STBot bot = new STBot();
        bot.loadConfig();
        bot.startClient();
        bot.init();

        System.out.println("[ Entering Terminal Administration Mode ]");
        System.out.println("Available Commands: " +
                "stats, " +
                "qotd reset <guild>, " +
                "qotd push <guild>, " +
                "guilds list, " +
                "guilds leave <guild>");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.nextLine().toLowerCase();
            if (command.startsWith("qotd reset")) {
                long l = Long.parseLong(command.replace("qotd reset", "").trim());
                Optional<ICommand> botCommand = bot.getModuleService().getCommands().stream().filter(c -> c.getId().equalsIgnoreCase("qotd")).findFirst();
                botCommand.ifPresent(c -> ((QOTDCommand)c).next.remove(l));
            } else if (command.startsWith("qotd push")) {
                long l = Long.parseLong(command.replace("qotd push", "").trim());
                Optional<ICommand> botCommand = bot.getModuleService().getCommands().stream().filter(c -> c.getId().equalsIgnoreCase("qotd")).findFirst();
                botCommand.ifPresent(c -> ((QOTDCommand) c).next.put(l, Instant.now().plus(1, ChronoUnit.DAYS)));
            } else if (command.startsWith("guilds list")) {
                System.out.println("STBot is in: ");
                for (Guild g : bot.getClient().getGuilds())
                    System.out.println(g.getName() + " - " + g.getId());
            } else if (command.startsWith("guilds leave")) {
                long l = Long.parseLong(command.replace("guilds leave", "").trim());
                bot.getClient().getGuilds().stream().filter(g -> g.getIdLong() == l).forEach(g -> {
                    System.out.println("Leaving guild: " + g.getName());
                    g.leave().queue();
                });
            } else if (command.startsWith("stats")) {
                System.out.println("Number of guilds joined: " + bot.getClient().getGuilds().size());
                System.out.println("Number of users: " + bot.getClient().getGuilds().stream().mapToInt(Guild::getMemberCount).sum());
            }
        }
    }
}
