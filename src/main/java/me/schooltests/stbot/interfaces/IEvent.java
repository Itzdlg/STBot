package me.schooltests.stbot.interfaces;

import me.schooltests.stbot.enums.EventPriority;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;

public interface IEvent {
    Class<? extends GenericGuildEvent> getEventClass();
    EventPriority getPriority();
    <T extends GenericGuildEvent> void run(T e);
}