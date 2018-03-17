/*
 * The MIT License (MIT)
 *
 * Copyright 2016-2018 Valentyn Kolesnikov
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

import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import com.github.underscore.string.$.JsonStringBuilder;
import com.github.underscore.string.$.XmlStringBuilder;
import com.github.underscore.string.$.JsonJavaStringBuilder;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class StringTest {

/*
_.camelCase('Foo Bar');
=> 'fooBar'

_.camelCase('--foo-bar');
=> 'fooBar'

_.camelCase('__foo_bar__');
=> 'fooBar'
*/

    @Test
    public void camelCase() {
        assertEquals("fooBar", $.camelCase("Foo Bar"));
        assertEquals("fooBar", new $("Foo Bar").camelCase());
        assertEquals("fooBar", $.chain("Foo Bar").camelCase().item());
        assertEquals("fooBar", $.camelCase("--foo-bar"));
        assertEquals("fooBar", $.camelCase("__foo_bar__"));
        assertEquals("", $.camelCase(null));
        assertEquals("a", $.camelCase("\u00c0"));
    }

/*
_.lowerFirst('Fred');
// => 'fred'

_.lowerFirst('FRED');
// => 'fRED'
*/
    @Test
    public void lowerFirst() {
        assertEquals("fred", $.lowerFirst("Fred"));
        assertEquals("fred", new $("Fred").lowerFirst());
        assertEquals("fred", $.chain("Fred").lowerFirst().item());
        assertEquals("fRED", $.lowerFirst("FRED"));
    }

/*
_.upperFirst('fred');
// => 'Fred'

_.upperFirst('FRED');
// => 'FRED'
*/
    @Test
    public void upperFirst() {
        assertEquals("Fred", $.upperFirst("fred"));
        assertEquals("Fred", new $("fred").upperFirst());
        assertEquals("Fred", $.chain("fred").upperFirst().item());
        assertEquals("FRED", $.upperFirst("FRED"));
    }

/*
_.capitalize('fred');
=> 'Fred'
*/
    @Test
    public void capitalize() {
        assertEquals("Fred", $.capitalize("fred"));
        assertEquals("Fred", new $("fred").capitalize());
        assertEquals("Fred", $.chain("fred").capitalize().item());
        assertEquals("", $.capitalize(null));
        assertEquals("À", $.capitalize("\u00c0"));
    }

/*
_.uncapitalize('Fred');
=> 'fred'
*/
    @Test
    public void uncapitalize() {
        assertEquals("fred", $.uncapitalize("Fred"));
        assertEquals("fred", new $("Fred").uncapitalize());
        assertEquals("fred", $.chain("Fred").uncapitalize().item());
        assertEquals("", $.uncapitalize(null));
        assertEquals("à", $.uncapitalize("\u00c0"));
    }

/*
_.deburr('déjà vu');
=> 'deja vu'
*/
    @Test
    public void deburr() {
        assertEquals("deja vu", $.deburr("déjà vu"));
        assertEquals("deja vu", new $("déjà vu").deburr());
        assertEquals("deja vu", $.chain("déjà vu").deburr().item());
        assertEquals("", $.deburr(null));
        assertEquals("A", $.deburr("\u00c0"));
    }

/*
_.endsWith('abc', 'c');
=> true

_.endsWith('abc', 'b');
=> false

_.endsWith('abc', 'b', 2);
=> true
*/
    @Test
    public void endsWith() {
        assertTrue($.endsWith("abc", "c"));
        assertTrue(new $("abc").endsWith("c"));
        assertTrue((Boolean) $.chain("abc").endsWith("c").item());
        assertFalse($.endsWith("abc", "b"));
        assertTrue($.endsWith("abc", "b", 2));
        assertTrue(new $("abc").endsWith("b", 2));
        assertTrue((Boolean) $.chain("abc").endsWith("b", 2).item());
        assertFalse($.endsWith("abc", "c", -4));
        assertFalse($.endsWith((String) null, (String) null));
        assertFalse($.endsWith("1", (String) null));
        assertFalse($.endsWith(null, "1"));
        assertTrue($.endsWith("1", "1"));
    }

/*
_.kebabCase('Foo Bar');
=> 'foo-bar'

_.kebabCase('fooBar');
=> 'foo-bar'

_.kebabCase('__foo_bar__');
=> 'foo-bar'
*/
    @Test
    public void kebabCase() {
        assertEquals("foo-bar", $.kebabCase("Foo Bar"));
        assertEquals("foo-bar", new $("Foo Bar").kebabCase());
        assertEquals("foo-bar", $.chain("Foo Bar").kebabCase().item());
        assertEquals("foo-bar", $.kebabCase("fooBar"));
        assertEquals("foo-bar", $.kebabCase("__foo_bar__"));
        assertEquals("", $.kebabCase(null));
        assertEquals("a", $.kebabCase("\u00c0"));
    }

/*
_.snakeCase('Foo Bar');
=> 'foo_bar'

_.snakeCase('fooBar');
=> 'foo_bar'

_.snakeCase('--foo-bar');
=> 'foo_bar'
*/
    @Test
    public void snakeCase() {
        assertEquals("foo_bar", $.snakeCase("Foo Bar"));
        assertEquals("foo_bar", new $("Foo Bar").snakeCase());
        assertEquals("foo_bar", $.chain("Foo Bar").snakeCase().item());
        assertEquals("foo_bar", $.snakeCase("fooBar"));
        assertEquals("foo_bar", $.snakeCase("--foo-bar"));
        assertEquals("", $.snakeCase(null));
        assertEquals("a", $.snakeCase("\u00c0"));
    }

/*
_.startCase('--foo-bar');
=> 'Foo Bar'

_.startCase('fooBar');
=> 'Foo Bar'

_.startCase('__foo_bar__');
=> 'Foo Bar
*/
    @Test
    public void startCase() {
        assertEquals("Foo Bar", $.startCase("--foo-bar"));
        assertEquals("Foo Bar", new $("--foo-bar").startCase());
        assertEquals("Foo Bar", $.chain("--foo-bar").startCase().item());
    }

/*
_.startsWith('abc', 'a');
=> true

_.startsWith('abc', 'b');
=> false

_.startsWith('abc', 'b', 1);
=> true
*/
    @Test
    public void startsWith() {
        assertTrue($.startsWith("abc", "a"));
        assertTrue(new $("abc").startsWith("a"));
        assertTrue((Boolean) $.chain("abc").startsWith("a").item());
        assertFalse($.startsWith("abc", "b"));
        assertTrue($.startsWith("abc", "b", 1));
        assertTrue(new $("abc").startsWith("b", 1));
        assertTrue((Boolean) $.chain("abc").startsWith("b", 1).item());
        assertFalse($.startsWith("abc", "c", -4));
        assertFalse($.startsWith((String) null, (String) null));
        assertFalse($.startsWith("1", (String) null));
        assertFalse($.startsWith(null, "1"));
        assertTrue($.startsWith("1", "1"));
    }

