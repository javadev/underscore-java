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
