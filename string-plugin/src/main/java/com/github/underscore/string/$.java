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
package com.github.underscore.string;

import java.util.*;
import com.github.underscore.Function1;
import com.github.underscore.Function3;

public class $<T> extends com.github.underscore.$<T> {
    private static final java.util.regex.Pattern RE_LATIN_1 = java.util.regex.Pattern.compile(
        "[\\xc0-\\xd6\\xd8-\\xde\\xdf-\\xf6\\xf8-\\xff]");
    private static Map<String, String> deburredLetters = new LinkedHashMap<String, String>() { {
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
    private static String upper = "[A-Z\\xc0-\\xd6\\xd8-\\xde]";
    private static String lower = "[a-z\\xdf-\\xf6\\xf8-\\xff]+";
    private static java.util.regex.Pattern reWords = java.util.regex.Pattern.compile(
        upper + "+(?=" + upper + lower + ")|" + upper + "?" + lower + "|" + upper + "+|[0-9]+");

    public $(final Iterable<T> iterable) {
        super(iterable);
    }

    public $(final String string) {
        super(string);
    }

    public static class Chain<T> extends com.github.underscore.$.Chain<T> {
        public Chain(final T item) {
            super(item);
        }
        public Chain(final List<T> list) {
            super(list);
        }

        public Chain<String> camelCase() {
            return new Chain<String>($.camelCase((String) item()));
        }

        public Chain<String> capitalize() {
            return new Chain<String>($.capitalize((String) item()));
        }

        public Chain<String> deburr() {
            return new Chain<String>($.deburr((String) item()));
        }

        public Chain<Boolean> endsWith(final String target) {
            return new Chain<Boolean>($.endsWith((String) item(), target));
        }

        public Chain<Boolean> endsWith(final String target, final Integer position) {
            return new Chain<Boolean>($.endsWith((String) item(), target, position));
        }

        public Chain<String> kebabCase() {
            return new Chain<String>($.kebabCase((String) item()));
        }

        public Chain<String> snakeCase() {
            return new Chain<String>($.snakeCase((String) item()));
        }

        public Chain<String> startCase() {
            return new Chain<String>($.startCase((String) item()));
        }

        public Chain<Boolean> startsWith(final String target) {
            return new Chain<Boolean>($.startsWith((String) item(), target));
        }

        public Chain<Boolean> startsWith(final String target, final Integer position) {
            return new Chain<Boolean>($.startsWith((String) item(), target, position));
        }

        public Chain<List<String>> words() {
            return new Chain<List<String>>($.words((String) item()));
        }
    }

    public static Chain chain(final String item) {
        return new $.Chain<String>(item);
    }

    public static <T> Chain chain(final List<T> list) {
        return new $.Chain<T>(list);
    }

    public static <T> Chain chain(final Set<T> list) {
        return new $.Chain<T>(newArrayList(list));
    }

    public static <T> Chain chain(final T ... list) {
        return new $.Chain<T>(Arrays.asList(list));
    }

    public static String camelCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? (word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1)) : localWord);
            }
        }).apply(string);
    }

    public static String capitalize(final String string) {
        final String localString = baseToString(string);
        return localString.isEmpty() ? "" : (localString.substring(0, 1).toUpperCase(Locale.getDefault())
            + (localString.length() > 1 ? localString.substring(1) : ""));
    }

    private static String baseToString(String value) {
        return value == null ? "" : value;
    }

    public static String deburr(final String string) {
        final String localString = baseToString(string);
        final StringBuilder sb = new StringBuilder();
        for (final String str : localString.split("")) {
            if (RE_LATIN_1.matcher(str).matches()) {
                sb.append(deburredLetters.get(str));
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

    private static Function1<String, String> createCompounder(
        final Function3<String, String, Integer, String> callback) {
        return new Function1<String, String>() {
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

    public static boolean endsWith(final String string, final String target) {
        return endsWith(string, target, null);
    }

    public static boolean endsWith(final String string, final String target, final Integer position) {
        if (string == null || target == null) {
            return false;
        }
        final String localString = baseToString(string);

        final int length = localString.length();
        final int localPosition = position == null ? length
          : Math.min(position < 0 ? 0 : position, length);

        final int localPosition2 = localPosition - target.length();
      return localPosition2 >= 0 && localString.indexOf(target, localPosition2) == localPosition2;
    }

    public static String kebabCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? "-" : "") + word.toLowerCase(Locale.getDefault());
            }
        }).apply(string);
    }

    public static String snakeCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? "_" : "") + word.toLowerCase(Locale.getDefault());
            }
        }).apply(string);
    }

    public static String startCase(final String string) {
        return createCompounder(new Function3<String, String, Integer, String>() {
            public String apply(final String result, final String word, final Integer index) {
                final String localWord = word.toLowerCase(Locale.getDefault());
                return result + (index > 0 ? " " : "") + word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1);
            }
        }).apply(string);
    }

    public static boolean startsWith(final String string, final String target) {
        return startsWith(string, target, null);
    }

    public static boolean startsWith(final String string, final String target, final Integer position) {
        if (string == null || target == null) {
            return false;
        }
        final String localString = baseToString(string);

        final int length = localString.length();
        final int localPosition = position == null ? 0
          : Math.min(position < 0 ? 0 : position, length);

        return localString.lastIndexOf(target, localPosition) == localPosition;
    }

    public String camelCase() {
        return $.camelCase(getString().get());
    }

    public String capitalize() {
        return $.capitalize(getString().get());
    }

    public String deburr() {
        return $.deburr(getString().get());
    }

    public boolean endsWith(final String target) {
        return $.endsWith(getString().get(), target);
    }

    public boolean endsWith(final String target, final Integer position) {
        return $.endsWith(getString().get(), target, position);
    }

    public String kebabCase() {
        return $.kebabCase(getString().get());
    }

    public String snakeCase() {
        return $.snakeCase(getString().get());
    }

    public String startCase() {
        return $.startCase(getString().get());
    }

    public boolean startsWith(final String target) {
        return $.startsWith(getString().get(), target);
    }

    public boolean startsWith(final String target, final Integer position) {
        return $.startsWith(getString().get(), target, position);
    }

    public List<String> words() {
        return $.words(getString().get());
    }

    public static void main(String ... args) {
        final String message = "Underscore-java-string is a string plugin for underscore-java.\n\n"
            + "For docs, license, tests, and downloads, see: http://javadev.github.io/underscore-java";
        System.out.println(message);
    }
}
