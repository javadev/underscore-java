/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2020 Valentyn Kolesnikov
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
package com.github.underscore.lodash;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class MathTest {

    @Test
    public void average() {
        final Double result = U.average(asList((byte) 1, (byte) 2, (byte) 3));
        assertEquals("2.0", result.toString());
        final Double resultFunc = U.average(asList((byte) 1, (byte) 2, (byte) 3), item -> (byte) (item * 2));
        assertEquals("4.0", resultFunc.toString());
        final Double resultFunc2 = U.average(asList((Byte) null), item -> item);
        assertNull(resultFunc2);
        assertEquals("2.0", result.toString());
    }

    @Test
    public void average2() {
        final Double result2 = U.average(asList((double) 1, (double) 2, (double) 3));
        assertEquals("2.0", result2.toString());
        final Double result3 = U.average(asList((float) 1, (float) 2, (float) 3));
        assertEquals("2.0", result3.toString());
        final Double result4 = U.average(asList((int) 1, (int) 2, (int) 3));
        assertEquals("2.0", result4.toString());
        final Double result5 = U.average(asList((long) 1, (long) 2, (long) 3));
        assertEquals("2.0", result5.toString());
        final Double result6 = U.average(asList((short) 1, (short) 2, (short) 3));
        assertEquals("2.0", result6.toString());
        final Double result7 = U.average(asList(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
        assertEquals("2.0", result7.toString());
        final Double result8 = U.average(asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)));
        assertEquals("2.0", result8.toString());
        final Double result9 = U.average(asList((Integer) null));
        assertNull(result9);
    }

    @Test
    public void average3() {
        final Double result10 = U.average(asList(1, (Integer) null));
        assertEquals("0.5", result10.toString());
        final Double result11 = U.average(asList((double) 0.2, (double) 0.1, Math.PI));
        assertEquals(Double.valueOf((0.2 + 0.1 + Math.PI) / 3).toString(), result11.toString());
        final Double result12 = U.average(asList((double) 0, (double) 14, (double) 0.2));
        assertEquals(Double.valueOf((0 + 14 + 0.2) / 3), result12);
        final Double result13 = U.average(asList((int) -1, (int) -2, (int) -3));
        assertEquals("-2.0", result13.toString());
        final Double result14 = U.average(new Integer[] {1, 2, 3});
        assertEquals("2.0", result14.toString());
        final Double result15 = U.average(new Double[] {1.0, 2.0, 3.0});
        assertEquals("2.0", result15.toString());
        final Double result16 = U.average(new Float[] {(float) 1.0, (float) 2.0, (float) 3.0});
        assertEquals("2.0", result16.toString());
        final Double result17 = U.average(new Short[] {1, 2, 3});
        assertEquals("2.0", result17.toString());
        final Double result18 = U.average(new Long[] {(long) 1, (long) 2, (long) 3});
        assertEquals("2.0", result18.toString());
        final Double result19 = U.average(new BigInteger[] {BigInteger.valueOf(1), BigInteger.valueOf(2),
                BigInteger.valueOf(3)});
        assertEquals("2.0", result19.toString());
    }

    @Test
    public void average4() {
        final Double result20 = U.average(new BigDecimal[] {BigDecimal.valueOf(1), BigDecimal.valueOf(2),
                BigDecimal.valueOf(3)});
        assertEquals("2.0", result20.toString());
        final Double result21 = U.average(null, BigDecimal.valueOf(1));
        assertNull(result21);
        final Double result22 = U.average(BigDecimal.valueOf(1), null);
        assertNull(result22);
        final Double result23 = U.average(BigDecimal.valueOf(2), BigDecimal.valueOf(4));
        assertEquals("3.0", result23.toString());
        final Double result24 = U.average(BigInteger.valueOf(1), null);
        assertNull(result24);
        final Double result25 = U.average(null, BigInteger.valueOf(1));
        assertNull(result25);
        final Double result26 = U.average(BigInteger.valueOf(2), BigInteger.valueOf(4));
        assertEquals("3.0", result26.toString());
        final Double result27 = U.average((byte) 1, null);
        assertNull(result27);
        final Double result28 = U.average(null, (byte) 1);
        assertNull(result28);
    }

    @Test
    public void average5() {
        final Double result29 = U.average((byte) 2, (byte) 4);
        assertEquals("3.0", result29.toString());
        final Double result30 = U.average(Double.valueOf(2), null);
        assertNull(result30);
        final Double result31 = U.average(null, Double.valueOf(2));
        assertNull(result31);
        final Double result32 = U.average(Double.valueOf(2), Double.valueOf(4));
        assertEquals("3.0", result32.toString());
        final Double result33 = U.average(Float.valueOf(2), null);
        assertNull(result33);
        final Double result34 = U.average(null, Float.valueOf(2));
        assertNull(result34);
        final Double result35 = U.average(Float.valueOf(2), Float.valueOf(4));
        assertEquals("3.0", result35.toString());
        final Double result36 = U.average(2, null);
        assertNull(result36);
    }

    @Test
    public void average6() {
        final Double result37 = U.average(null, 2);
        assertNull(result37);
        final Double result38 = U.average(2, 4);
        assertEquals("3.0", result38.toString());
        final Double result39 = U.average(Long.valueOf(2), null);
        assertNull(result39);
        final Double result40 = U.average(null, Long.valueOf(2));
        assertNull(result40);
        final Double result41 = U.average(Long.valueOf(2), Long.valueOf(4));
        assertEquals("3.0", result41.toString());
        final Double result42 = U.average(Long.valueOf(2), Long.valueOf(4));
        assertEquals("3.0", result42.toString());
        final Double result43 = U.average(new Integer[] {(Integer) null});
        assertNull(result43);
    }

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
    @SuppressWarnings("unchecked")
    @Test
    public void sum() {
        final Byte result = U.sum(asList((byte) 1, (byte) 2, (byte) 3));
        assertEquals("6", result.toString());
        final Byte resultFunc = U.sum(asList((byte) 1, (byte) 2, (byte) 3), item -> (byte) (item * 2));
        assertEquals("12", resultFunc.toString());
        final Double result2 = U.sum(asList((double) 1, (double) 2, (double) 3));
        assertEquals("6.0", result2.toString());
        final Float result3 = U.sum(asList((float) 1, (float) 2, (float) 3));
        assertEquals("6.0", result3.toString());
        final Integer result4 = U.sum(asList((int) 1, (int) 2, (int) 3));
        assertEquals("6", result4.toString());
        final Long result5 = U.sum(asList((long) 1, (long) 2, (long) 3));
        assertEquals("6", result5.toString());
        final Short result6 = U.sum(asList((short) 1, (short) 2, (short) 3));
        assertEquals("6", result6.toString());
        final BigDecimal result7 = U.sum(asList(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3)));
        assertEquals("6", result7.toString());
        final BigInteger result8 = U.sum(asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)));
        assertEquals("6", result8.toString());
        final Integer result9 = U.sum(asList((Integer) null));
        assertNull(result9);
        final Integer result10 = U.sum(asList(1, (Integer) null));
        assertEquals("1", result10.toString());
        final Double result11 = U.sum(asList((double) 0.2, (double) 0.1, Math.PI));
        assertEquals(Double.valueOf(0.2 + 0.1 + Math.PI).toString(), result11.toString());
        final Double result12 = U.sum(asList((double) 0, (double) 14, (double) 0.2));
        assertEquals("14.2", result12.toString());
        final Integer result13 = U.sum(asList((int) -1, (int) -2, (int) -3));
        assertEquals("-6", result13.toString());
        final Integer resultChain = (Integer) U.chain(asList((int) 1, (int) 2, (int) 3)).sum().item();
        assertEquals("6", resultChain.toString());
        final Integer result14 = U.sum(new Integer[] {1, 2, 3});
        assertEquals("6", result14.toString());
        final Double result15 = U.sum(new Double[] {1.0, 2.0, 3.0});
        assertEquals("6.0", result15.toString());
        final Float result16 = U.sum(new Float[] {(float) 1.0, (float) 2.0, (float) 3.0});
        assertEquals("6.0", result16.toString());
        final Short result17 = U.sum(new Short[] {1, 2, 3});
        assertEquals("6", result17.toString());
        final Long result18 = U.sum(new Long[] {(long) 1, (long) 2, (long) 3});
        assertEquals("6", result18.toString());
        final BigInteger result19 = U.sum(new BigInteger[] {BigInteger.valueOf(1), BigInteger.valueOf(2),
                BigInteger.valueOf(3)});
        assertEquals("6", result19.toString());
        final BigDecimal result20 = U.sum(new BigDecimal[] {BigDecimal.valueOf(1), BigDecimal.valueOf(2),
                BigDecimal.valueOf(3)});
        assertEquals("6", result20.toString());
        final Integer result21 = U.sum(new Integer[] {1, 2, null});
        assertEquals("3", result21.toString());
        final Integer resultChainFunc = (Integer) U.chain(asList((int) 1, (int) 2, (int) 3)).sum(
                item -> item * 2).item();
        assertEquals("12", resultChainFunc.toString());
        final Number resultObj = new U(asList((int) 1, (int) 2, (int) 3)).sum();
        assertEquals("6", resultObj.toString());
        final Number resultObjFunc = new U(asList((byte) 1, (byte) 2, (byte) 3)).sum(
                (Function<Number, Number>) item -> item.intValue() * 2);
        assertEquals("12", resultObjFunc.toString());
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
        U.sum(asList(new MyNumber(), new MyNumber()));
    }

