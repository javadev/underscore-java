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

import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class UtilityTest {

/*
var stooge = {name: 'moe'};
stooge === _.identity(stooge);
=> true
*/
    @Test
    public void identity() {
        Map<String, String> object = new LinkedHashMap<String, String>() { { put("name", "moe"); } };
        assertEquals(object, $.identity(object));
    }

/*
var stooge = {name: 'moe'};
stooge === _.constant(stooge)();
=> true
*/
    @Test
    public void constant() {
        Map<String, String> object = new LinkedHashMap<String, String>() { { put("name", "moe"); } };
        assertEquals(object, $.constant(object).apply());
    }

/*
var stooge = {name: 'moe'};
'moe' === _.property('name')(stooge);
=> true
*/
    @Test
    public void property() {
        Map<String, Object> stooge = new LinkedHashMap<String, Object>() { { put("name", "moe"); } };
        assertEquals("moe", $.property("name").apply(stooge));
    }

/*
var stooge = {name: 'moe'};
_.propertyOf(stooge)('name');
=> 'moe'
*/
    @Test
    public void propertyOf() {
        Map<String, String> stooge = new LinkedHashMap<String, String>() { { put("name", "moe"); } };
        assertEquals("moe", $.propertyOf(stooge).apply("name"));
    }

/*
_.random(0, 100);
=> 42
*/
    @Test
    public void random() {
        int result = $.random(0, 100);
        assertTrue(result >= 0);
        assertTrue(result <= 100);
    }

/*
_.now();
=> 1392066795351
*/
    @Test
    public void now() {
        assertTrue($.now() <= new Date().getTime());
    }

/*
_.uniqueId('contact_');
=> 'contact_104'
*/
    @Test
    public void uniqueId() {
        assertEquals("contact_1", $.uniqueId("contact_"));
    }

    @Test
    public void uniquePassword() {
        String password = $.uniquePassword();
        assertTrue("Password doesn't matches the pattern [A-Z]+[0-9]+.*?[a-z]+ " + password,
            password.matches("[A-Z]+[0-9]+.*?[a-z]+.*"));
    }

/*
_.times(3, function(n){ genie.grantWishNumber(n); });
*/
    @Test
    public void times() {
        final List<Integer> result = new ArrayList<Integer>();
        $.times(3, new Function<Integer>() {
            public Integer apply() {
                result.add(1);
                return null;
            }
        });
        assertEquals("[1, 1, 1]", result.toString());
    }

/*
_.mixin({
  capitalize: function(string) {
    return string.charAt(0).toUpperCase() + string.substring(1).toLowerCase();
  }
});
_("fabio").capitalize();
=> "Fabio"
*/
    @Test
    @SuppressWarnings("unchecked")
    public void mixin() {
        $.mixin("capitalize", new Function1<String, String>() {
            public String apply(final String string) {
                return String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1).toLowerCase();
            }
        });
        assertEquals("Fabio", new $("fabio").call("capitalize").get());
        assertFalse(new $("fabio").call("capitalize2").isPresent());
        assertFalse(new $(asList(1)).call("capitalize2").isPresent());
    }

/*
_.escape('Curly, Larry & Moe < > "\'`');
=> "Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;"
*/
    @Test
    public void escape() {
        assertEquals("Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;", $.escape("Curly, Larry & Moe < > \"'`"));
    }

/*
_.unescape('Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;');
=> "Curly, Larry & Moe < > \"'`"
*/
    @Test
    public void unescape() {
        assertEquals("Curly, Larry & Moe < > \"'`", $.unescape("Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;"));
    }

/*
var compiled = _.template("hello: <%= name %>");
compiled({name: 'moe'});
=> "hello: moe"
*/
    @Test
    public void template() {
        Template<Set<Map.Entry<String, Object>>> compiled = $.template("hello: <%= name %>");
        assertEquals("hello: moe", compiled.apply((new LinkedHashMap<String, Object>() { {
            put("name", "moe"); } }).entrySet()));
    }

    @Test
    public void template2() {
        Template<Set<Map.Entry<String, Object>>> compiled = $.template("hello: <%= name %>, hello2: <%= name %>");
        assertEquals("hello: moe, hello2: moe", compiled.apply((new LinkedHashMap<String, Object>() { {
            put("name", "moe"); } }).entrySet()));
    }

    @Test
    public void template3() {
        Template<Set<Map.Entry<String, Object>>> compiled = $.template("hello: <%= name %>, hello2: <%= name2 %>");
        assertEquals("hello: moe, hello2: moe2", compiled.apply(
            (new LinkedHashMap<String, Object>() { { put("name", "moe"); put("name2", "moe2"); } }).entrySet()));
    }

    @Test
    public void template4() {
        $.templateSettings(new HashMap<String, String>() { { put("interpolate", ""); } });
        $.templateSettings(new HashMap<String, String>() { { put("interpolate", "\\{\\{=([\\s\\S]+?)\\}\\}"); } });
        Template<Set<Map.Entry<String, Object>>> compiled = $.template("hello: {{= name }}");
        assertEquals("hello: moe", compiled.apply((new LinkedHashMap<String, Object>() { {
            put("name", "moe"); } }).entrySet()));
        $.templateSettings(new HashMap<String, String>() { { put("interpolate", "<%=([\\s\\S]+?)%>"); } });
    }

