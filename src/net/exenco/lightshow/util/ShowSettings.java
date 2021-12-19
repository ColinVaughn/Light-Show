package net.exenco.lightshow.util;

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
    public ShowSettings(ConfigHandler configHandler) {
        this.configHandler = configHandler;
        load();
    }

    private Commands commands;
    private ArtNet artNet;
    private EffectSettings showEffects;
    private List<DmxEntry> dmxEntryList;
    private Stage stage;

    /**
     * Loads all necessary information.
     */
    public void load() {
        JsonObject configJson = configHandler.getConfigJson();
        this.commands = Commands.valueOf(configJson.getAsJsonObject("Commands"));
        this.showEffects = EffectSettings.valueOf(configJson.getAsJsonObject("EffectSettings"));
        this.artNet = ArtNet.valueOf(configJson.getAsJsonObject("ArtNet"));
        this.stage = Stage.valueOf(configJson.getAsJsonObject("Stage"));

        this.dmxEntryList = new ArrayList<>();
        for(JsonElement jsonElement : configJson.getAsJsonArray("DmxEntries")) {
            dmxEntryList.add(DmxEntry.valueOf(jsonElement.getAsJsonObject()));
        }
    }

    /**
     * @return Command Settings used by plugin.
     */
    public Commands commands() {
        return this.commands;
    }

    /**
     * @return Identifier list for Dmx entries.
     */
    public List<DmxEntry> dmxEntryList() {
        return this.dmxEntryList;
    }

    /**
     * @return All effect settings.
     */
    public EffectSettings showEffects() {
        return this.showEffects;
    }

    /**
     * @return Art-Net settings.
     */
    public ArtNet artNet() {
        return this.artNet;
    }

    /**
     * @return Stage settings.
     */
    public Stage stage() {
        return this.stage;
    }

    public record Commands(String noPermission, String notAllowed, String reload, String toggleOn, String toggleOff) {
        public static Commands valueOf(JsonObject jsonObject) {
            String noPermission = jsonObject.get("NoPermission").getAsString();
            String notAllowed = jsonObject.get("NotAllowed").getAsString();
            String reload = jsonObject.get("Reload").getAsString();
            String toggleOn = jsonObject.get("ToggleOn").getAsString();
            String toggleOff = jsonObject.get("ToggleOff").getAsString();
            return new Commands(noPermission, notAllowed, reload, toggleOn, toggleOff);
        }
    }

    public record ArtNet(String ip, int port, String starting, String alreadyStarted, String stopping, String alreadyStopped) {
        public static ArtNet valueOf(JsonObject jsonObject) {
            String ip = jsonObject.get("Ip").getAsString();
            int port = jsonObject.get("Port").getAsInt();
            String starting = jsonObject.get("Starting").getAsString();
            String alreadyStarted = jsonObject.get("AlreadyStarted").getAsString();
            String stopping = jsonObject.get("Stopping").getAsString();
            String alreadyStopped = jsonObject.get("AlreadyStopped").getAsString();
            return new ArtNet(ip, port, starting, alreadyStarted, stopping, alreadyStopped);
        }
    }

    public record EffectSettings(Selector selector, MovingLight movingLight) {
        public record Selector(int maxValue) {
            public static Selector valueOf(JsonObject jsonObject) {
                int maxValue = jsonObject.get("MaxValue").getAsInt();
                return new Selector(maxValue);
            }
        }

        public record MovingLight(String offTexture, String lowTexture, String mediumTexture, String highTexture) {
            public static MovingLight valueOf(JsonObject jsonObject) {
                String offTexture = jsonObject.get("OffTexture").getAsString();
                String lowTexture = jsonObject.get("LowTexture").getAsString();
                String mediumTexture = jsonObject.get("MediumTexture").getAsString();
                String highTexture = jsonObject.get("HighTexture").getAsString();
                return new MovingLight(offTexture, lowTexture, mediumTexture, highTexture);
            }
        }

        public static EffectSettings valueOf(JsonObject jsonObject) {
            Selector selector = Selector.valueOf(jsonObject.getAsJsonObject("SongSelector"));
            MovingLight movingLight = MovingLight.valueOf(jsonObject.getAsJsonObject("MovingLight"));
            return new EffectSettings(selector, movingLight);
        }
    }

    public record Stage(String information, String noCurrentSong, String termsOfService, Location location, double radius) {
        public static Stage valueOf(JsonObject jsonObject) {
            String information = jsonObject.get("Information").getAsString();
            String noCurrentSong = jsonObject.get("NoCurrentSong").getAsString();
            String termsOfService = jsonObject.get("Warning").getAsString();
            Location location = ConfigHandler.translateLocation(jsonObject.getAsJsonObject("Location"));
            if(location.getWorld() == null || Bukkit.getWorld(location.getWorld().getUID()) == null)
                throw new NullPointerException("World entered for stage location is not valid! " + location);
            double radius = jsonObject.get("Radius").getAsDouble();
            return new Stage(information, noCurrentSong, termsOfService, location, radius);
        }
    }

    public record DmxEntry(int universe, String filename, int offset) {
        public static DmxEntry valueOf(JsonObject jsonObject) {
            int universe = jsonObject.get("Universe").getAsInt();
            String filename = jsonObject.get("Filename").getAsString();
            int offset = jsonObject.has("Offset") ? jsonObject.get("Offset").getAsInt() : 0;
            return new DmxEntry(universe, filename, offset);
        }
    }
}