/*
_.subtract(1, 2);
=> -1
_.subtract(1, 2, 3);
=> -4
_.subtract();
=> null
*/
    @SuppressWarnings("unchecked")
    @Test
    public void subtract() {
        assertEquals("-1", U.subtract((byte) 1, (byte) 2).toString());
        assertEquals("-1", U.subtract((short) 1, (short) 2).toString());
        assertEquals("-1", U.subtract((int) 1, (int) 2).toString());
        assertEquals("-1", U.subtract((long) 1, (long) 2).toString());
        assertEquals("-1.0", U.subtract((float) 1, (float) 2).toString());
        assertEquals("-1.0", U.subtract((double) 1, (double) 2).toString());
        assertEquals("-1", U.subtract((byte) 1, (byte) 2).toString());
        assertEquals("-1", U.subtract((short) 1, (short) 2).toString());
        assertEquals("-1", U.subtract(1, 2).toString());
        assertEquals("-1", U.subtract(1L, 2L).toString());
        assertEquals("-1.0", U.subtract(1f, 2f).toString());
        assertEquals("-1.0", U.subtract(1d, 2d).toString());
        assertEquals("-1", U.subtract(BigDecimal.valueOf(1), BigDecimal.valueOf(2)).toString());
        assertEquals("-1", U.subtract(BigInteger.valueOf(1), BigInteger.valueOf(2)).toString());
        assertEquals("-1", U.subtract((Number) 1, (Number) 2).toString());
        assertEquals("-4", U.subtract((int) 1, (int) 2, (int) 3).toString());
        assertEquals("1", U.subtract((int) 1).toString());
        assertEquals(null, U.subtract());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void subtractError() {
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
        U.subtract(new MyNumber(), new MyNumber());
    }

/*
_.mean([0, 0.5, 1]);
=> 0.5
_.mean([0, 1, 2]);
=> 1
*/
    @SuppressWarnings("unchecked")
    @Test
    public void mean() {
        final Double result = U.mean(asList((double) 0, (double) 0.5, (double) 1));
        assertEquals("0.5", result.toString());
        final Double resultObj = new U(asList((double) 0, (double) 0.5, (double) 1)).mean();
        assertEquals("0.5", resultObj.toString());
        final Double resultChain = (Double) U.chain(asList((double) 0, (double) 0.5, (double) 1)).mean().item();
        assertEquals("0.5", resultChain.toString());
        final Double result2 = U.mean(asList((long) 0, (long) 1, (long) 2));
        assertEquals("1.0", result2.toString());
        final Double result3 = U.mean(Arrays.<Double>asList());
        assertEquals("0.0", result3.toString());
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
    @SuppressWarnings("unchecked")
    @Test
    public void median() {
        final Double result = U.median(asList((int) 0, (int) 0, (int) 0, (int) 0, (int) 5));
        assertEquals("0.0", result.toString());
        final Double resultObj = new U(asList((int) 0, (int) 0, (int) 0, (int) 0, (int) 5)).median();
        assertEquals("0.0", resultObj.toString());
        final Double resultChain = (Double) U.chain(asList((int) 0, (int) 0, (int) 0, (int) 0, (int) 5))
            .median().item();
        assertEquals("0.0", resultChain.toString());
        final Double result2 = U.median(asList((int) 0, (int) 0, (int) 1, (int) 2, (int) 5));
        assertEquals("1.0", result2.toString());
        final Double result3 = U.median(asList((int) 0, (int) 0, (int) 1, (int) 2));
        assertEquals("0.5", result3.toString());
        final Double result4 = U.median(asList((int) 0, (int) 0, (int) 1, (int) 2, (int) 3, (int) 4));
        assertEquals("1.5", result4.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void medianForEmpty() {
        U.median(new ArrayList<Double>());
    }

    // http://stackoverflow.com/questions/27772432/is-there-a-underscore-js-lib-for-java
    @Test
    public void sumOfInt() {
/*
String[] words = {"Gallinule", "Escambio", "Aciform", "Entortilation", "Extensibility"};
int sum = Arrays.stream(words)
        .filter(w -> w.startsWith("E"))
        .mapToInt(w -> w.length())
        .sum();
System.out.println("Sum of letters in words starting with E... " + sum);
*/
        String[] words = {"Gallinule", "Escambio", "Aciform", "Entortilation", "Extensibility"};
        int sum = (Integer) U.chain(asList(words))
            .filter(item -> item.startsWith("E"))
            .map(String::length)
            .sum().item();
        assertEquals(34, sum);
    }

    @Test
    public void createLruCache() {
        new U.LruCache<Integer, String>(0);
        U.LruCache<Integer, String> cache = U.createLruCache(2);
        cache.put(0, "Value 0");
        assertEquals("Value 0", cache.get(0));
        assertNull(cache.get(1));
        cache.put(1, "Value 1");
        assertEquals("Value 1", cache.get(1));
        cache.put(1, "Value 1+");
        assertEquals("Value 1+", cache.get(1));
        cache.put(2, "Value 2");
        assertEquals("Value 2", cache.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createPermutationWithRepetition() {
        List<List<String>> result = U.createPermutationWithRepetition(asList("apple", "orange"), 3);
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", result.toString());
        List<List<String>> result2 = new U(asList("apple", "orange")).createPermutationWithRepetition(3);
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", result2.toString());
        List<List<String>> resultChain = U.chain(asList("apple", "orange")).createPermutationWithRepetition(3).value();
        assertEquals("[[apple, apple, apple],"
                   + " [orange, apple, apple],"
                   + " [apple, orange, apple],"
                   + " [orange, orange, apple],"
                   + " [apple, apple, orange],"
                   + " [orange, apple, orange],"
                   + " [apple, orange, orange],"
                   + " [orange, orange, orange]]", resultChain.toString());
    }

    @Test
    public void gcd() {
        assertEquals(12, U.gcd(24, 36));
        assertEquals(5, U.gcd(5, 0));
        assertEquals(12, U.findGcd(24, 36));
        assertEquals(5, U.findGcd(5, 0));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void main() {
        U.main(new String[] {});
        new U("");
        new U(asList()).chain();
        U.chain(new HashSet<String>());
        U.chain(new String[] {});
    }
}
