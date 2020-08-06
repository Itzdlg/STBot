package me.schooltests.stbot.core.events;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.core.CoreModule;
import me.schooltests.stbot.enums.EventPriority;
import me.schooltests.stbot.interfaces.IEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class SetDefaultsEvent implements IEvent {
    public CoreModule getModule() {
        return STBot.getInstance().getModuleService().getRegistration(CoreModule.class).get();
    }

    @Override
    public Class<? extends GenericGuildEvent> getEventClass() {
        return GuildJoinEvent.class;
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.LOW;
    }

    @Override
    public <T extends GenericGuildEvent> void run(T e) {
        getModule().getCoreData().setDefaults(e.getGuild().getIdLong());
    }
}
