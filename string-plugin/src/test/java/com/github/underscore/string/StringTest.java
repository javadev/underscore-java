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
    public void main() throws Exception {
        $.main(new String[] {});
        new $(new ArrayList<String>());
        new $("");
        $.chain(new ArrayList<String>());
        $.chain(new HashSet<String>());
        $.chain(new String[] {});
    }
}
