package me.schooltests.stbot.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.schooltests.stbot.Pair;
import net.dv8tion.jda.api.EmbedBuilder;

public final class EmbedUtil {
    public static Pair<String, EmbedBuilder> parseEmbedFromJSON(String json) {
        JsonObject complete = JsonParser.parseString(json).getAsJsonObject();
        if (!complete.has("embed")) {
            return Pair.empty();
        }

        JsonObject embedJson = complete.get("embed").getAsJsonObject();

        EmbedBuilder embed = new EmbedBuilder();
        String unembedded = "";

        if (embedJson.has("title") && embedJson.has("url"))
            embed.setTitle(embedJson.get("title").getAsString(), embedJson.get("url").getAsString().startsWith("http") ? embedJson.get("url").getAsString() : "https://" + embedJson.get("url").getAsString());
        else if (embedJson.has("title") && !embedJson.has("url")) embed.setTitle(embedJson.get("title").getAsString());

        if (embedJson.has("description")) embed.setDescription(embedJson.get("description").getAsString());
        if (embedJson.has("author")) {
            JsonObject author = embedJson.get("author").getAsJsonObject();
            String name = author.has("name") ? author.get("name").getAsString() : null;
            String url = author.has("url") ? author.get("url").getAsString() : null;
            if (url != null && !url.startsWith("http")) url = "https://" + url;

            String icon_url = author.has("icon_url") ? author.get("icon_url").getAsString() : null;
            if (icon_url != null && !icon_url.startsWith("http")) icon_url = "https://" + icon_url;

            embed.setAuthor(name, url, icon_url);
        }

        if (embedJson.has("thumbnail") && embedJson.get("thumbnail").getAsJsonObject().has("url"))
            embed.setThumbnail(embedJson.get("thumbnail").getAsJsonObject().get("url").getAsString().startsWith("http") ? embedJson.get("thumbnail").getAsJsonObject().get("url").getAsString() : "https://" + embedJson.get("thumbnail").getAsJsonObject().get("url").getAsString());

        if (embedJson.has("image") && embedJson.get("image").getAsJsonObject().has("url"))
            embed.setThumbnail((embedJson.get("image").getAsJsonObject().get("url").getAsString().startsWith("http") ? embedJson.get("image").getAsJsonObject().get("url").getAsString() : "https://" + embedJson.get("image").getAsJsonObject().get("url").getAsString()));

        if (embedJson.has("footer")) {
            JsonObject footer = embedJson.get("footer").getAsJsonObject();
            String text = footer.has("text") ? footer.get("text").getAsString() : null;
            String url = footer.has("icon_url") ? footer.get("icon_url").getAsString() : null;
            if (url != null && !url.startsWith("http")) url = "https://" + url;

            embed.setFooter(text, url);
        }

        if (embedJson.has("fields")) {
            JsonArray arr = embedJson.get("fields").getAsJsonArray();
            for (JsonElement l : arr) {
                JsonObject element = l.getAsJsonObject();
                String name = element.has("name") ? element.get("name").getAsString() : null;
                String value = element.has("value") ? element.get("value").getAsString() : null;
                boolean inline = !element.has("inline") || element.get("inline").getAsBoolean();

                embed.addField(name, value, inline);
            }
        }

        if (embedJson.has("color"))
            embed.setColor(embedJson.get("color").getAsInt());

        if (complete.has("content"))
            unembedded = complete.get("content").getAsString();

        return new Pair<>(unembedded, embed);
    }
}
