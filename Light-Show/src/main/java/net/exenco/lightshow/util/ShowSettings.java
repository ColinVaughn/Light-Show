package net.exenco.lightshow.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to load and administer settings set by the user via files.
 */
public class ShowSettings {

    private final ConfigHandler configHandler;
    private Commands commands;
    private ArtNet artNet;
    private EffectSettings showEffects;
    private List<DmxEntry> dmxEntryList;
    private Stage stage;

    public ShowSettings(ConfigHandler configHandler) {
        this.configHandler = configHandler;
        load();
    }

    public void load() {
        JsonObject configJson = configHandler.getConfigJson();

        if (configJson == null) {
            throw new IllegalStateException("Configuration JSON is null");
        }

        this.commands = Commands.valueOf(configJson.getAsJsonObject("Commands"));
        this.showEffects = EffectSettings.valueOf(configJson.getAsJsonObject("EffectSettings"));
        this.artNet = ArtNet.valueOf(configJson.getAsJsonObject("ArtNet"));
        this.stage = Stage.valueOf(configJson.getAsJsonObject("Stage"));

        this.dmxEntryList = new ArrayList<>();
        JsonArray dmxEntries = configJson.getAsJsonArray("DmxEntries");
        if (dmxEntries != null) {
            for (JsonElement jsonElement : dmxEntries) {
                dmxEntryList.add(DmxEntry.valueOf(jsonElement.getAsJsonObject()));
            }
        } else {
            throw new IllegalStateException("DmxEntries array is missing from configuration");
        }
    }

    public Commands commands() {
        return this.commands;
    }

    public List<DmxEntry> dmxEntryList() {
        return this.dmxEntryList;
    }

    public EffectSettings showEffects() {
        return this.showEffects;
    }

    public ArtNet artNet() {
        return this.artNet;
    }

    public Stage stage() {
        return this.stage;
    }

    public record Commands(String noPermission, String notAllowed, String reload, String toggleOn, String toggleOff) {
        public static Commands valueOf(JsonObject jsonObject) {
            if (jsonObject == null) throw new IllegalArgumentException("JsonObject for Commands cannot be null");

            String noPermission = jsonObject.has("NoPermission") ? jsonObject.get("NoPermission").getAsString() : "";
            String notAllowed = jsonObject.has("NotAllowed") ? jsonObject.get("NotAllowed").getAsString() : "";
            String reload = jsonObject.has("Reload") ? jsonObject.get("Reload").getAsString() : "";
            String toggleOn = jsonObject.has("ToggleOn") ? jsonObject.get("ToggleOn").getAsString() : "";
            String toggleOff = jsonObject.has("ToggleOff") ? jsonObject.get("ToggleOff").getAsString() : "";

            return new Commands(noPermission, notAllowed, reload, toggleOn, toggleOff);
        }
    }

    public record ArtNet(Redirector redirector, Address address, int timeout, String starting, String cannotStart, String stopping, String cannotStop, String connected, String notConnected) {
        public static ArtNet valueOf(JsonObject jsonObject) {
            if (jsonObject == null) throw new IllegalArgumentException("JsonObject for ArtNet cannot be null");

            Redirector redirector = jsonObject.has("Redirector") ? Redirector.valueOf(jsonObject.getAsJsonObject("Redirector")) : null;
            Address address = jsonObject.has("Address") ? Address.valueOf(jsonObject.getAsJsonObject("Address")) : null;
            int timeout = jsonObject.has("Timeout") ? jsonObject.get("Timeout").getAsInt() : 0;
            String starting = jsonObject.has("Starting") ? jsonObject.get("Starting").getAsString() : "";
            String cannotStart = jsonObject.has("CannotStart") ? jsonObject.get("CannotStart").getAsString() : "";
            String stopping = jsonObject.has("Stopping") ? jsonObject.get("Stopping").getAsString() : "";
            String cannotStop = jsonObject.has("CannotStop") ? jsonObject.get("CannotStop").getAsString() : "";
            String connected = jsonObject.has("Connected") ? jsonObject.get("Connected").getAsString() : "";
            String notConnected = jsonObject.has("NotConnected") ? jsonObject.get("NotConnected").getAsString() : "";

            return new ArtNet(redirector, address, timeout, starting, cannotStart, stopping, cannotStop, connected, notConnected);
        }

