package wtf.gacek.nosignatures;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class NoSignaturesSpigot extends JavaPlugin {
    private NoSignaturesCore core;

    @Override
    public void onLoad() {
        core = new NoSignaturesCore(SpigotPacketEventsBuilder.build(this));
    }

    @Override
    public void onEnable() {
        core.start();
    }

    @Override
    public void onDisable() {
        core.stop();
    }
}
