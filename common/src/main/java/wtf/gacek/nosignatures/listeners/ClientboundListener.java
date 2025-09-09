package wtf.gacek.nosignatures.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import wtf.gacek.nosignatures.Utils;

public class ClientboundListener implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type.getSide() != PacketSide.SERVER) return;
        boolean updated = false;

        switch (type) {
            case PacketType.Play.Server.DELETE_CHAT -> { // The signature should always be set to 0, so I think this is useless?
                event.setCancelled(true);
                return;
            }
            case PacketType.Play.Server.PLAYER_CHAT_HEADER -> { // TODO: Figure out how to trigger this packet 😭😭😭
                System.out.printf(
                        "OMG OMG PLAYER CHAT HEADER SENT!!!!!!!!!! PACKET: %s (%d), SERVER VERSION: %s (%d), USER VERSION: %s (%d)\n",
                        event.getPacketType().getName(),
                        event.getPacketId(),
                        event.getServerVersion().getReleaseName(),
                        event.getServerVersion().getProtocolVersion(),
                        event.getUser().getClientVersion().getReleaseName(),
                        event.getUser().getClientVersion().getProtocolVersion()
                );
                WrapperPlayServerPlayerChatHeader packet = new WrapperPlayServerPlayerChatHeader(event);

                if (packet.getPreviousSignature().isPresent()) { // This field is optional, and therefore nullable.
                    packet.setPreviousSignature(null);
                    updated = true;
                }

                int signatureSize = packet.getSignature().length;
                if (signatureSize != 0) {
                    packet.setSignature(Utils.getEmptyArray(signatureSize));
                    if (!updated) updated = true;
                }

                int hashSize = packet.getHash().length;
                if (hashSize != 0) {
                    packet.setHash(Utils.getEmptyArray(hashSize));
                    if (!updated) updated = true;
                }
            }
//            case PacketType.Play.Server.PLAYER_INFO -> { // I think this will result in a crash, but I couldn't get it to send
//                WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event);
//                for (WrapperPlayServerPlayerInfo.PlayerData playerData : packet.getPlayerDataList()) {
//                    if (playerData.getSignatureData() != null) {
//                        playerData.setSignatureData(null);
//                        if (!updated) updated = true;
//                    }
//                }
//            }
            case PacketType.Play.Server.PLAYER_INFO_UPDATE -> { // Just pretend we never got the keys in the first place. The client is at fault here, I swear!
                WrapperPlayServerPlayerInfoUpdate packet = new WrapperPlayServerPlayerInfoUpdate(event);
                if (packet.getActions().contains(WrapperPlayServerPlayerInfoUpdate.Action.INITIALIZE_CHAT)) {
                    // we can simply remove the action, and PacketEvents won't try to add the chat sessions
                    packet.getActions().remove(WrapperPlayServerPlayerInfoUpdate.Action.INITIALIZE_CHAT);

                    if (packet.getActions().isEmpty()) { // Don't send an empty packet
                        event.setCancelled(true);
                    } else {
                        updated = true;
                    }
                }
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
