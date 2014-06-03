package controllers;

import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

/**
 *
 */
public class UUIDUtil {
    /**
     * Generate a shortUUID
     *
     * @return
     */
    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        return Base64.encodeBase64URLSafeString(asByteArray(uuid));
    }

    /**
     * Convert UUID to a byte array
     *
     * @param uuid
     * @return
     */
    private static byte[] asByteArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return buffer;
    }
}
