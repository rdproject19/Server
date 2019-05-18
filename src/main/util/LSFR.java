package main.util;

import java.nio.ByteBuffer;

public class LSFR {

    private String seed; // The authentication token (512 bytes)
    private byte[] state;
    private long shiftcounter = 0;

    public LSFR(String s) {
        this.seed = s;
        convertSeedToBits();
    }

    public LSFR(String s, long shiftcount) {
        this(s);
        for (long i = 0; i < shiftcount; i++)
        {
            shift();
        }
    }

    /**
     * Shifts the LSFR by one position
     * @return The new state of the LSFR as an Integer
     */
    public int shift() {
        int[] bytestouse = {6, 13, 15, 17, 21, 26, 30};
        byte newbyte = state[4];
        for (int b : bytestouse) {
            newbyte ^= state[b];
        }

        byte[] newstate = new byte[32];
        for (int i = 0; i < 31; i++) {
            newstate[i] = state[i+1];
        }
        newstate[31] = newbyte;

        state = newstate;
        shiftcounter++;
        return ByteBuffer.wrap(newstate).getInt();
    }

    /**
     * To convert the seed bytes to the 32 bits intial state for the LSFR
     */
    private void convertSeedToBits() {
        byte[] result = new byte[32];
        byte[] stringBytes = seed.getBytes();
        if (stringBytes[0] % 2 == 0) {
            result[0] = (byte)0xAA;
        } else {
            result[0] = (byte)0x55;
        }

        byte b;
        for (int i=0,j=0; i < 32; i++) {
            b = result[j++-i];
            b ^= stringBytes[i];
            for (int k = 0; k < 4; k++) {
                b ^= stringBytes[i + 32*k];
            }

            result[i] = b;
        }

        state = result;
    }

}
