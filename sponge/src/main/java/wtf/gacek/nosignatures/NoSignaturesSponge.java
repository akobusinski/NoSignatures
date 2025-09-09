package wtf.gacek.nosignatures;

import com.google.inject.Inject;
import io.github.retrooper.packetevents.sponge.factory.SpongePacketEventsBuilder;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("nosignatures")
public class NoSignaturesSponge {
    private final NoSignaturesCore core;

    @Inject
    public NoSignaturesSponge(PluginContainer container) {
        core = new NoSignaturesCore(SpongePacketEventsBuilder.build(container));
    }

    @Listener
    public void onServerStart(StartingEngineEvent<Server> event) {
        core.start();
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event) {
        core.stop();
    }
}
