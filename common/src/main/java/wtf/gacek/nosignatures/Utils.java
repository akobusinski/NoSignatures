package wtf.gacek.nosignatures;

import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.util.crypto.MessageSignData;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;

import java.time.Instant;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static final HashMap<Integer, byte[]> EMPTY_ARRAYS = new HashMap<>();

    public static byte[] getEmptyArray(int size) {
        if (EMPTY_ARRAYS.containsKey(size)) {
            return EMPTY_ARRAYS.get(size);
        }

        byte[] array = new byte[size];
        Arrays.fill(array, (byte) 0x00);
        EMPTY_ARRAYS.put(size, array);

        return array;
    }

    public static LastSeenMessages.Update nullifyLastSeenMessagesUpdate(LastSeenMessages.Update old) {
        return new LastSeenMessages.Update(
                0,
                BitSet.valueOf(getEmptyArray(old.getAcknowledged().length())),
                (byte) 0
        );
    }

    public static LastSeenMessages.LegacyUpdate nullifyLastSeenMessagesUpdate(LastSeenMessages.LegacyUpdate old) {
        return new LastSeenMessages.LegacyUpdate(new LastSeenMessages(List.of()), null);
    }

    public static MessageSignData nullifyMessageSignData(MessageSignData old) {
        return new MessageSignData(
                nullifySaltSignature(old.getSaltSignature()),
                Instant.now(),
                false
        );
    }

    public static SaltSignature nullifySaltSignature(SaltSignature old) {
        return new SaltSignature(0, getEmptyArray(old.getSignature().length));
    }
}
