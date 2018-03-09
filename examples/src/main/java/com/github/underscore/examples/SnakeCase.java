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
package com.github.underscore.examples;

import java.util.*;

/**
 * Examples for underscore-java.
 *
 * @author Valentyn Kolesnikov
 */
public class SnakeCase {
    private static final java.util.regex.Pattern RE_LATIN_1 = java.util.regex.Pattern.compile(
        "[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static final Map<String, String> DEBURRED_LETTERS = new LinkedHashMap<String, String>() { {
        put("\u00c0", "A"); put("\u00c1", "A"); put("\u00c2", "A"); put("\u00c3", "A");
        put("\u00c4", "A"); put("\u00c5", "A");
        put("\u00e0", "a"); put("\u00e1", "a"); put("\u00e2", "a"); put("\u00e3", "a");
        put("\u00e4", "a"); put("\u00e5", "a");
        put("\u00c7", "C"); put("\u00e7", "c");
        put("\u00d0", "D"); put("\u00f0", "d");
        put("\u00c8", "E"); put("\u00c9", "E"); put("\u00ca", "E"); put("\u00cb", "E");
        put("\u00e8", "e"); put("\u00e9", "e"); put("\u00ea", "e"); put("\u00eb", "e");
        put("\u00cC", "I"); put("\u00cd", "I"); put("\u00ce", "I"); put("\u00cf", "I");
        put("\u00eC", "i"); put("\u00ed", "i"); put("\u00ee", "i"); put("\u00ef", "i");
        put("\u00d1", "N"); put("\u00f1", "n");
        put("\u00d2", "O"); put("\u00d3", "O"); put("\u00d4", "O"); put("\u00d5", "O");
        put("\u00d6", "O"); put("\u00d8", "O");
        put("\u00f2", "o"); put("\u00f3", "o"); put("\u00f4", "o"); put("\u00f5", "o");
        put("\u00f6", "o"); put("\u00f8", "o");
        put("\u00d9", "U"); put("\u00da", "U"); put("\u00db", "U"); put("\u00dc", "U");
        put("\u00f9", "u"); put("\u00fa", "u"); put("\u00fb", "u"); put("\u00fc", "u");
        put("\u00dd", "Y"); put("\u00fd", "y"); put("\u00ff", "y");
        put("\u00c6", "Ae"); put("\u00e6", "ae");
        put("\u00de", "Th"); put("\u00fe", "th");
        put("\u00df", "ss");
    } };
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde\\u0400-\\u04FF]";
    private static String lower = "[a-z\\xdf-\\xf6\\xf8-\\xff]+";
    private static java.util.regex.Pattern reWords = java.util.regex.Pattern.compile(
        upper + "+(?=" + upper + lower + ")|" + upper + "?" + lower + "|" + upper + "+|[0-9]+");

    private static String baseToString(String value) {
        return value == null ? "" : value;
    }

    public static String deburr(final String string) {
        final String localString = baseToString(string);
        final StringBuilder sb = new StringBuilder();
        for (final String str : localString.split("")) {
            if (RE_LATIN_1.matcher(str).matches()) {
                sb.append(DEBURRED_LETTERS.get(str));
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public static List<String> words(final String string) {
        final String localString = baseToString(string);
        final List<String> result = new ArrayList<String>();
        final java.util.regex.Matcher matcher = reWords.matcher(localString);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static Function<String, String> createCompounder(
        final Function3<String, String, Integer, String> callback) {
        return new Function<String, String>() {
            public String apply(final String string) {
                int index = -1;
                List<String> array = words(deburr(string));
                int length = array.size();
                String result = "";

                while (++index < length) {
                    result = callback.apply(result, array.get(index), index);
                }
                return result;
            }
        };
    }

/*
_.snakeCase('Foo Bar');
=> 'foo_bar'

_.snakeCase('fooBar');
=> 'foo_bar'

_.snakeCase('--foo-bar');
=> 'foo_bar'
*/
    public static String snakeCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                return result + (index > 0 ? "_" : "") + word.toLowerCase(Locale.getDefault());
            }
        }).apply(string);
    }
}