/*
_.trim('  abc  ');
=> 'abc'

_.trim('-_-abc-_-', '_-');
=> 'abc'
*/
    @Test
    public void trim() {
        assertEquals("abc", $.trim("  abc  "));
        assertEquals("abc", new $("  abc  ").trim());
        assertEquals("abc", $.chain("  abc  ").trim().item());
        assertEquals("", $.trim(""));
        assertEquals(" ", $.trim(" ", ""));
        assertEquals("abc", $.trim("-_-abc-_-", "_-"));
        assertEquals("abc", new $("-_-abc-_-").trimWith("_-"));
        assertEquals("abc", $.chain("-_-abc-_-").trim("_-").item());
        assertEquals("    ", $.trim("    ", " "));
    }

/*
_.trimStart('  abc  ');
=> 'abc  '

_.trimStart('-_-abc-_-', '_-');
=> 'abc-_-'
*/

    @Test
    public void trimStart() {
        assertEquals("abc  ", $.trimStart("  abc  "));
        assertEquals("abc  ", new $("  abc  ").trimStart());
        assertEquals("abc  ", $.chain("  abc  ").trimStart().item());
        assertEquals("", $.trimStart(""));
        assertEquals(" ", $.trimStart(" ", ""));
        assertEquals("abc-_-", $.trimStart("-_-abc-_-", "_-"));
        assertEquals("abc-_-", new $("-_-abc-_-").trimStartWith("_-"));
        assertEquals("abc-_-", $.chain("-_-abc-_-").trimStart("_-").item());
        assertEquals("    ", $.trimStart("    ", " "));
    }

/*
_.trimEnd('  abc  ');
=> '  abc'

_.trimEnd('-_-abc-_-', '_-');
=> '-_-abc'
*/

    @Test
    public void trimEnd() {
        assertEquals("  abc", $.trimEnd("  abc  "));
        assertEquals("  abc", new $("  abc  ").trimEnd());
        assertEquals("  abc", $.chain("  abc  ").trimEnd().item());
        assertEquals("", $.trimEnd(""));
        assertEquals(" ", $.trimEnd(" ", ""));
        assertEquals("-_-abc", $.trimEnd("-_-abc-_-", "_-"));
        assertEquals("-_-abc", new $("-_-abc-_-").trimEndWith("_-"));
        assertEquals("-_-abc", $.chain("-_-abc-_-").trimEnd("_-").item());
        assertEquals("    ", $.trimEnd("    ", " "));
    }

/*
_.trunc('hi-diddly-ho there, neighborino');
=> 'hi-diddly-ho there, neighbo...'

_.trunc('hi-diddly-ho there, neighborino', 24);
=> 'hi-diddly-ho there, n...'
*/

    @Test
    public void trunc() {
        assertEquals("hi-diddly-ho there, neighbo...", $.trunc("hi-diddly-ho there, neighborino"));
        assertEquals("hi-diddly-ho there, n...", $.trunc("hi-diddly-ho there, neighborino", 24));
        assertEquals("hi-diddly-ho there, neighborino", $.trunc("hi-diddly-ho there, neighborino", 31));
        assertEquals("hi-", $.trunc("hi-"));
        assertEquals("hi-", new $("hi-").trunc());
        assertEquals("hi-", $.chain("hi-").trunc().item());
        assertEquals("...", $.trunc("hi-did", 3));
        assertEquals("...", new $("hi-did").trunc(3));
        assertEquals("...", $.chain("hi-did").trunc(3).item());
    }

/*
_.words('fred, barney, & pebbles');
=> ['fred', 'barney', 'pebbles']
*/
    @Test
    public void words() {
        assertEquals("[fred, barney, pebbles]", $.words("fred, barney, & pebbles").toString());
        assertEquals("[fred, barney, pebbles]", new $("fred, barney, & pebbles").words().toString());
        assertEquals("[fred, barney, pebbles]", $.chain("fred, barney, & pebbles").words().value().toString());
        assertEquals("[текст, на, русском]", $.words("текст, на, & русском").toString());
        assertEquals("[]", $.words(null).toString());
    }

/*
_.pad('abc', 8);
=> '  abc   '

_.pad('abc', 8, '_-');
=> '_-abc_-_'

_.pad('abc', 3);
=> 'abc'
*/
    @Test
    public void pad() {
        assertEquals("abc", $.pad("abc", 2));
        assertEquals("abc", new $("abc").pad(2));
        assertEquals("abc", $.chain("abc").pad(2).item());
        assertEquals("  abc  ", $.pad("abc", 7));
        assertEquals("  abc   ", $.pad("abc", 8));
        assertEquals("_-abc_-_", $.pad("abc", 8, "_-"));
        assertEquals("_-abc_-_", new $("abc").pad(8, "_-"));
        assertEquals("_-abc_-_", $.chain("abc").pad(8, "_-").item());
    }

/*
_.padStart('abc', 6);
=> '   abc'

_.padStart('abc', 6, '_-');
=> '_-_abc'

_.padStart('abc', 3);
=> 'abc'
*/
    @Test
    public void padStart() {
        assertEquals("   abc", $.padStart("abc", 6));
        assertEquals("   abc", new $("abc").padStart(6));
        assertEquals("   abc", $.chain("abc").padStart(6).item());
        assertEquals("_-_abc", $.padStart("abc", 6, "_-"));
        assertEquals("_-_abc", new $("abc").padStart(6, "_-"));
        assertEquals("_-_abc", $.chain("abc").padStart(6, "_-").item());
        assertEquals("abc", $.padStart("abc", 3));
    }

/*
_.padEnd('abc', 6);
// → 'abc   '

_.padEnd('abc', 6, '_-');
// → 'abc_-_'

_.padEnd('abc', 3);
// → 'abc'
*/
    @Test
    public void padEnd() {
        assertEquals("abc   ", $.padEnd("abc", 6));
        assertEquals("abc   ", new $("abc").padEnd(6));
        assertEquals("abc   ", $.chain("abc").padEnd(6).item());
        assertEquals("abc_-_", $.padEnd("abc", 6, "_-"));
        assertEquals("abc_-_", new $("abc").padEnd(6, "_-"));
        assertEquals("abc_-_", $.chain("abc").padEnd(6, "_-").item());
        assertEquals("abc", $.padEnd("abc", 3));
    }

