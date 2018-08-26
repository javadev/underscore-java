package com.github.underscore.lodash;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Base32Test {

    @Test
    public void decode() {
        assertEquals("!", Base32.decode("EE"));
        assertEquals("-Hello world!+-", Base32.decode("FVEGKbDMNcQHObbSNRSCCKZN"));
    }

    @Test
    public void decodeEmpty() {
        assertEquals("", Base32.decode(""));
    }

    @Test(expected = Base32.DecodingException.class)
    public void decodeIllegal() {
        Base32.decode("EE!");
    }

    @Test
    public void encode() {
        assertEquals("EE", Base32.encode("!"));
        assertEquals("FVEGKbDMNcQHObbSNRSCCKZN", Base32.encode("-Hello world!+-"));
    }

    @Test
    public void encodeEmpty() {
        assertEquals("", Base32.encode(""));
    }
}
