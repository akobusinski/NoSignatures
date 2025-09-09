package wtf.gacek.nosignatures;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class NoSignaturesFabricClient implements ClientModInitializer {
    private final NoSignaturesCore core = new NoSignaturesCore();

    @Override
    public void onInitializeClient() {
        core.start();

        ClientLifecycleEvents.CLIENT_STOPPING.register(server -> core.stop());
    }
}
