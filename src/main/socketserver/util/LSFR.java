package socketserver.util;

import java.nio.ByteBuffer;

public class LSFR {

    private String seed; // The authentication token (512 bytes)
    private byte[] state;
    private long shiftcounter = 0;

    public LSFR(String s) {
        if (s.length() != 128) {
            throw new RuntimeException("LSFR seed must be 128 chars long");
        }
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

    public LSFR(byte[] state, long shift) {
        this.state = state;
        shiftcounter = shift;
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
        //Start with empty state
        byte[] result = new byte[32];
        //Get binary representation of seed
        byte[] stringBytes = seed.getBytes();
        //To introduce more entropy, check if the first byte is even or odd.
        if (stringBytes[0] % 2 == 0) {
            result[0] = (byte)0xAA;
        } else {
            result[0] = (byte)0x55;
        }

        byte b;
        for (int i=0,j=0; i < 32; i++) {
            //All bytes in the result depend on previous bytes
            b = result[j++-i];
            //Xor with corresponding string bytes
            b ^= stringBytes[i];
            //The string has more bytes than the states, so each state byte depends on a number of string bytes
            for (int k = 0; k < 4; k++) {
                b ^= stringBytes[i + 32*k];
            }

            result[i] = b;
        }

        state = result;
    }

    public String getStateString() {
        return new String(state);
    }

    public long getShiftCount() {
        return shiftcounter;
    }

}
