package wtf.gacek.nosignatures;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id=BuildConstants.ID,
        name=BuildConstants.NAME,
        version=BuildConstants.VERSION,
        description=BuildConstants.DESCRIPTION,
        authors=BuildConstants.AUTHOR,
        dependencies={
                @Dependency(id = "packetevents")
        }
)
public class NoSignaturesVelocity {
    private final NoSignaturesCore core;

    @Inject
    public NoSignaturesVelocity(ProxyServer server, PluginContainer container, Logger logger, @DataDirectory Path dataDirectory) {
        core = new NoSignaturesCore(VelocityPacketEventsBuilder.build(server, container, logger, dataDirectory));
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent e) {
        core.start();
    }

    @Subscribe
    public void onProxyShutdown(ProxyInitializeEvent e) {
        core.stop();
    }
}
