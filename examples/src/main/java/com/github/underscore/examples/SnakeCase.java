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
    private static final Map<String, String> DEBURRED_LETTERS = new LinkedHashMap<String, String>();

    static {
        String[] deburredLetters = new String[] {
        "\u00c0", "A", "\u00c1", "A", "\u00c2", "A", "\u00c3", "A",
        "\u00c4", "A", "\u00c5", "A",
        "\u00e0", "a", "\u00e1", "a", "\u00e2", "a", "\u00e3", "a",
        "\u00e4", "a", "\u00e5", "a",
        "\u00c7", "C", "\u00e7", "c",
        "\u00d0", "D", "\u00f0", "d",
        "\u00c8", "E", "\u00c9", "E", "\u00ca", "E", "\u00cb", "E",
        "\u00e8", "e", "\u00e9", "e", "\u00ea", "e", "\u00eb", "e",
        "\u00cC", "I", "\u00cd", "I", "\u00ce", "I", "\u00cf", "I",
        "\u00eC", "i", "\u00ed", "i", "\u00ee", "i", "\u00ef", "i",
        "\u00d1", "N", "\u00f1", "n",
        "\u00d2", "O", "\u00d3", "O", "\u00d4", "O", "\u00d5", "O",
        "\u00d6", "O", "\u00d8", "O",
        "\u00f2", "o", "\u00f3", "o", "\u00f4", "o", "\u00f5", "o",
        "\u00f6", "o", "\u00f8", "o",
        "\u00d9", "U", "\u00da", "U", "\u00db", "U", "\u00dc", "U",
        "\u00f9", "u", "\u00fa", "u", "\u00fb", "u", "\u00fc", "u",
        "\u00dd", "Y", "\u00fd", "y", "\u00ff", "y",
        "\u00c6", "Ae", "\u00e6", "ae",
        "\u00de", "Th", "\u00fe", "th",
        "\u00df", "ss"};
        for (int index = 0; index < deburredLetters.length; index += 2) {
            DEBURRED_LETTERS.put(deburredLetters[index], deburredLetters[index + 1]);
        }
    }

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
