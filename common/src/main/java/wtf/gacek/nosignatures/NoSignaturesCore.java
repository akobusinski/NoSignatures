package wtf.gacek.nosignatures;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import wtf.gacek.nosignatures.listeners.ClientboundListener;
import wtf.gacek.nosignatures.listeners.ServerboundListener;

public class NoSignaturesCore {
    public NoSignaturesCore(PacketEventsAPI<?> packetEventsAPI) {
        PacketEvents.setAPI(packetEventsAPI);
        PacketEvents.getAPI().load();
        registerListeners();
    }

    public NoSignaturesCore() {
        registerListeners();
    }

    private void registerListeners() {
        EventManager eventManager = PacketEvents.getAPI().getEventManager();

        eventManager.registerListener(new ClientboundListener(), PacketListenerPriority.NORMAL);
        eventManager.registerListener(new ServerboundListener(), PacketListenerPriority.NORMAL);
    }

    public void start() {
        PacketEvents.getAPI().init();
    }

    public void stop() {
        PacketEvents.getAPI().terminate();
    }
}
