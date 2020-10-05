package me.schooltests.stbot.modules.core;

import me.schooltests.stbot.modules.core.commands.ManageChannelsCommand;
import me.schooltests.stbot.modules.core.commands.ManageRolesCommand;
import me.schooltests.stbot.modules.core.commands.SetPrefixCommand;
import me.schooltests.stbot.modules.Module;
import me.schooltests.stbot.modules.core.events.SetDefaultsEvent;
import me.schooltests.stbot.services.ModuleService;

public class CoreModule extends Module {
    private final CoreData coreData = new CoreData(this);
    public CoreModule(ModuleService moduleService) {
        this.moduleService = moduleService;

        registerCommand(new SetPrefixCommand());
        registerCommand(new ManageRolesCommand());
        registerCommand(new ManageChannelsCommand());

        registerEvent(new SetDefaultsEvent());
    }

    @Override
    public String getId() {
        return "core";
    }

    @Override
    public String getFriendlyName() {
        return "Core Module";
    }

    public CoreData getCoreData() {
        return coreData;
    }
}
