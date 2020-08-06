package me.schooltests.stbot;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.enums.EventPriority;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.interfaces.IEvent;
import me.schooltests.stbot.services.ModuleService;
import net.dv8tion.jda.api.events.GenericEvent;

public abstract class Module {
    public final STBot bot = STBot.getInstance();
    public ModuleService moduleService = bot.getModuleService();

    public abstract String getId();

    public abstract String getFriendlyName();

    public void registerEvent(IEvent event) {
        moduleService.addEventListener(this, event);
    }

    public void registerCommand(ICommand command) {
        moduleService.addCommand(this, command);
    }
}