/*
_.repeat('*', 3);
=> '***'

_.repeat('abc', 2);
=> 'abcabc'

_.repeat('abc', 0);
=> ''
*/
    @Test
    public void repeat() {
        assertEquals("***", $.repeat("*", 3));
        assertEquals("***", new $("*").repeat(3));
        assertEquals("***", $.chain("*").repeat(3).item());
        assertEquals("abcabc", $.repeat("abc", 2));
        assertEquals("", $.repeat("abc", 0));
        assertEquals("", $.repeat(null, 1));
    }

    @Test
    public void testJsonArray() {
        JsonStringBuilder builder = new JsonStringBuilder();
        $.JsonArray.writeJson((Collection) null, builder);
        assertEquals("null", builder.toString());
        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new ArrayList<String>() { { add((String) null); } }, builder);
        assertEquals("[\n  null\n]", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJsonArrayCollection() {
        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]",
            $.toJson(Arrays.asList("First item", "Second item")));
        assertEquals("[\n  [\n    1,\n    2\n  ]\n]",
            $.toJson(Arrays.asList(new byte[] {1, 2})));
        assertEquals("[\n  [\n    1,\n    2\n  ]\n]",
            $.toJson(Arrays.asList(new short[] {1, 2})));
        assertEquals("[\n  [\n    1,\n    2\n  ]\n]",
            $.toJson(Arrays.asList(new int[] {1, 2})));
        assertEquals("[\n  [\n    1,\n    2\n  ]\n]",
            $.toJson(Arrays.asList(new long[] {1, 2})));
        assertEquals("[\n  [\n    1.0,\n    2.0\n  ]\n]",
            $.toJson(Arrays.asList(new float[] {1, 2})));
        assertEquals("[\n  [\n    1.0,\n    2.0\n  ]\n]",
            $.toJson(Arrays.asList(new double[] {1, 2})));
        assertEquals("[\n  [\n    \"1\",\n    \"2\"\n  ]\n]",
            $.toJson(Arrays.asList(new char[] {'1', '2'})));
        assertEquals("[\n  [\n    true,\n    false,\n    true\n  ]\n]",
            $.toJson(Arrays.asList(new boolean[] {true, false, true})));
        assertEquals("[\n  1.0,\n  2.0\n]",
            $.toJson(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals("[\n  1.0,\n  2.0\n]",
            $.toJson(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals("[\n  true,\n  false,\n  true\n]",
            $.toJson(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals("[\n  [\n    \"First item\",\n    \"Second item\"\n  ]\n]",
            $.toJson(Arrays.asList(Arrays.asList("First item", "Second item"))));
        assertEquals("[\n  {\n    \"1\": \"First item\",\n    \"2\": \"Second item\",\n    \"3\": null\n  }\n]",
            $.toJson(Arrays.asList(new LinkedHashMap() { {
                put("1", "First item"); put("2", "Second item"); put("3", null); } })));
        assertEquals("[\n  null\n]", $.toJson(Arrays.asList(new String[] {(String) null})));
        assertEquals("null", $.toJson((Collection) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals("[\n  [\n    test,\n    test\n  ]\n]",
            $.toJson(new ArrayList<Test[]>() { { add(new Test[] {new Test(), new Test()}); } }));
    }

    @Test
    public void escape() {
        assertEquals(null, $.JsonValue.escape(null));
        assertEquals("\\\"", $.JsonValue.escape("\""));
        assertEquals("\\\\", $.JsonValue.escape("\\"));
        assertEquals("\\b", $.JsonValue.escape("\b"));
        assertEquals("\\f", $.JsonValue.escape("\f"));
        assertEquals("\\n", $.JsonValue.escape("\n"));
        assertEquals("\\r", $.JsonValue.escape("\r"));
        assertEquals("\\t", $.JsonValue.escape("\t"));
        assertEquals("\\/", $.JsonValue.escape("/"));
        assertEquals("\\u0000", $.JsonValue.escape("\u0000"));
        assertEquals("\\u001F", $.JsonValue.escape("\u001F"));
        assertEquals("\u0020", $.JsonValue.escape("\u0020"));
        assertEquals("\\u007F", $.JsonValue.escape("\u007F"));
        assertEquals("\\u009F", $.JsonValue.escape("\u009F"));
        assertEquals("\u00A0", $.JsonValue.escape("\u00A0"));
        assertEquals("\\u2000", $.JsonValue.escape("\u2000"));
        assertEquals("\\u20FF", $.JsonValue.escape("\u20FF"));
        assertEquals("\u2100", $.JsonValue.escape("\u2100"));
        assertEquals("\uFFFF", $.JsonValue.escape("\uFFFF"));
    }

    @Test
    public void testByteArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((byte[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new byte[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new byte[] { 12 }, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new byte[] { -7, 22, 86, -99 }, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    public void testShortArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((short[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new short[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new short[] { 12 }, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new short[] { -7, 22, 86, -99 }, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    public void testIntArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((int[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new int[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new int[] { 12 }, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new int[] { -7, 22, 86, -99 }, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    public void testLongArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((long[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new long[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new long[] { 12 }, builder);
        assertEquals("[\n  12\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new long[] { -7, 22, 86, -99 }, builder);
        assertEquals("[\n  -7,\n  22,\n  86,\n  -99\n]", builder.toString());
    }

    @Test
    public void testFloatArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((float[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new float[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new float[] { 12.8f }, builder);
        assertEquals("[\n  12.8\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new float[] { -7.1f, 22.234f, 86.7f, -99.02f }, builder);
        assertEquals("[\n  -7.1,\n  22.234,\n  86.7,\n  -99.02\n]", builder.toString());
    }

    @Test
    public void testDoubleArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((double[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new double[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new double[] { 12.8 }, builder);
        assertEquals("[\n  12.8\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new double[] { -7.1, 22.234, 86.7, -99.02 }, builder);
        assertEquals("[\n  -7.1,\n  22.234,\n  86.7,\n  -99.02\n]", builder.toString());
    }

    @Test
    public void testBooleanArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((boolean[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new boolean[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new boolean[] { true }, builder);
        assertEquals("[\n  true\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new boolean[] { true, false, true }, builder);
        assertEquals("[\n  true,\n  false,\n  true\n]", builder.toString());
    }

    @Test
    public void testCharArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((char[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new char[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new char[] { 'a' }, builder);
        assertEquals("[\n  \"a\"\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new char[] { 'a', 'b', 'c' }, builder);
        assertEquals("[\n  \"a\",\n  \"b\",\n  \"c\"\n]", builder.toString());
    }

    @Test
    public void testObjectArrayToString() {
        JsonStringBuilder builder;

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson((Object[]) null, builder);
        assertEquals("null", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new Object[0], builder);
        assertEquals("[]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new Object[] { "Hello" }, builder);
        assertEquals("[\n  \"Hello\"\n]", builder.toString());

        builder = new JsonStringBuilder();
        $.JsonArray.writeJson(new Object[] { "Hello", new Integer(12), new int[] { 1, 2, 3} }, builder);
        assertEquals("[\n  \"Hello\",\n  12,\n  [\n    1,\n    2,\n    3\n  ]\n]", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toJsonFromList() {
        final List<String> testList = new ArrayList<String>();
        testList.add("First item");
        testList.add("Second item");

        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]", $.toJson(testList));
        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]", new $(testList).toJson());
        assertEquals("[\n  \"First item\",\n  \"Second item\"\n]", $.chain(testList).toJson().item());
        assertEquals("[\n  null\n]", $.toJson(Arrays.asList(Double.NaN)));
        assertEquals("[\n  null\n]", $.toJson(Arrays.asList(Double.POSITIVE_INFINITY)));
        assertEquals("[\n  null\n]", $.toJson(Arrays.asList(Float.NaN)));
        assertEquals("[\n  null\n]", $.toJson(Arrays.asList(Float.POSITIVE_INFINITY)));
    }

    @Test
    public void toJsonFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<String, String>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        assertEquals("{\n  \"First item\": \"1\",\n  \"Second item\": \"2\"\n}", $.toJson(testMap));
        assertEquals("null", $.toJson((Map) null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toJsonFromMapFormatted() {
        String string =
        "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\": \"Standard Generalized Markup Language\",\n"
        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\",\n          \"GlossDef\": {\n"
        + "            \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              \"XML\"\n            ]\n"
        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals(string, $.toJson((Map<String, Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecode() {
            String string = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";
        assertEquals("[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
        + "        }\n      }\n    }\n  }\n]",
            $.toJson((List<Object>) $.fromJson(string)));
        assertEquals("[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
        + "        }\n      }\n    }\n  }\n]",
            $.toJson((List<Object>) new $(string).fromJson()));
        assertEquals("[\n  0,\n  {\n    \"1\": {\n      \"2\": {\n        \"3\": {\n          \"4\": [\n"
        + "            5,\n            {\n              \"6\": 7\n            }\n          ]\n"
        + "        }\n      }\n    }\n  }\n]",
            $.toJson((List<Object>) $.chain(string).fromJson().item()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecode2() {
        String string = "[\"hello\\bworld\\\"abc\\tdef\\\\ghi\\rjkl\\n123\\f356\"]";
        assertEquals("[\n  \"hello\\bworld\\\"abc\\tdef\\\\ghi\\rjkl\\n123\\f356\"\n]",
            $.toJson((List<Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecode3() {
        assertEquals("[\n\n]", $.toJson((List<Object>) $.fromJson("[]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeMap() {
        String string = "{\"first\": 123e+10,\"second\":[{\"k1\":{\"id\":\"id1\"}},"
            + "4,5,6,{\"id\":-123E-1}],\t\n\r \"third\":789,\"id\":null}";
        assertEquals("{\n  \"first\": 1.23E12,\n  \"second\": [\n    {\n      \"k1\": {\n        \"id\": \"id1\"\n"
        + "      }\n    },\n    4,\n    5,\n    6,\n    {\n      \"id\": -12.3\n    }\n  ],\n  \"third\": 789,\n"
        + "  \"id\": null\n}", $.toJson((Map<String, Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeMap2() {
        assertEquals("{\n\n}", $.toJson((Map<String, Object>) $.fromJson("{}")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeMap3() {
        // http://stackoverflow.com/questions/12155800/how-to-convert-hashmap-to-json-object-in-java
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("1", "a");
        map.put("2", "b");
        assertEquals("{\n  \"1\": \"a\",\n  \"2\": \"b\"\n}", $.toJson(map));
        Map<String, Object> newMap = (Map<String, Object>) $.fromJson($.toJson(map));
        assertEquals("a", newMap.get("1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeTrueFalse() {
        List<Object> array1 = new ArrayList<Object>();
        array1.add("abc\u0010a/");
        array1.add(new Integer(123));
        array1.add(new Double(222.123));
        array1.add(Boolean.TRUE);
        array1.add(Boolean.FALSE);
        assertEquals("[\n  \"abc\\u0010a\\/\",\n  123,\n  222.123,\n  true,\n  false\n]", $.toJson(array1));
        String string = "[\n  \"abc\\u0010a\\/\",\n  123,\n  222.123,\n  true,\n  false\n]";
        assertEquals("[\n  \"abc\\u0010a\\/\",\n  123,\n  222.123,\n  true,\n  false\n]",
            $.toJson((List<Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeUnicode() {
        assertEquals("[\n  \"abc\u0A00\"\n]", $.toJson((List<Object>) $.fromJson("[\"abc\\u0a00\"]")));
        assertEquals("[\n  \"abc\u0A00\"\n]", $.toJson((List<Object>) $.fromJson("[\"abc\\u0A00\"]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJsonDecodeCyrillic() {
        assertEquals("[\n  \"Текст на русском\"\n]", $.toJson((List<Object>) $.fromJson("[\"Текст на русском\"]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDecodeSpecialCharacter() {
        assertEquals("{\n  \"description\": \"c:\\\\userDescription.txt\"\n}", $.toJson(
                (Map<String, Object>) $.fromJson("{\"description\":\"c:\\userDescription.txt\"}")));
        assertEquals("{description=c:\\userDescription.txt}", $.fromJson(
                $.toJson(new LinkedHashMap<String, String>() { {
                    put("description",  "c:\\userDescription.txt"); } })).toString());
    }

    @Test
    public void testDecodeUnicode1() {
        assertEquals("[abc\\u0$00]", $.fromJson("[\"abc\\u0$00\"]").toString());
    }

    @Test
    public void testDecodeUnicode2() {
        assertEquals("[abc\\u001g/]", $.fromJson("[\"abc\\u001g\\/\"]").toString());
    }

    @Test
    public void testDecodeUnicode3() {
        assertEquals("[abc\\u001G/]", $.fromJson("[\"abc\\u001G\\/\"]").toString());
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr1() {
        $.fromJson("$");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr2() {
        $.fromJson("[\"value\"");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr3() {
        $.fromJson("{\"value\":123");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr4() {
        $.fromJson("{\"value\"123");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr5() {
        $.fromJson("{value");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr6() {
        $.fromJson("[ture]");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr8() {
        $.fromJson("[\"\\abc\"]");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr9() {
        $.fromJson("[123ea]");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr10() {
        try {
            $.fromJson("[123.a]");
            fail("Expected ParseException");
        } catch ($.ParseException ex) {
            ex.getOffset();
            ex.getLine();
            ex.getColumn();
            throw ex;
        }
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr11() {
        $.fromJson("[1g]");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr12() {
        $.fromJson("[--1");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr13() {
        $.fromJson("[\"abc\u0010\"]");
    }

    @Test(expected = $.ParseException.class)
    public void testDecodeParseErr14() {
        $.fromJson("[\"abc\"][]");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testXmlArray() {
        XmlStringBuilder builder = new XmlStringBuilder();
        $.XmlArray.writeXml((Collection) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\nnull\n</root>", builder.toString());
        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new ArrayList<String>() { { add((String) null); } }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testXmlArrayCollection() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>First item</element>"
            + "\n  <element>Second item</element>\n</root>",
            $.toXml(Arrays.asList("First item", "Second item")));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
            + "\n    <element>2</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new byte[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
            + "\n    <element>2</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new short[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
            + "\n    <element>2</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new int[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
            + "\n    <element>2</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new long[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1.0</element>"
            + "\n    <element>2.0</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new float[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1.0</element>"
            + "\n    <element>2.0</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new double[] {1, 2})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>1</element>"
            + "\n    <element>2</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new char[] {'1', '2'})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>true</element>"
            + "\n    <element>false</element>\n    <element>true</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new boolean[] {true, false, true})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>1.0</element>"
            + "\n  <element>2.0</element>\n</root>",
            $.toXml(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>1.0</element>"
            + "\n  <element>2.0</element>\n</root>",
            $.toXml(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>true</element>"
            + "\n  <element>false</element>\n  <element>true</element>\n</root>",
            $.toXml(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n"
            + "    <element>First item</element>\n    <element>Second item</element>\n  </element>\n</root>",
            $.toXml(Arrays.asList(Arrays.asList("First item", "Second item"))));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n"
            + "    <1>First item</1>\n    <2>Second item</2>\n    <3>null</3>\n  </element>\n</root>",
            $.toXml(Arrays.asList(new LinkedHashMap() { {
                put("1", "First item"); put("2", "Second item"); put("3", null); } })));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            $.toXml(Arrays.asList(new String[] {(String) null})));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\nnull\n</root>",
            $.toXml((Collection) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>\n    <element>test</element>"
            + "\n    <element>test</element>\n  </element>\n</root>",
            $.toXml(new ArrayList<Test[]>() { { add(new Test[] {new Test(), new Test()}); } }));
    }

    @Test
    public void escapeXml() {
        assertEquals(null, $.XmlValue.escape(null));
        assertEquals("&quot;", $.XmlValue.escape("\""));
        assertEquals("&apos;", $.XmlValue.escape("'"));
        assertEquals("&amp;", $.XmlValue.escape("&"));
        assertEquals("&lt;", $.XmlValue.escape("<"));
        assertEquals("&gt;", $.XmlValue.escape(">"));
        assertEquals("\\\\", $.XmlValue.escape("\\"));
        assertEquals("\\b", $.XmlValue.escape("\b"));
        assertEquals("\\f", $.XmlValue.escape("\f"));
        assertEquals("\\n", $.XmlValue.escape("\n"));
        assertEquals("\\r", $.XmlValue.escape("\r"));
        assertEquals("\\t", $.XmlValue.escape("\t"));
        assertEquals("\\/", $.XmlValue.escape("/"));
        assertEquals("&#x0000;", $.XmlValue.escape("\u0000"));
        assertEquals("&#x001F;", $.XmlValue.escape("\u001F"));
        assertEquals("\u0020", $.XmlValue.escape("\u0020"));
        assertEquals("&#x007F;", $.XmlValue.escape("\u007F"));
        assertEquals("&#x009F;", $.XmlValue.escape("\u009F"));
        assertEquals("\u00A0", $.XmlValue.escape("\u00A0"));
        assertEquals("&#x2000;", $.XmlValue.escape("\u2000"));
        assertEquals("&#x20FF;", $.XmlValue.escape("\u20FF"));
        assertEquals("\u2100", $.XmlValue.escape("\u2100"));
        assertEquals("\uFFFF", $.XmlValue.escape("\uFFFF"));
    }

    @Test
    public void testXmlByteArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((byte[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new byte[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new byte[] { 12 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new byte[] { -7, 22, 86, -99 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
            + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlShortArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((short[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new short[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new short[] { 12 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new short[] { -7, 22, 86, -99 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
            + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlIntArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((int[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new int[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new int[] { 12 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new int[] { -7, 22, 86, -99 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
            + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlLongArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((long[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new long[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new long[] { 12 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new long[] { -7, 22, 86, -99 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7</element>\n  <element>"
            + "22</element>\n  <element>86</element>\n  <element>-99</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlFloatArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((float[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new float[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new float[] { 12.8f }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12.8</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new float[] { -7.1f, 22.234f, 86.7f, -99.02f }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7.1</element>\n  <element>"
            + "22.234</element>\n  <element>86.7</element>\n  <element>-99.02</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlDoubleArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((double[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new double[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new double[] { 12.8 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>12.8</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new double[] { -7.1, 22.234, 86.7, -99.02 }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>-7.1</element>\n  <element>"
            + "22.234</element>\n  <element>86.7</element>\n  <element>-99.02</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlBooleanArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((boolean[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new boolean[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new boolean[] { true }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>true</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new boolean[] { true, false, true }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>true</element>\n  <element>"
            + "false</element>\n  <element>true</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlCharArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((char[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new char[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new char[] { 'a' }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>a</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new char[] { 'a', 'b', 'c' }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>a</element>\n  <element>"
            + "b</element>\n  <element>c</element>\n</root>", builder.toString());
    }

    @Test
    public void testXmlObjectArrayToString() {
        XmlStringBuilder builder;

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml((Object[]) null, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new Object[0], builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element></element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new Object[] { "Hello" }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>Hello</element>\n</root>",
            builder.toString());

        builder = new XmlStringBuilder();
        $.XmlArray.writeXml(new Object[] { "Hello", new Integer(12), new int[] { 1, 2, 3} }, builder);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>Hello</element>\n  <element>"
            + "12</element>\n  <element>\n    <element>1</element>\n    <element>2</element>\n    <element>3</element>"
            + "\n  </element>\n</root>", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testXmlDecodeCyrillic() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>Текст на русском</element>\n"
            + "</root>", $.toXml((List<Object>) $.fromJson("[\"Текст на русском\"]")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toXmlFromList() {
        final List<String> testList = new ArrayList<String>();
        testList.add("First item");
        testList.add("Second item");

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
            + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
            $.toXml(testList));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
            + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
            new $(testList).toXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
            + "  <element>First item</element>\n  <element>Second item</element>\n</root>",
            $.chain(testList).toXml().item());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            $.toXml(Arrays.asList(Double.NaN)));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            $.toXml(Arrays.asList(Double.POSITIVE_INFINITY)));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            $.toXml(Arrays.asList(Float.NaN)));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element>null</element>\n</root>",
            $.toXml(Arrays.asList(Float.POSITIVE_INFINITY)));
    }

    @Test
    public void toXmlFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<String, String>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
            + "  <First item>1</First item>\n  <Second item>2</Second item>\n</root>",
            $.toXml(testMap));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\nnull\n</root>", $.toXml((Map) null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toXml() {
        String string =
        "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\": \"Standard Generalized Markup Language\",\n"
        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\",\n          \"GlossDef\": {\n"
        + "            \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              \"XML\"\n            ]\n"
        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "\n<glossary>"
        + "\n  <title>example glossary</title>"
        + "\n  <GlossDiv>"
        + "\n    <title>S</title>"
        + "\n    <GlossList>"
        + "\n      <GlossEntry>"
        + "\n        <ID>SGML</ID>"
        + "\n        <SortAs>SGML</SortAs>"
        + "\n        <GlossTerm>Standard Generalized Markup Language</GlossTerm>"
        + "\n        <Acronym>SGML</Acronym>"
        + "\n        <Abbrev>ISO 8879:1986</Abbrev>"
        + "\n        <GlossDef>"
        + "\n          <para>A meta-markup language, used to create markup languages such as DocBook.</para>"
        + "\n          <GlossSeeAlso>"
        + "\n            <element>GML</element>"
        + "\n            <element>XML</element>"
        + "\n          </GlossSeeAlso>"
        + "\n        </GlossDef>"
        + "\n        <GlossSee>markup</GlossSee>"
        + "\n      </GlossEntry>"
        + "\n    </GlossList>"
        + "\n  </GlossDiv>"
        + "\n</glossary>", $.toXml((Map<String, Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fromXml() {
        String string =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
        + "\n"
        + "\n<root>"
        + "\n<Details>"
        + "\n    <detail-a>"
        + "\n"
        + "\n        <detail> attribute 1 of detail a </detail>"
        + "\n        <detail> attribute 2 of detail a </detail>"
        + "\n        <detail> attribute 3 of detail a </detail>"
        + "\n"
        + "\n    </detail-a>"
        + "\n"
        + "\n    <detail-b>"
        + "\n        <detail> attribute 1 of detail b </detail>"
        + "\n        <detail> attribute 2 of detail b </detail>"
        + "\n"
        + "\n    </detail-b>"
        + "\n"
        + "\n"
        + "\n</Details>"
        + "\n</root>";
        assertEquals(
        "{"
        + "\n  \"root\": {"
        + "\n    \"Details\": {"
        + "\n      \"detail-a\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail a \","
        + "\n          \" attribute 2 of detail a \","
        + "\n          \" attribute 3 of detail a \""
        + "\n        ]"
        + "\n      },"
        + "\n      \"detail-b\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail b \","
        + "\n          \" attribute 2 of detail b \""
        + "\n        ]"
        + "\n      }"
        + "\n    }"
        + "\n  }"
        + "\n}",
            $.toJson((Map<String, Object>) $.fromXml(string)));
        assertEquals(
        "{"
        + "\n  \"root\": {"
        + "\n    \"Details\": {"
        + "\n      \"detail-a\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail a \","
        + "\n          \" attribute 2 of detail a \","
        + "\n          \" attribute 3 of detail a \""
        + "\n        ]"
        + "\n      },"
        + "\n      \"detail-b\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail b \","
        + "\n          \" attribute 2 of detail b \""
        + "\n        ]"
        + "\n      }"
        + "\n    }"
        + "\n  }"
        + "\n}",
            $.toJson((Map<String, Object>) new $(string).fromXml()));
        assertEquals(
        "{"
        + "\n  \"root\": {"
        + "\n    \"Details\": {"
        + "\n      \"detail-a\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail a \","
        + "\n          \" attribute 2 of detail a \","
        + "\n          \" attribute 3 of detail a \""
        + "\n        ]"
        + "\n      },"
        + "\n      \"detail-b\": {"
        + "\n        \"detail\": ["
        + "\n          \" attribute 1 of detail b \","
        + "\n          \" attribute 2 of detail b \""
        + "\n        ]"
        + "\n      }"
        + "\n    }"
        + "\n  }"
        + "\n}",
            $.toJson((Map<String, Object>) $.chain(string).fromXml().item()));
        String stringXml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "\n<root>"
        + "\n  <glossary>"
        + "\n    <title>example glossary</title>"
        + "\n    <GlossDiv>"
        + "\n      <title>S</title>"
        + "\n      <GlossList>"
        + "\n        <GlossEntry>"
        + "\n          <ID>SGML</ID>"
        + "\n          <SortAs>SGML</SortAs>"
        + "\n          <GlossTerm>Standard Generalized Markup Language</GlossTerm>"
        + "\n          <Acronym>SGML</Acronym>"
        + "\n          <Abbrev>ISO 8879:1986</Abbrev>"
        + "\n          <GlossDef>"
        + "\n            <para>A meta-markup language, used to create markup languages such as DocBook.</para>"
        + "\n            <GlossSeeAlso>"
        + "\n              <element>GML</element>"
        + "\n              <element>XML</element>"
        + "\n            </GlossSeeAlso>"
        + "\n          </GlossDef>"
        + "\n          <GlossSee>markup</GlossSee>"
        + "\n        </GlossEntry>"
        + "\n      </GlossList>"
        + "\n    </GlossDiv>"
        + "\n  </glossary>"
        + "\n</root>";
        $.fromXml(stringXml);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fetchGetXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report><time/><coords><x>3916.234</x><y>169.914</y><z>6.33</z>"
            + "</coords><code>NMR</code><message>Another day of everyday normal regular weather, business as usual, unless "
            + "it’s going to be like the time of the Great Paprika Mayonnaise Incident of 2014, that was some pretty nasty "
            + "stuff.</message><varX-Rating>8</varX-Rating></report>";
        assertEquals("NMR", (String) ((Map<String, Object>) ((Map<String, Object>) $.fromXml(xml)).get("report")).get("code"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeParseXmlErr13() {
        $.fromXml("[\"abc\u0010\"]");
    }

    @Test
    public void testJsonJavaArray() {
        JsonJavaStringBuilder builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((Collection) null, builder);
        assertEquals("\"null\";", builder.toString());
        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new ArrayList<String>() { { add((String) null); } }, builder);
        assertEquals("\"[\\n\"\n + \"  null\\n\"\n + \"]\";", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testJsonJavaArrayCollection() {
        assertEquals("\"[\\n\"\n"
                        + " + \"  \\\"First item\\\",\\n\"\n"
                        + " + \"  \\\"Second item\\\"\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList("First item", "Second item")));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1,\\n\"\n"
                        + " + \"    2\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new byte[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1,\\n\"\n"
                        + " + \"    2\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new short[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1,\\n\"\n"
                        + " + \"    2\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new int[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1,\\n\"\n"
                        + " + \"    2\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new long[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1.0,\\n\"\n"
                        + " + \"    2.0\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new float[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    1.0,\\n\"\n"
                        + " + \"    2.0\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new double[] {1, 2})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    \"1\",\\n\"\n"
                        + " + \"    \"2\"\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new char[] {'1', '2'})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    true,\\n\"\n"
                        + " + \"    false,\\n\"\n"
                        + " + \"    true\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new boolean[] {true, false, true})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  1.0,\\n\"\n"
                        + " + \"  2.0\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  1.0,\\n\"\n"
                        + " + \"  2.0\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  true,\\n\"\n"
                        + " + \"  false,\\n\"\n"
                        + " + \"  true\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    \\\"First item\\\",\\n\"\n"
                        + " + \"    \\\"Second item\\\"\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(Arrays.asList("First item", "Second item"))));
        assertEquals("\"[\\n\"\n"
                        + " + \"  {\\n\"\n"
                        + " + \"    \\\"1\\\": \\\"First item\\\",\\n\"\n"
                        + " + \"    \\\"2\\\": \\\"Second item\\\",\\n\"\n"
                        + " + \"    \\\"3\\\": null\\n\"\n"
                        + " + \"  }\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(Arrays.asList(new LinkedHashMap() { {
                put("1", "First item"); put("2", "Second item"); put("3", null); } })));
        assertEquals("\"[\\n\"\n"
                + " + \"  null\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(Arrays.asList(new String[] {(String) null})));
        assertEquals("\"null\";", $.toJsonJavaString((Collection) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals("\"[\\n\"\n"
                        + " + \"  [\\n\"\n"
                        + " + \"    test,\\n\"\n"
                        + " + \"    test\\n\"\n"
                        + " + \"  ]\\n\"\n"
                        + " + \"]\";",
            $.toJsonJavaString(new ArrayList<Test[]>() { { add(new Test[] {new Test(), new Test()}); } }));
    }

    @Test
    public void escapeJava() {
        assertEquals(null, $.JsonJavaValue.escape(null));
        assertEquals("\\\"", $.JsonJavaValue.escape("\""));
        assertEquals("\\\\", $.JsonJavaValue.escape("\\"));
        assertEquals("\\b", $.JsonJavaValue.escape("\b"));
        assertEquals("\\f", $.JsonJavaValue.escape("\f"));
        assertEquals("\\n", $.JsonJavaValue.escape("\n"));
        assertEquals("\\r", $.JsonJavaValue.escape("\r"));
        assertEquals("\\t", $.JsonJavaValue.escape("\t"));
        assertEquals("\\/", $.JsonJavaValue.escape("/"));
        assertEquals("\\u0000", $.JsonJavaValue.escape("\u0000"));
        assertEquals("\\u001F", $.JsonJavaValue.escape("\u001F"));
        assertEquals("\u0020", $.JsonJavaValue.escape("\u0020"));
        assertEquals("\\u007F", $.JsonJavaValue.escape("\u007F"));
        assertEquals("\\u009F", $.JsonJavaValue.escape("\u009F"));
        assertEquals("\u00A0", $.JsonJavaValue.escape("\u00A0"));
        assertEquals("\\u2000", $.JsonJavaValue.escape("\u2000"));
        assertEquals("\\u20FF", $.JsonJavaValue.escape("\u20FF"));
        assertEquals("\u2100", $.JsonJavaValue.escape("\u2100"));
        assertEquals("\uFFFF", $.JsonJavaValue.escape("\uFFFF"));
    }

    @Test
    public void testJavaByteArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((byte[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new byte[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new byte[] { 12 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new byte[] { -7, 22, 86, -99 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7,\\n\"\n"
                + " + \"  22,\\n\"\n"
                + " + \"  86,\\n\"\n"
                + " + \"  -99\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaShortArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((short[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new short[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new short[] { 12 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new short[] { -7, 22, 86, -99 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7,\\n\"\n"
                + " + \"  22,\\n\"\n"
                + " + \"  86,\\n\"\n"
                + " + \"  -99\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaIntArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((int[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new int[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new int[] { 12 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new int[] { -7, 22, 86, -99 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7,\\n\"\n"
                + " + \"  22,\\n\"\n"
                + " + \"  86,\\n\"\n"
                + " + \"  -99\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaLongArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((long[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new long[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new long[] { 12 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new long[] { -7, 22, 86, -99 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7,\\n\"\n"
                + " + \"  22,\\n\"\n"
                + " + \"  86,\\n\"\n"
                + " + \"  -99\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaFloatArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((float[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new float[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new float[] { 12.8f }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12.8\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new float[] { -7.1f, 22.234f, 86.7f, -99.02f }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7.1,\\n\"\n"
                + " + \"  22.234,\\n\"\n"
                + " + \"  86.7,\\n\"\n"
                + " + \"  -99.02\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaDoubleArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((double[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new double[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new double[] { 12.8 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  12.8\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new double[] { -7.1, 22.234, 86.7, -99.02 }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  -7.1,\\n\"\n"
                + " + \"  22.234,\\n\"\n"
                + " + \"  86.7,\\n\"\n"
                + " + \"  -99.02\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaBooleanArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((boolean[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new boolean[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new boolean[] { true }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  true\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new boolean[] { true, false, true }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  true,\\n\"\n"
                + " + \"  false,\\n\"\n"
                + " + \"  true\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaCharArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((char[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new char[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new char[] { 'a' }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  \"a\"\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new char[] { 'a', 'b', 'c' }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  \"a\",\\n\"\n"
                + " + \"  \"b\",\\n\"\n"
                + " + \"  \"c\"\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @Test
    public void testJavaObjectArrayToString() {
        JsonJavaStringBuilder builder;

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson((Object[]) null, builder);
        assertEquals("\"null\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new Object[0], builder);
        assertEquals("\"[]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new Object[] { "Hello" }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  \\\"Hello\\\"\\n\"\n"
                + " + \"]\";", builder.toString());

        builder = new JsonJavaStringBuilder();
        $.JsonJavaArray.writeJson(new Object[] { "Hello", new Integer(12), new int[] { 1, 2, 3} }, builder);
        assertEquals("\"[\\n\"\n"
                + " + \"  \\\"Hello\\\",\\n\"\n"
                + " + \"  12,\\n\"\n"
                + " + \"  [\\n\"\n"
                + " + \"    1,\\n\"\n"
                + " + \"    2,\\n\"\n"
                + " + \"    3\\n\"\n"
                + " + \"  ]\\n\"\n"
                + " + \"]\";", builder.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toJsonJavaFromList() {
        final List<String> testList = new ArrayList<String>();
        testList.add("First item");
        testList.add("Second item");

        assertEquals("\"[\\n\"\n"
                + " + \"  \\\"First item\\\",\\n\"\n"
                + " + \"  \\\"Second item\\\"\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(testList));
        assertEquals("\"[\\n\"\n"
                + " + \"  \\\"First item\\\",\\n\"\n"
                + " + \"  \\\"Second item\\\"\\n\"\n"
                + " + \"]\";", new $(testList).toJsonJavaString());
        assertEquals("\"[\\n\"\n"
                + " + \"  \\\"First item\\\",\\n\"\n"
                + " + \"  \\\"Second item\\\"\\n\"\n"
                + " + \"]\";", $.chain(testList).toJsonJavaString().item());
        assertEquals("\"[\\n\"\n"
                + " + \"  null\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(Arrays.asList(Double.NaN)));
        assertEquals("\"[\\n\"\n"
                + " + \"  null\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(Arrays.asList(Double.POSITIVE_INFINITY)));
        assertEquals("\"[\\n\"\n"
                + " + \"  null\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(Arrays.asList(Float.NaN)));
        assertEquals("\"[\\n\"\n"
                + " + \"  null\\n\"\n"
                + " + \"]\";", $.toJsonJavaString(Arrays.asList(Float.POSITIVE_INFINITY)));
    }

    @Test
    public void toJsonJavaFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<String, String>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        assertEquals("\"{\\n\"\n"
                + " + \"  \\\"First item\\\": \\\"1\\\",\\n\"\n"
                + " + \"  \\\"Second item\\\": \\\"2\\\"\\n\"\n"
                + " + \"}\";", $.toJsonJavaString(testMap));
        assertEquals("\"null\";", $.toJsonJavaString((Map) null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toJsonJavaString() {
        String javaString =
            "\"{\\n\"\n"
            + " + \"  \\\"glossary\\\": {\\n\"\n"
            + " + \"    \\\"title\\\": \\\"example glossary\\\",\\n\"\n"
            + " + \"    \\\"GlossDiv\\\": {\\n\"\n"
            + " + \"      \\\"title\\\": \\\"S\\\",\\n\"\n"
            + " + \"      \\\"GlossList\\\": {\\n\"\n"
            + " + \"        \\\"GlossEntry\\\": {\\n\"\n"
            + " + \"          \\\"ID\\\": \\\"SGML\\\",\\n\"\n"
            + " + \"          \\\"SortAs\\\": \\\"SGML\\\",\\n\"\n"
            + " + \"          \\\"GlossTerm\\\": \\\"Standard Generalized Markup Language\\\",\\n\"\n"
            + " + \"          \\\"Acronym\\\": \\\"SGML\\\",\\n\"\n"
            + " + \"          \\\"Abbrev\\\": \\\"ISO 8879:1986\\\",\\n\"\n"
            + " + \"          \\\"GlossDef\\\": {\\n\"\n"
            + " + \"            \\\"para\\\": \\\"A meta-markup language, used to create markup"
            + " languages such as DocBook.\\\",\\n\"\n"
            + " + \"            \\\"GlossSeeAlso\\\": [\\n\"\n"
            + " + \"              \\\"GML\\\",\\n\"\n"
            + " + \"              \\\"XML\\\"\\n\"\n"
            + " + \"            ]\\n\"\n"
            + " + \"          },\\n\"\n"
            + " + \"          \\\"GlossSee\\\": \\\"markup\\\"\\n\"\n"
            + " + \"        }\\n\"\n"
            + " + \"      }\\n\"\n"
            + " + \"    }\\n\"\n"
            + " + \"  }\\n\"\n"
            + " + \"}\";";
        String string =
        "{\n  \"glossary\": {\n    \"title\": \"example glossary\",\n    \"GlossDiv\": {\n      \"title\":"
        + " \"S\",\n      \"GlossList\": {\n        \"GlossEntry\": {\n          \"ID\": \"SGML\",\n"
        + "          \"SortAs\": \"SGML\",\n          \"GlossTerm\": \"Standard Generalized Markup Language\",\n"
        + "          \"Acronym\": \"SGML\",\n          \"Abbrev\": \"ISO 8879:1986\",\n          \"GlossDef\": {\n"
        + "            \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
        + "            \"GlossSeeAlso\": [\n              \"GML\",\n              \"XML\"\n            ]\n"
        + "          },\n          \"GlossSee\": \"markup\"\n        }\n      }\n    }\n  }\n}";
        assertEquals(javaString, $.toJsonJavaString((Map<String, Object>) $.fromJson(string)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void main() throws Exception {
        $.main(new String[] {});
        new $(new ArrayList<String>());
        new $("");
        new $(Arrays.asList()).chain();
        new $.JsonArray();
        new $.JsonValue();
        new $.JsonObject();
        new $.XmlArray();
        new $.XmlValue();
        new $.XmlObject();
        new $.JsonJavaArray();
        new $.JsonJavaValue();
        new $.JsonJavaObject();
        $.chain(new ArrayList<String>());
        $.chain(new ArrayList<String>(), 1);
        $.chain(new HashSet<String>());
        $.chain(new String[] {});
    }

    @SuppressWarnings("unchecked")
    @Test
    public void chain() {
        $.chain(new String[] {""}).first();
        $.chain(new String[] {""}).first(1);
        $.chain(new String[] {""}).firstOrNull();
        $.chain(new String[] {""}).firstOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).initial();
        $.chain(new String[] {""}).initial(1);
        $.chain(new String[] {""}).last();
        $.chain(new String[] {""}).last(1);
        $.chain(new String[] {""}).lastOrNull();
        $.chain(new String[] {""}).lastOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).rest();
        $.chain(new String[] {""}).rest(1);
        $.chain(new String[] {""}).compact();
        $.chain(new String[] {""}).compact("1");
        $.chain(new String[] {""}).flatten();
        $.chain(new Integer[] {0}).map(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new String[] {""}).filter(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).filterIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        $.chain(new String[] {""}).reject(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).rejectIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        $.chain(new String[] {""}).filterFalse(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).reduce(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        $.chain(new String[] {""}).reduceRight(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        $.chain(new String[] {""}).find(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).findLast(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new Integer[] {0}).max();
        $.chain(new Integer[] {0}).max(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new Integer[] {0}).min();
        $.chain(new Integer[] {0}).min(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new Integer[] {0}).sort();
        $.chain(new Integer[] {0}).sortWith(new Comparator<Integer>() {
            public int compare(Integer value1, Integer value2) { return value1; } });
        $.chain(new Integer[] {0}).sortBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new LinkedHashMap<Integer, Integer>().entrySet()).sortBy("");
        $.chain(new Integer[] {0}).groupBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new Integer[] {0}).indexBy("");
        $.chain(new Integer[] {0}).countBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new Integer[] {0}).shuffle();
        $.chain(new Integer[] {0}).sample();
        $.chain(new Integer[] {0}).sample(1);
        $.chain(new int[] {0}).value();
        $.chain(new String[] {""}).tap(new Consumer<String>() {
            public void accept(String str) {
            } });
        $.chain(new String[] {""}).forEach(new Consumer<String>() {
            public void accept(String str) {
            } });
        $.chain(new String[] {""}).forEachRight(new Consumer<String>() {
            public void accept(String str) {
            } });
        $.chain(new String[] {""}).every(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).some(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        $.chain(new String[] {""}).contains("");
        $.chain(new String[] {""}).invoke("toString", Collections.emptyList());
        $.chain(new String[] {""}).invoke("toString");
        $.chain(new String[] {""}).pluck("toString");
        $.chain(new String[] {""}).where(Collections.<Tuple<String, String>>emptyList());
        $.chain(new String[] {""}).findWhere(Collections.<Tuple<String, String>>emptyList());
        $.chain(new Integer[] {0}).uniq();
        $.chain(new Integer[] {0}).uniq(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new Integer[] {0}).distinct();
        $.chain(new Integer[] {0}).distinctBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        $.chain(new String[] {""}).union();
        $.chain(new String[] {""}).intersection();
        $.chain(new String[] {""}).difference();
        $.chain(new String[] {""}).range(0);
        $.chain(new String[] {""}).range(0, 0);
        $.chain(new String[] {""}).range(0, 0, 1);
        $.chain(new String[] {""}).chunk(1);
        $.chain(new String[] {""}).concat();
        $.chain(new String[] {""}).slice(0);
        $.chain(new String[] {""}).slice(0, 0);
        $.chain(new String[] {""}).reverse();
        $.chain(new String[] {""}).join();
        $.chain(new String[] {""}).join("");
        $.chain(new String[] {""}).skip(0);
        $.chain(new String[] {""}).limit(0);
        $.chain(new LinkedHashMap<Integer, Integer>().entrySet()).toMap();
    }
}
