/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2018 Valentyn Kolesnikov
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

import com.github.underscore.Function;
import com.github.underscore.Predicate;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    @SuppressWarnings("unchecked")
    @Test
    public void sum() {
        final Byte result = U.sum(asList((byte) 1, (byte) 2, (byte) 3));
        assertEquals("6", result.toString());
        final Byte resultFunc = U.sum(asList((byte) 1, (byte) 2, (byte) 3), new Function<Byte, Byte>() {
            public Byte apply(final Byte item) {
                return (byte) (item * 2);
            }
        });
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
        assertEquals(null, result9);
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
        final Integer resultChainFunc = (Integer) U.chain(asList((int) 1, (int) 2, (int) 3)).sum(
            new Function<Integer, Integer>() {
            public Integer apply(final Integer item) {
                return item * 2;
            }
        }).item();
        assertEquals("12", resultChainFunc.toString());
        final Number resultObj = new U(asList((int) 1, (int) 2, (int) 3)).sum();
        assertEquals("6", resultObj.toString());
        final Number resultObjFunc = new U(asList((byte) 1, (byte) 2, (byte) 3)).sum(new Function<Number, Number>() {
            public Number apply(final Number item) {
                return item.intValue() * 2;
            }
        });
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
    @SuppressWarnings("unchecked")
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
            .filter(new Predicate<String>() { public boolean test(String item) { return item.startsWith("E"); } })
            .map(new Function<String, Integer>() { public Integer apply(String item) { return item.length(); } })
            .sum().item();
        assertEquals(34, sum);
    }

    @Test
    public void createLRUCache() {
        new U.LRUCache<Integer, String>(0);
        U.LRUCache<Integer, String> cache = U.createLRUCache(2);
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
