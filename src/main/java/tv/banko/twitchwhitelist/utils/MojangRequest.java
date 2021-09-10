package tv.banko.twitchwhitelist.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;

public class MojangRequest {

    private int requests;
    private long resetIn;

    public MojangRequest() {
        this.requests = 0;
        this.resetIn = System.currentTimeMillis() + (10 * 60 * 1000);
    }

    public UUID getUUID(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;

        if (resetIn <= System.currentTimeMillis()) {
            requests = 0;
            resetIn = System.currentTimeMillis() + (10 * 60 * 1000);
        }

        if (requests > 400) {
            Bukkit.getLogger().log(Level.WARNING, "Could not manage mojang request of " + name + "! Too many requests in 10 Minutes.");
            return null;
        }

        try {
            String json = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);

            requests++;

            if (json.isEmpty()) return null;

            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(json);

            JsonObject jsonObject = jsonTree.getAsJsonObject();

            String uuidString = jsonObject.get("id").toString().replace("\"", "");
            return fromString(uuidString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UUID fromString(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {

            return UUID.fromString(
                uuid.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                )
            );
        }
    }
}
