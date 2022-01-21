/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2022 Valentyn Kolesnikov
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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class Base32 {

    private static final Base32 INSTANCE = new Base32();

    private final char[] digits;
    private final int mask;
    private final int shift;
    private final Map<Character, Integer> charMap;

    private Base32() {
        digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef".toCharArray();
        mask = digits.length - 1;
        shift = Integer.numberOfTrailingZeros(digits.length);
        charMap = new HashMap<>();
        for (int index = 0; index < digits.length; index += 1) {
            charMap.put(digits[index], index);
        }
    }

    public static String decode(final String encoded) {
        return new String(INSTANCE.decodeInternal(encoded), StandardCharsets.UTF_8);
    }

    private byte[] decodeInternal(final String encoded) {
        if (encoded.length() == 0) {
            return new byte[0];
        }
        int encodedLength = encoded.length();
        int outLength = encodedLength * shift / 8;
        byte[] result = new byte[outLength];
        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;
        for (char c : encoded.toCharArray()) {
            if (!charMap.containsKey(c)) {
                throw new DecodingException("Illegal character: " + c);
            }
            buffer <<= shift;
            buffer |= charMap.get(c) & mask;
            bitsLeft += shift;
            if (bitsLeft >= 8) {
                result[next++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }
        return result;
    }

    public static String encode(final String data) {
        return INSTANCE.encodeInternal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeInternal(final byte[] data) {
        if (data.length == 0) {
            return "";
        }

        int outputLength = (data.length * 8 + shift - 1) / shift;
        StringBuilder result = new StringBuilder(outputLength);

        int buffer = data[0];
        int next = 1;
        int bitsLeft = 8;
        while (bitsLeft > 0 || next < data.length) {
            if (bitsLeft < shift) {
                if (next < data.length) {
                    buffer <<= 8;
                    buffer = buffer | (data[next++] & 0xff);
                    bitsLeft += 8;
                } else {
                    int pad = shift - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int index = mask & (buffer >> (bitsLeft - shift));
            bitsLeft -= shift;
            result.append(digits[index]);
        }
        return result.toString();
    }

    public static class DecodingException extends RuntimeException {

        public DecodingException(final String message) {
            super(message);
        }
    }
}