        public record Redirector(boolean enabled, String key) {
            public static Redirector valueOf(JsonObject jsonObject) {
                if (jsonObject == null) throw new IllegalArgumentException("JsonObject for Redirector cannot be null");

                boolean enabled = jsonObject.has("Enabled") ? jsonObject.get("Enabled").getAsBoolean() : false;
                String key = jsonObject.has("Key") ? jsonObject.get("Key").getAsString() : "";
                return new Redirector(enabled, key);
            }
        }

        public record Address(String ip, int port) {
            public static Address valueOf(JsonObject jsonObject) {
                if (jsonObject == null) throw new IllegalArgumentException("JsonObject for Address cannot be null");

                String ip = jsonObject.has("Ip") ? jsonObject.get("Ip").getAsString() : "";
                int port = jsonObject.has("Port") ? jsonObject.get("Port").getAsInt() : 0;
                return new Address(ip, port);
            }
        }
    }

    public record EffectSettings(Selector selector, MovingLight movingLight) {
        public record Selector(int maxValue) {
            public static Selector valueOf(JsonObject jsonObject) {
                if (jsonObject == null) throw new IllegalArgumentException("JsonObject for Selector cannot be null");

                int maxValue = jsonObject.has("MaxValue") ? jsonObject.get("MaxValue").getAsInt() : 0;
                return new Selector(maxValue);
            }
        }

        public record MovingLight(String offTexture, String lowTexture, String mediumTexture, String highTexture) {
            public static MovingLight valueOf(JsonObject jsonObject) {
                if (jsonObject == null) throw new IllegalArgumentException("JsonObject for MovingLight cannot be null");

                String offTexture = jsonObject.has("OffTexture") ? jsonObject.get("OffTexture").getAsString() : "";
                String lowTexture = jsonObject.has("LowTexture") ? jsonObject.get("LowTexture").getAsString() : "";
                String mediumTexture = jsonObject.has("MediumTexture") ? jsonObject.get("MediumTexture").getAsString() : "";
                String highTexture = jsonObject.has("HighTexture") ? jsonObject.get("HighTexture").getAsString() : "";
                return new MovingLight(offTexture, lowTexture, mediumTexture, highTexture);
            }
        }

        public static EffectSettings valueOf(JsonObject jsonObject) {
            if (jsonObject == null) throw new IllegalArgumentException("JsonObject for EffectSettings cannot be null");

            Selector selector = jsonObject.has("SongSelector") ? Selector.valueOf(jsonObject.getAsJsonObject("SongSelector")) : null;
            MovingLight movingLight = jsonObject.has("MovingLight") ? MovingLight.valueOf(jsonObject.getAsJsonObject("MovingLight")) : null;
            return new EffectSettings(selector, movingLight);
        }
    }

    public record Stage(String information, String noCurrentSong, String termsOfService, Location location, double radius) {
        public static Stage valueOf(JsonObject jsonObject) {
            if (jsonObject == null) throw new IllegalArgumentException("JsonObject for Stage cannot be null");

            String information = jsonObject.has("Information") ? jsonObject.get("Information").getAsString() : "";
            String noCurrentSong = jsonObject.has("NoCurrentSong") ? jsonObject.get("NoCurrentSong").getAsString() : "";
            String termsOfService = jsonObject.has("Warning") ? jsonObject.get("Warning").getAsString() : "";
            Location location = jsonObject.has("Location") ? ConfigHandler.translateLocation(jsonObject.getAsJsonObject("Location")) : null;
            if (location == null || location.getWorld() == null || Bukkit.getWorld(location.getWorld().getUID()) == null) {
                throw new NullPointerException("World entered for stage location is not valid! " + location);
            }
            double radius = jsonObject.has("Radius") ? jsonObject.get("Radius").getAsDouble() : 0.0;
            return new Stage(information, noCurrentSong, termsOfService, location, radius);
        }
    }

    public record DmxEntry(int universe, String filename, int offset) {
        public static DmxEntry valueOf(JsonObject jsonObject) {
            if (jsonObject == null) throw new IllegalArgumentException("JsonObject for DmxEntry cannot be null");

            int universe = jsonObject.has("Universe") ? jsonObject.get("Universe").getAsInt() : 0;
            String filename = jsonObject.has("Filename") ? jsonObject.get("Filename").getAsString() : "";
            int offset = jsonObject.has("Offset") ? jsonObject.get("Offset").getAsInt() : 0;
            return new DmxEntry(universe, filename, offset);
        }
    }
}

