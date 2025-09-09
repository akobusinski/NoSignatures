package wtf.gacek.nosignatures;

import io.github.retrooper.packetevents.bungee.factory.BungeePacketEventsBuilder;
import net.md_5.bungee.api.plugin.Plugin;

public class NoSignaturesBungeeCord extends Plugin {
    private NoSignaturesCore core;

    @Override
    public void onLoad() {
        core = new NoSignaturesCore(BungeePacketEventsBuilder.build(this));
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
