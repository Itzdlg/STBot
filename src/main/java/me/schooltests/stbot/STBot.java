package me.schooltests.stbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.schooltests.stbot.core.CoreModule;
import me.schooltests.stbot.services.ListenerService;
import me.schooltests.stbot.services.ModuleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class STBot {
    private static STBot instance;
    private ModuleService moduleService;
    private ListenerService listenerService;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject botConfig;
    private JDA client;

    public STBot() {
        instance = this;
    }

    public void loadConfig() {
        try {
            botConfig = JsonParser.parseReader(new FileReader(new File("config.json"))).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startClient() {
        try {
            client = JDABuilder.createDefault(botConfig.get("token").getAsString()).build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        // Register services
        moduleService = new ModuleService();
        listenerService = new ListenerService();

        // Register core module
        moduleService.register(new CoreModule(moduleService));

        // Register modules

    }

    public static STBot getInstance() {
        return instance;
    }

    public ModuleService getModuleService() {
        return moduleService;
    }

    public JDA getClient() {
        return client;
    }

    public JsonObject getConfig() {
        return botConfig;
    }

    public Gson getGson() {
        return gson;
    }
}
