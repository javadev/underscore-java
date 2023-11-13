/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2023 Valentyn Kolesnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.underscore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class Base32Test {

    @Test
    void decode() {
        assertEquals("!", Base32.decode("EE"));
        assertEquals("-Hello world!+-", Base32.decode("FVEGKbDMNcQHObbSNRSCCKZN"));
    }

    @Test
    void decodeEmpty() {
        assertEquals("", Base32.decode(""));
    }

    @Test
    void decodeIllegal() {
        assertThrows(Base32.DecodingException.class, () -> Base32.decode("EE!"));
    }

    @Test
    void encode() {
        assertEquals("EE", Base32.encode("!"));
        assertEquals("FVEGKbDMNcQHObbSNRSCCKZN", Base32.encode("-Hello world!+-"));
    }

    @Test
    void encodeEmpty() {
        assertEquals("", Base32.encode(""));
    }
}