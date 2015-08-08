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
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
_.trimLeft('  abc  ');
=> 'abc  '

_.trimLeft('-_-abc-_-', '_-');
=> 'abc-_-'
*/

    @Test
    public void trimLeft() {
        assertEquals("abc  ", $.trimLeft("  abc  "));
        assertEquals("abc  ", new $("  abc  ").trimLeft());
        assertEquals("abc  ", $.chain("  abc  ").trimLeft().item());
        assertEquals("", $.trimLeft(""));
        assertEquals(" ", $.trimLeft(" ", ""));
        assertEquals("abc-_-", $.trimLeft("-_-abc-_-", "_-"));
        assertEquals("abc-_-", new $("-_-abc-_-").trimLeftWith("_-"));
        assertEquals("abc-_-", $.chain("-_-abc-_-").trimLeft("_-").item());
        assertEquals("    ", $.trimLeft("    ", " "));
    }

/*
_.trimRight('  abc  ');
=> '  abc'

_.trimRight('-_-abc-_-', '_-');
=> '-_-abc'
*/

    @Test
    public void trimRight() {
        assertEquals("  abc", $.trimRight("  abc  "));
        assertEquals("  abc", new $("  abc  ").trimRight());
        assertEquals("  abc", $.chain("  abc  ").trimRight().item());
        assertEquals("", $.trimRight(""));
        assertEquals(" ", $.trimRight(" ", ""));
        assertEquals("-_-abc", $.trimRight("-_-abc-_-", "_-"));
        assertEquals("-_-abc", new $("-_-abc-_-").trimRightWith("_-"));
        assertEquals("-_-abc", $.chain("-_-abc-_-").trimRight("_-").item());
        assertEquals("    ", $.trimRight("    ", " "));
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
_.padLeft('abc', 6);
=> '   abc'

_.padLeft('abc', 6, '_-');
=> '_-_abc'

_.padLeft('abc', 3);
=> 'abc'
*/
    @Test
    public void padLeft() {
        assertEquals("   abc", $.padLeft("abc", 6));
        assertEquals("   abc", new $("abc").padLeft(6));
        assertEquals("   abc", $.chain("abc").padLeft(6).item());
        assertEquals("_-_abc", $.padLeft("abc", 6, "_-"));
        assertEquals("_-_abc", new $("abc").padLeft(6, "_-"));
        assertEquals("_-_abc", $.chain("abc").padLeft(6, "_-").item());
        assertEquals("abc", $.padLeft("abc", 3));
    }

/*
_.padRight('abc', 6);
// → 'abc   '

_.padRight('abc', 6, '_-');
// → 'abc_-_'

_.padRight('abc', 3);
// → 'abc'
*/
    @Test
    public void padRight() {
        assertEquals("abc   ", $.padRight("abc", 6));
        assertEquals("abc   ", new $("abc").padRight(6));
        assertEquals("abc   ", $.chain("abc").padRight(6).item());
        assertEquals("abc_-_", $.padRight("abc", 6, "_-"));
        assertEquals("abc_-_", new $("abc").padRight(6, "_-"));
        assertEquals("abc_-_", $.chain("abc").padRight(6, "_-").item());
        assertEquals("abc", $.padRight("abc", 3));
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
    public void testJSONArray() throws java.io.IOException {
        java.io.StringWriter writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((Collection) null, writer);
        assertEquals("null", writer.toString());
        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new ArrayList<String>() { { add((String) null); } }, writer);
        assertEquals("[null]", writer.toString());
    }

    @Test
    public void testJSONArrayCollection() {
        assertEquals("[\"First item\",\"Second item\"]",
            $.toJSONString(Arrays.asList("First item", "Second item")));
        assertEquals("[[1,2]]",
            $.toJSONString(Arrays.asList(new byte[] {1, 2})));
        assertEquals("[[1,2]]",
            $.toJSONString(Arrays.asList(new short[] {1, 2})));
        assertEquals("[[1,2]]",
            $.toJSONString(Arrays.asList(new int[] {1, 2})));
        assertEquals("[[1,2]]",
            $.toJSONString(Arrays.asList(new long[] {1, 2})));
        assertEquals("[[1.0,2.0]]",
            $.toJSONString(Arrays.asList(new float[] {1, 2})));
        assertEquals("[[1.0,2.0]]",
            $.toJSONString(Arrays.asList(new double[] {1, 2})));
        assertEquals("[[\"1\",\"2\"]]",
            $.toJSONString(Arrays.asList(new char[] {'1', '2'})));
        assertEquals("[[true,false,true]]",
            $.toJSONString(Arrays.asList(new boolean[] {true, false, true})));
        assertEquals("[1.0,2.0]",
            $.toJSONString(Arrays.asList(new Float[] {1F, 2F})));
        assertEquals("[1.0,2.0]",
            $.toJSONString(Arrays.asList(new Double[] {1D, 2D})));
        assertEquals("[true,false,true]",
            $.toJSONString(Arrays.asList(new Boolean[] {true, false, true})));
        assertEquals("[[\"First item\",\"Second item\"]]",
            $.toJSONString(Arrays.asList(Arrays.asList("First item", "Second item"))));
        assertEquals("[{\"1\":\"First item\",\"2\":\"Second item\",\"3\":null}]",
            $.toJSONString(Arrays.asList(new LinkedHashMap() { {
                put("1", "First item"); put("2", "Second item"); put("3", null); } })));
        assertEquals("[null]", $.toJSONString(Arrays.asList(new String[] {(String) null})));
        assertEquals("null", $.toJSONString((Collection) null));
        class Test {
            public String toString() {
                return "test";
            }
        }
        assertEquals("[[test,test]]",
            $.toJSONString(new ArrayList<Test[]>() { { add(new Test[] {new Test(), new Test()}); } }));
    }

    @Test
    public void escape() {
        assertEquals(null, $.JSONValue.escape(null));
        assertEquals("\\\"", $.JSONValue.escape("\""));
        assertEquals("\\\\", $.JSONValue.escape("\\"));
        assertEquals("\\b", $.JSONValue.escape("\b"));
        assertEquals("\\f", $.JSONValue.escape("\f"));
        assertEquals("\\n", $.JSONValue.escape("\n"));
        assertEquals("\\r", $.JSONValue.escape("\r"));
        assertEquals("\\t", $.JSONValue.escape("\t"));
        assertEquals("\\/", $.JSONValue.escape("/"));
        assertEquals("\\u0000", $.JSONValue.escape("\u0000"));
        assertEquals("\\u001F", $.JSONValue.escape("\u001F"));
        assertEquals("\u0020", $.JSONValue.escape("\u0020"));
        assertEquals("\\u007F", $.JSONValue.escape("\u007F"));
        assertEquals("\\u009F", $.JSONValue.escape("\u009F"));
        assertEquals("\u00A0", $.JSONValue.escape("\u00A0"));
        assertEquals("\\u2000", $.JSONValue.escape("\u2000"));
        assertEquals("\\u20FF", $.JSONValue.escape("\u20FF"));
        assertEquals("\u2100", $.JSONValue.escape("\u2100"));
    }

    @Test
    public void testByteArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((byte[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new byte[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new byte[] { 12 }, writer);
        assertEquals("[12]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new byte[] { -7, 22, 86, -99 }, writer);
        assertEquals("[-7,22,86,-99]", writer.toString());
    }

    @Test
    public void testShortArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((short[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new short[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new short[] { 12 }, writer);
        assertEquals("[12]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new short[] { -7, 22, 86, -99 }, writer);
        assertEquals("[-7,22,86,-99]", writer.toString());
    }

    @Test
    public void testIntArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((int[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new int[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new int[] { 12 }, writer);
        assertEquals("[12]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new int[] { -7, 22, 86, -99 }, writer);
        assertEquals("[-7,22,86,-99]", writer.toString());
    }

    @Test
    public void testLongArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((long[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new long[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new long[] { 12 }, writer);
        assertEquals("[12]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new long[] { -7, 22, 86, -99 }, writer);
        assertEquals("[-7,22,86,-99]", writer.toString());
    }

    @Test
    public void testFloatArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((float[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new float[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new float[] { 12.8f }, writer);
        assertEquals("[12.8]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new float[] { -7.1f, 22.234f, 86.7f, -99.02f }, writer);
        assertEquals("[-7.1,22.234,86.7,-99.02]", writer.toString());
    }

    @Test
    public void testDoubleArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((double[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new double[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new double[] { 12.8 }, writer);
        assertEquals("[12.8]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new double[] { -7.1, 22.234, 86.7, -99.02 }, writer);
        assertEquals("[-7.1,22.234,86.7,-99.02]", writer.toString());
    }

    @Test
    public void testBooleanArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((boolean[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new boolean[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new boolean[] { true }, writer);
        assertEquals("[true]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new boolean[] { true, false, true }, writer);
        assertEquals("[true,false,true]", writer.toString());
    }

    @Test
    public void testCharArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((char[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new char[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new char[] { 'a' }, writer);
        assertEquals("[\"a\"]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new char[] { 'a', 'b', 'c' }, writer);
        assertEquals("[\"a\",\"b\",\"c\"]", writer.toString());
    }

    @Test
    public void testObjectArrayToString() throws java.io.IOException {
        java.io.StringWriter writer;

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString((Object[]) null, writer);
        assertEquals("null", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new Object[0], writer);
        assertEquals("[]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new Object[] { "Hello" }, writer);
        assertEquals("[\"Hello\"]", writer.toString());

        writer = new java.io.StringWriter();
        $.JSONArray.writeJSONString(new Object[] { "Hello", new Integer(12), new int[] { 1, 2, 3} }, writer);
        assertEquals("[\"Hello\",12,[1,2,3]]", writer.toString());
    }

    @Test
    public void toJSONStringFromList() {
        final List<String> testList = new ArrayList<String>();
        testList.add("First item");
        testList.add("Second item");

        assertEquals("[\"First item\",\"Second item\"]", $.toJSONString(testList));
        assertEquals("[null]", $.toJSONString(Arrays.asList(Double.NaN)));
        assertEquals("[null]", $.toJSONString(Arrays.asList(Double.POSITIVE_INFINITY)));
        assertEquals("[null]", $.toJSONString(Arrays.asList(Float.NaN)));
        assertEquals("[null]", $.toJSONString(Arrays.asList(Float.POSITIVE_INFINITY)));
    }

    @Test
    public void toJSONStringFromMap() {
        final Map<String, String> testMap = new LinkedHashMap<String, String>();
        testMap.put("First item", "1");
        testMap.put("Second item", "2");

        assertEquals("{\"First item\":\"1\",\"Second item\":\"2\"}", $.toJSONString(testMap));
        assertEquals("null", $.toJSONString((Map) null));
    }

    @Test
    public void main() throws Exception {
        $.main(new String[] {});
        new $(new ArrayList<String>());
        new $("");
        new $(Arrays.asList()).chain();
        new $.JSONArray();
        new $.JSONValue();
        new $.JSONObject();
        $.chain(new ArrayList<String>());
        $.chain(new HashSet<String>());
        $.chain(new String[] {});
    }
}
