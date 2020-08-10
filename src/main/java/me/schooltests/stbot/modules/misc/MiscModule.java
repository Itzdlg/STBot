package me.schooltests.stbot.modules.misc;

import me.schooltests.stbot.modules.Module;
import me.schooltests.stbot.modules.core.CoreData;
import me.schooltests.stbot.modules.misc.commands.QOTDCommand;
import me.schooltests.stbot.modules.misc.commands.SayCommand;
import me.schooltests.stbot.services.ModuleService;

public class MiscModule extends Module {
    public MiscModule(ModuleService moduleService) {
        this.moduleService = moduleService;
        registerCommand(new QOTDCommand());
        registerCommand(new SayCommand());
    }

    @Override
    public String getId() {
        return "misc";
    }

    @Override
    public String getFriendlyName() {
        return "Misc Module";
    }
}