/*
var list = "<% _.each(people, function(name) { %> <li><%= name %></li> <% }); %>";
_.template(list, {people: ['moe', 'curly', 'larry']});
=> "<li>moe</li><li>curly</li><li>larry</li>"
*/
    @Test
    public void templateEach() {
        String list = "<% _.each(people, function(name) { %> <li><%= name %></li> <% }); %>";
        Template<Set<Map.Entry<String, Object>>> compiled = $.template(list);
        assertEquals(" <li>moe</li>  <li>curly</li>  <li>larry</li> ",
            compiled.apply((new LinkedHashMap<String, Object>() { {
                put("people", asList("moe", "curly", "larry")); } }).entrySet()));
        String list2 = "<% _.each(people2, function(name) { %> <li><%= name %></li> <% }); %>";
        Template<Set<Map.Entry<String, Object>>> compiled2 = $.template(list2);
        assertEquals("<% _.each(people2, function(name) { %> <li><%= name %></li> <% }); %>",
            compiled2.apply((new LinkedHashMap<String, Object>() { {
                put("people", asList("moe", "curly", "larry")); } }).entrySet()));
        $.templateSettings(new HashMap<String, String>() { { put("interpolate", "\\{\\{=([\\s\\S]+?)\\}\\}");
            put("evaluate", "\\{\\{([\\s\\S]+?)\\}\\}"); } });
        String list3 = "{{ _.each(people, function(name) { }} <li>{{= name }}</li> {{ }); }}";
        Template<Set<Map.Entry<String, Object>>> compiled3 = $.template(list3);
        assertEquals(" <li>moe</li>  <li>curly</li>  <li>larry</li> ",
            compiled3.apply((new LinkedHashMap<String, Object>() { {
                put("people", asList("moe", "curly", "larry")); } }).entrySet()));
        $.templateSettings(new HashMap<String, String>() { { put("interpolate", "<%=([\\s\\S]+?)%>");
            put("evaluate", "<%([\\s\\S]+?)%>"); } });
    }

/*
var template = _.template("<b><%- value %></b>");
template({value: '<script>'});
=> "<b>&lt;script&gt;</b>"
*/
    @Test
    public void templateValue() {
        Template<Set<Map.Entry<String, Object>>> template = $.template("<b><%- value %></b>");
        assertEquals("<b>&lt;script&gt;</b>",
            template.apply((new LinkedHashMap<String, Object>() { { put("value", "<script>"); } }).entrySet()));
    }

    @Test
    public void templateValue2() {
        Template<Set<Map.Entry<String, Object>>> template = $.template("hello: <%= name %>, <b><%- value %></b>");
        assertEquals("hello: moe, <b>&lt;script&gt;</b>",
            template.apply((new LinkedHashMap<String, Object>() { {
                put("name", "moe"); put("value", "<script>"); } }).entrySet()));
    }
/*
var compiled = _.template("<% print('Hello ' + epithet); %>");
compiled({epithet: "stooge"});
=> "Hello stooge"
*/
    @Test
    public void templatePrint() {
        Template<Set<Map.Entry<String, Object>>> compiled = $.template("<% print('Hello ' + epithet); %>");
        assertEquals("Hello stooge",
            compiled.apply((new LinkedHashMap<String, Object>() { { put("epithet", "stooge"); } }).entrySet()));
        Template<Set<Map.Entry<String, Object>>> compiled2 = $.template("<% print('Hello ' + epithet2); %>");
        assertEquals("<% print('Hello ' + epithet2); %>",
            compiled2.apply((new LinkedHashMap<String, Object>() { { put("epithet", "stooge"); } }).entrySet()));
    }

/*
var object = {cheese: 'crumpets', stuff: function(){ return 'nonsense'; }};
_.result(object, 'cheese');
=> "crumpets"
_.result(object, 'stuff');
=> "nonsense"
*/
    @Test
    public void result() {
        Map<String, Object> object = new LinkedHashMap<String, Object>() { {
            put("cheese", "crumpets");
            put("stuff", new Function<String>() { public String apply() {
                return "nonsense"; } });
        } };

        assertEquals("crumpets", $.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("cheese");
            }
        }));
        assertEquals("nonsense", $.result(object.entrySet(), new Predicate<Map.Entry<String, Object>>() {
            public Boolean apply(Map.Entry<String, Object> item) {
                return item.getKey().equals("stuff");
            }
        }));
        assertEquals("result1", $.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result1");
            }
        }));
        assertEquals(null, $.result(asList("result1", "result2"), new Predicate<String>() {
            public Boolean apply(String item) {
                return item.equals("result3");
            }
        }));
    }

}
