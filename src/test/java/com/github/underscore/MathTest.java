/*
 * The MIT License (MIT)
 *
 * Copyright 2015 Valentyn Kolesnikov
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

import java.math.*;
import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class MathTest {

/*
_.sum([1, 2, 3]);
=> 6
_.sum([0.2, 0.1, Math.PI]);
=> Math.PI + 0.3
_.sum([0, 14, 0.2]);
=> 14.2
_.sum([-1, -2, -3]);
=> -6
*/
    @Test
    public void sum() {
        final Byte result = $.sum(asList((byte) 1, (byte) 2, (byte) 3));
        assertEquals("6", result.toString());
        final Double result2 = $.sum(asList((double) 1, (double) 2, (double) 3));
        assertEquals("6.0", result2.toString());
        final Float result3 = $.sum(asList((float) 1, (float) 2, (float) 3));
        assertEquals("6.0", result3.toString());
        final Integer result4 = $.sum(asList((int) 1, (int) 2, (int) 3));
        assertEquals("6", result4.toString());
        final Long result5 = $.sum(asList((long) 1, (long) 2, (long) 3));
        assertEquals("6", result5.toString());
        final Short result6 = $.sum(asList((short) 1, (short) 2, (short) 3));
        assertEquals("6", result6.toString());
        final BigDecimal result7 = $.sum(asList(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
        assertEquals("6", result7.toString());
        final BigInteger result8 = $.sum(asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)));
        assertEquals("6", result8.toString());
        final Integer result9 = $.sum(asList((Integer) null));
        assertEquals(null, result9);
        final Integer result10 = $.sum(asList(1, (Integer) null));
        assertEquals("1", result10.toString());
        final Double result11 = $.sum(asList((double) 0.2, (double) 0.1, Math.PI));
        assertEquals(Double.valueOf(0.2 + 0.1 + Math.PI).toString(), result11.toString());
        final Double result12 = $.sum(asList((double) 0, (double) 14, (double) 0.2));
        assertEquals("14.2", result12.toString());
        final Integer result13 = $.sum(asList((int) -1, (int) -2, (int) -3));
        assertEquals("-6", result13.toString());
        final Integer resultChain = (Integer) $.chain(asList((int) 1, (int) 2, (int) 3)).sum().item();
        assertEquals("6", resultChain.toString());
        final Number resultObj = new $(asList((int) 1, (int) 2, (int) 3)).sum();
        assertEquals("6", resultObj.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sumError() {
        class MyNumber extends Number {
            public int intValue() {
                return 0;
            }
            public long longValue() {
                return 0;
            }
            public float floatValue() {
                return 0;
            }
            public double doubleValue() {
                return 0;
            }
        }
        $.sum(asList(new MyNumber(), new MyNumber()));
    }

/*
_.mean([0, 0.5, 1]);
=> 0.5
_.mean([0, 1, 2]);
=> 1
*/
    @Test
    public void mean() {
        final Double result = $.mean(asList((double) 0, (double) 0.5, (double) 1));
        assertEquals("0.5", result.toString());
        final Double result2 = $.mean(asList((long) 0, (long) 1, (long) 2));
        assertEquals("1.0", result2.toString());
    }

/*
_.median([0, 0, 0, 0, 5]);
=> 0
_.median([0, 0, 1, 2, 5]);
=> 1
_.median([0, 0, 1, 2]);
=> 0.5
_.median([0, 0, 1, 2, 3, 4]);
=> 1.5
*/
    @Test
    public void median() {
        final Double result = $.median(asList((int) 0, (int) 0, (int) 0, (int) 0, (int) 5));
        assertEquals("0.0", result.toString());
        final Double result2 = $.median(asList((int) 0, (int) 0, (int) 1, (int) 2, (int) 5));
        assertEquals("1.0", result2.toString());
        final Double result3 = $.median(asList((int) 0, (int) 0, (int) 1, (int) 2));
        assertEquals("0.5", result3.toString());
        final Double result4 = $.median(asList((int) 0, (int) 0, (int) 1, (int) 2, (int) 3, (int) 4));
        assertEquals("1.5", result4.toString());
    }
}
