package wtf.gacek.nosignatures;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class NoSignaturesFabric implements DedicatedServerModInitializer {
    private final NoSignaturesCore core = new NoSignaturesCore();

    @Override
    public void onInitializeServer() {
        core.start();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> core.stop());
    }
}
