package me.schooltests.stbot.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.schooltests.stbot.STBot;
import me.schooltests.stbot.interfaces.ICommand;
import me.schooltests.stbot.Module;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CoreData {
    private CoreModule module;

    public CoreData(CoreModule module) {
        this.module = module;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setDefaults(Long guildID) {
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO settings (GUILD_ID, PREFIX) VALUES (?, ?)");
            statement.setLong(1, guildID);
            statement.setString(2, STBot.getInstance().getConfig().get("prefix").getAsString());
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getGuildPrefix(Long guildID) {
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM settings WHERE guild_id=?;");
            statement.setLong(1, guildID);
            ResultSet results = statement.executeQuery();

            String prefix = results.next() ? results.getString("prefix") : STBot.getInstance().getConfig().get("prefix").getAsString();
            conn.close();

            return prefix;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return STBot.getInstance().getConfig().get("prefix").getAsString();
    }

    public void setGuildPrefix(Long guildID, String prefix) {
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE settings SET prefix=? WHERE guild_id=?;");
            statement.setLong(2, guildID);
            statement.setString(1, prefix);
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Module> getDisabledModules(Long guildID) {
        Set<Module> disabledModules = new HashSet<>();
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM settings WHERE guild_id=?");
            statement.setLong(1, guildID);
            ResultSet rs = statement.executeQuery();
            String json = rs.next() ? rs.getString("modules_disabled") : "[]";

            conn.close();
            if (json.equals("[]")) return disabledModules;
            else {
                Gson gson = STBot.getInstance().getGson();
                Type type = new TypeToken<HashSet<String>>(){}.getType();
                Set<String> disabledSet = gson.fromJson(json, type);

                for (String s : disabledSet)
                    disabledModules.add(module.moduleService.getRegistration(s).orElse(null));
                disabledModules.removeAll(Collections.singletonList(null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disabledModules;
    }

    public Set<ICommand> getDisabledCommands(Long guildID) {
        Set<ICommand> disabledCommands = new HashSet<>();
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM settings WHERE guild_id=?");
            statement.setLong(1, guildID);
            ResultSet rs = statement.executeQuery();
            String json = rs.next() ? rs.getString("commands_disabled") : "[]";

            conn.close();
            if (json.equals("[]")) return disabledCommands;
            else {
                Gson gson = STBot.getInstance().getGson();
                Type type = new TypeToken<HashSet<String>>(){}.getType();
                Set<String> disabledSet = gson.fromJson(json, type);

                for (String s : disabledSet)
                    disabledCommands.add(module.moduleService.getCommandMap().keySet().stream().filter(c -> c.getId().equalsIgnoreCase(s)).findFirst().orElse(null));
                disabledCommands.removeAll(Collections.singletonList(null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disabledCommands;
    }

    public Map<String, List<Long>> getRoles(long guildID) {
        Map<String, List<Long>> roles = new HashMap<>();
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM settings WHERE guild_id=?");
            statement.setLong(1, guildID);
            ResultSet rs = statement.executeQuery();
            String json = rs.next() ? rs.getString("roles") : "{}";

            conn.close();
            if (json.equals("{}")) return roles;
            else {
                Gson gson = STBot.getInstance().getGson();
                Type type = new TypeToken<Map<String, List<Long>>>(){}.getType();
                return gson.fromJson(json, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public void setRoles(Long guildID, Map<String, List<Long>> roles) {
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE settings SET roles=? WHERE guild_id=?;");
            statement.setLong(2, guildID);
            statement.setString(1, STBot.getInstance().getGson().toJson(roles));
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Long> getChannels(long guildID) {
        Map<String, Long> roles = new HashMap<>();
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM settings WHERE guild_id=?");
            statement.setLong(1, guildID);
            ResultSet rs = statement.executeQuery();
            String json = rs.next() ? rs.getString("channels") : "{}";

            conn.close();
            if (json.equals("{}")) return roles;
            else {
                Gson gson = STBot.getInstance().getGson();
                Type type = new TypeToken<Map<String, Long>>(){}.getType();
                return gson.fromJson(json, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public void setChannels(Long guildID, Map<String, Long> channels) {
        Connection conn = Objects.requireNonNull(getConnection());
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE settings SET channels=? WHERE guild_id=?;");
            statement.setLong(2, guildID);
            statement.setString(1, STBot.getInstance().getGson().toJson(channels));
            statement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
