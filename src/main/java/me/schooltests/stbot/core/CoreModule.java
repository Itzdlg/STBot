package me.schooltests.stbot.core;

import me.schooltests.stbot.core.commands.ManageChannelsCommand;
import me.schooltests.stbot.core.commands.ManageRolesCommand;
import me.schooltests.stbot.core.commands.QOTDCommand;
import me.schooltests.stbot.core.commands.SetPrefixCommand;
import me.schooltests.stbot.Module;
import me.schooltests.stbot.services.ModuleService;

public class CoreModule extends Module {
    private final CoreData coreData = new CoreData(this);
    public CoreModule(ModuleService moduleService) {
        this.moduleService = moduleService;

        registerCommand(new SetPrefixCommand());
        registerCommand(new QOTDCommand());
        registerCommand(new ManageRolesCommand());
        registerCommand(new ManageChannelsCommand());
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
