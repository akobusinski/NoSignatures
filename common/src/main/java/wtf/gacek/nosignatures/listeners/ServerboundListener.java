package wtf.gacek.nosignatures.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import wtf.gacek.nosignatures.Utils;

public class ServerboundListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type.getSide() != PacketSide.CLIENT) return;
        boolean updated = false;

        switch (type) {
            case PacketType.Play.Client.CHAT_SESSION_UPDATE -> event.setCancelled(true); // Whaaaaaaaaaaaat?? I have to give you my public key??? That's so odd!
            // This will work only if the server does not have our key already, otherwise the player won't be able to send messages.
            case PacketType.Play.Client.CHAT_MESSAGE -> {
                WrapperPlayClientChatMessage packet = new WrapperPlayClientChatMessage(event);

                LastSeenMessages.Update lastSeen = packet.getLastSeenMessages();
                if (lastSeen != null) {
                    packet.setLastSeenMessages(Utils.nullifyLastSeenMessagesUpdate(lastSeen));
                    updated = true;
                }

                LastSeenMessages.LegacyUpdate legacyLastSeen = packet.getLegacyLastSeenMessages();
                if (legacyLastSeen != null) {
                    packet.setLegacyLastSeenMessages(Utils.nullifyLastSeenMessagesUpdate(legacyLastSeen));
                    if (!updated) updated = true;
                }

                MessageSignData signData = packet.getMessageSignData().orElse(null);
                if (signData != null) {
                    packet.setMessageSignData(Utils.nullifyMessageSignData(signData));
                    if (!updated) updated = true;
                }
            }
            case PacketType.Play.Client.CHAT_COMMAND -> { // Replace the signed chat command with an unsigned one.
                WrapperPlayClientChatCommand packet = new WrapperPlayClientChatCommand(event);
                WrapperPlayClientChatCommandUnsigned unsigned = new WrapperPlayClientChatCommandUnsigned(packet.getCommand());
                event.getUser().receivePacket(unsigned);

                event.setCancelled(true);
            }
            default -> {
                return;
            }
        }

        if (updated) {
            event.markForReEncode(true);
        }
    }
}
