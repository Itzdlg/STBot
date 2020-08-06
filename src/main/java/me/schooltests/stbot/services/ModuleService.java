package me.schooltests.stbot.services;

import me.schooltests.stbot.STBot;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.interfaces.IEvent;
import me.schooltests.stbot.Module;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModuleService {
    public final STBot bot = STBot.getInstance();
    private final Set<Module> modules = new HashSet<>();
    private final Map<IEvent, Module> events = new HashMap<>();
    private final Map<ICommand, Module> commands = new HashMap<>();

    public <T extends Module> void register(T module) {
        modules.add(module);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> Optional<T> getRegistration(String id) {
        return (Optional<T>) modules.stream().filter(m -> m.getId().equalsIgnoreCase(id)).findFirst();

    }

    public <T extends Module> Optional<T> getRegistration(Class<T> clazz) {
        return modules.stream().filter(m -> m.getClass().equals(clazz))
                .map(clazz::cast).findFirst();
    }

    public Set<Module> getModules() {
        return Collections.unmodifiableSet(modules);
    }

    public void addEventListener(Module m, IEvent e) {
        events.put(e, m);
    }

    public Set<IEvent> getEvents() {
        return Collections.unmodifiableSet(events.keySet());
    }

    public final Map<IEvent, Module> getEventMap() {
        return Collections.unmodifiableMap(events);
    }

    public void addCommand(Module m, ICommand e) {
        commands.put(e, m);
    }

    public Set<ICommand> getCommands() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public Map<ICommand, Module> getCommandMap() {
        return Collections.unmodifiableMap(commands);
    }
}