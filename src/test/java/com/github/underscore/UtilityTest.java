/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2021 Valentyn Kolesnikov
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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.Test;

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
        Map<String, String> object =
                new LinkedHashMap<String, String>() {
                    {
                        put("name", "moe");
                    }
                };
        assertEquals(object, Underscore.identity(object));
    }

    /*
    var stooge = {name: 'moe'};
    stooge === _.constant(stooge)();
    => true
    */
    @Test
    public void constant() {
        Map<String, String> object =
                new LinkedHashMap<String, String>() {
                    {
                        put("name", "moe");
                    }
                };
        assertEquals(object, Underscore.constant(object).get());
    }

    /*
    var stooge = {name: 'moe'};
    'moe' === _.property('name')(stooge);
    => true
    */
    @Test
    public void property() {
        Map<String, Object> stooge =
                new LinkedHashMap<String, Object>() {
                    {
                        put("name", "moe");
                    }
                };
        assertEquals("moe", Underscore.property("name").apply(stooge));
    }

    /*
    var stooge = {name: 'moe'};
    _.propertyOf(stooge)('name');
    => 'moe'
    */
    @Test
    public void propertyOf() {
        Map<String, String> stooge =
                new LinkedHashMap<String, String>() {
                    {
                        put("name", "moe");
                    }
                };
        assertEquals("moe", Underscore.propertyOf(stooge).apply("name"));
    }

    /*
    _.random(0, 100);
    => 42
    */
    @Test
    public void random() {
        int result = Underscore.random(0, 100);
        assertTrue(result >= 0);
        assertTrue(result <= 100);
    }

    /*
    _.random(0, 1);
    => 0 or 1
    */
    @Test
    public void random2() {
        int result = Underscore.random(0, 1);
        assertTrue(result >= 0);
        assertTrue(result <= 1);
    }

    /*
    _.random(0);
    => 0
    */
    @Test
    public void random3() {
        int result = Underscore.random(0);
        assertEquals(0, result);
    }

    /*
    _.now();
    => 1392066795351
    */
    @Test
    public void now() {
        assertTrue(Underscore.now() <= new Date().getTime());
    }

    /*
    _.uniqueId('contact_');
    => 'contact_104'
    */
    @Test
    public void uniqueId() {
        assertEquals("contact_1", Underscore.uniqueId("contact_"));
        assertEquals("2", Underscore.uniqueId(null));
    }

    @Test
    public void uniquePassword() {
        String password = Underscore.uniquePassword();
        assertTrue(
                "Password doesn't matches the pattern [A-Z]+[0-9]+.*?[a-z]+ " + password,
                password.matches("[A-Z]+[0-9]+.*?[a-z]+.*"));
    }

    /*
    _.times(3, function(n){ genie.grantWishNumber(n); });
    */
    @Test
    public void times() {
        final List<Integer> result = new ArrayList<>();
        Underscore.times(3, () -> result.add(1));
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
        Underscore.mixin(
                "capitalize",
                string ->
                        String.valueOf(string.charAt(0)).toUpperCase()
                                + string.substring(1).toLowerCase());
        assertEquals("Fabio", new Underscore("fabio").call("capitalize").get());
        assertFalse(new Underscore("fabio").call("capitalize2").isPresent());
        assertFalse(new Underscore(Collections.singletonList(1)).call("capitalize2").isPresent());
    }

    /*
    _.escape('Curly, Larry & Moe < > "\'`');
    => "Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;"
    */
    @Test
    public void escape() {
        assertEquals(
                "Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;",
                Underscore.escape("Curly, Larry & Moe < > \"'`"));
    }

    /*
    _.unescape('Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;');
    => "Curly, Larry & Moe < > \"'`"
    */
    @Test
    public void unescape() {
        assertEquals(
                "Curly, Larry & Moe < > \"'`",
                Underscore.unescape("Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;"));
    }

    /*
    var compiled = _.template("hello: <%= name %>");
    compiled({name: 'moe'});
    => "hello: moe"
    */
    @Test
    public void template() {
        Template<Map<String, Object>> compiled = Underscore.template("hello: <%= name %>");
        assertEquals(
                "hello: moe",
                compiled.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                            }
                        }));
    }

    @Test
    public void template2() {
        Template<Map<String, Object>> compiled =
                Underscore.template("hello: <%= name %>, hello2: <%= name %>");
        assertEquals(
                "hello: moe, hello2: moe",
                compiled.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                            }
                        }));
    }

    @Test
    public void template3() {
        Template<Map<String, Object>> compiled =
                Underscore.template("hello: <%= name %>, hello2: <%= name2 %>");
        assertEquals(
                "hello: moe, hello2: moe2",
                compiled.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                                put("name2", "moe2");
                            }
                        }));
    }

    @Test
    public void template4() {
        Underscore.templateSettings(
                new HashMap<String, String>() {
                    {
                        put("interpolate", "");
                    }
                });
        Underscore.templateSettings(
                new HashMap<String, String>() {
                    {
                        put("interpolate", "\\{\\{=([\\s\\S]+?)\\}\\}");
                    }
                });
        Template<Map<String, Object>> compiled = Underscore.template("hello: {{= name }}");
        assertEquals(
                "hello: moe",
                compiled.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                            }
                        }));
        Underscore.templateSettings(
                new HashMap<String, String>() {
                    {
                        put("interpolate", "<%=([\\s\\S]+?)%>");
                    }
                });
    }

    /*
    var template = _.template("<b><%- value %></b>");
    template({value: '<script>'});
    => "<b>&lt;script&gt;</b>"
    */
    @Test
    public void templateValue() {
        Template<Map<String, Object>> template = Underscore.template("<b><%- value %></b>");
        assertEquals(
                "<b>&lt;script&gt;</b>",
                template.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("value", "<script>");
                            }
                        }));
    }

    @Test
    public void templateValue2() {
        Template<Map<String, Object>> template =
                Underscore.template("hello: <%= name %>, <b><%- value %></b>");
        assertEquals(
                "hello: moe, <b>&lt;script&gt;</b>",
                template.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                                put("value", "<script>");
                            }
                        }));
    }

    @Test
    public void templateValue3() {
        Template<Map<String, Object>> template =
                Underscore.template("hello: <% name %>, <b><%- value %></b>");
        assertEquals(
                "hello: moe, <b>&lt;script&gt;</b>",
                template.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                                put("value", "<script>");
                            }
                        }));
    }

    @Test
    public void templateValue4() {
        Template<Map<String, Object>> template =
                Underscore.template("hello: <% name %>, <b><%- value %></b>");
        assertEquals(
                "hello: $moe$, <b>&lt;script&gt;</b>",
                template.apply(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "$moe$");
                                put("value", "<script>");
                            }
                        }));
    }

    @Test
    public void templateCheck() {
        Template<Map<String, Object>> compiled = Underscore.template("hello: <%= name %>");
        assertTrue(
                compiled.check(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name", "moe");
                                    }
                                })
                        .isEmpty());
        assertEquals(
                "name2",
                compiled.check(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name2", "moe");
                                    }
                                })
                        .get(0));
    }

    @Test
    public void templateCheck2() {
        Template<Map<String, Object>> compiled =
                Underscore.template("hello: <%= name %> <%= name2 %>");
        assertEquals(
                "name2",
                compiled.check(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name", "moe");
                                    }
                                })
                        .get(0));
        Template<Map<String, Object>> compiled2 =
                Underscore.template("hello: <%- name %> <%- name2 %>");
        assertEquals(
                "name2",
                compiled2
                        .check(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name", "moe");
                                    }
                                })
                        .get(0));
        Template<Map<String, Object>> compiled3 =
                Underscore.template("hello: <% name %> <% name2 %>");
        assertEquals(
                "name2",
                compiled3
                        .check(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name", "moe");
                                    }
                                })
                        .get(0));
    }

    /*
    var fortmatted = _.format("hello: {}", "moe");
    => "hello: moe"
    */
    @Test
    public void format() {
        String fortmatted = Underscore.format("hello: {0}", "moe");
        assertEquals("hello: moe", fortmatted);
        String fortmatted2 = Underscore.format("hello: {}", "moe");
        assertEquals("hello: moe", fortmatted2);
        String fortmatted3 = Underscore.format("hello: {}, {}", "moe", 123);
        assertEquals("hello: moe, 123", fortmatted3);
        String fortmatted4 = Underscore.format("hello: {1}, {0}", "moe", 123);
        assertEquals("hello: 123, moe", fortmatted4);
        String fortmatted5 = Underscore.format("hello: {0}", "mo/e");
        assertEquals("hello: mo/e", fortmatted5);
        String fortmatted6 = Underscore.format("hello: {0}", "mo\\e");
        assertEquals("hello: mo\\e", fortmatted6);
    }

    @Test
    public void minimumDays() {
        List<List<Integer>> ll = new ArrayList<>();
        ll.add(Arrays.asList(1, 1, 1, 1, 1));
        ll.add(Arrays.asList(1, 1, 1, 0, 1));
        ll.add(Arrays.asList(1, 0, 1, 1, 1));
        ll.add(Arrays.asList(1, 1, 1, 1, 1));
        assertEquals(1, Underscore.minimumDays(4, 5, ll));
        List<List<Integer>> ll2 = new ArrayList<>();
        ll2.add(Arrays.asList(0, 0, 0, 0, 0));
        ll2.add(Arrays.asList(0, 0, 0, 0, 0));
        ll2.add(Arrays.asList(0, 0, 0, 0, 0));
        ll2.add(Arrays.asList(0, 0, 0, 0, 0));
        assertEquals(-1, Underscore.minimumDays(4, 5, ll2));
    }

    @Test
    public void topNCompetitors() {
        List<String> competitors =
                Arrays.asList("anacell", "betacellular", "cetracular", "deltacellular", "eurocell");
        List<String> reviews =
                Arrays.asList(
                        "I love anacell Best services provided by anacell in the town",
                        "betacellular has great services",
                        "deltacellular provides much betacellular",
                        "cetracular is worse than eurocell",
                        "betacellular is better than deltacellular");
        List<String> strings = Underscore.topNCompetitors(6, 2, competitors, 6, reviews);
        assertEquals("[betacellular, deltacellular]", strings.toString());
        List<String> competitors2 = Arrays.asList("ААА", "БББ");
        List<String> reviews2 =
                Arrays.asList(
                        "I love anacell Best services provided by anacell in the town",
                        "betacellular has great services",
                        "deltacellular provides much betacellular",
                        "cetracular is worse than eurocell",
                        "betacellular is better than deltacellular");
        List<String> strings2 = Underscore.topNCompetitors(2, 2, competitors2, 6, reviews2);
        assertEquals("[]", strings2.toString());
        List<String> strings3 = Underscore.topNCompetitors(2, 2, competitors2, 6, null);
        assertEquals("[]", strings3.toString());
        List<String> strings4 =
                Underscore.topNCompetitors(2, 2, competitors2, 6, Collections.<String>emptyList());
        assertEquals("[]", strings4.toString());
        List<String> strings5 = Underscore.topNCompetitors(2, 2, null, 6, reviews2);
        assertEquals("[]", strings5.toString());
        List<String> strings6 =
                Underscore.topNCompetitors(2, 2, Collections.<String>emptyList(), 6, reviews2);
        assertEquals("[]", strings6.toString());
        List<String> strings7 = Underscore.topNCompetitors(0, 2, competitors2, 6, reviews2);
        assertEquals("[]", strings7.toString());
        List<String> strings8 = Underscore.topNCompetitors(2, 2, competitors2, 0, reviews2);
        assertEquals("[]", strings8.toString());
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
        Map<String, Object> object =
                new LinkedHashMap<String, Object>() {
                    {
                        put("cheese", "crumpets");
                        put("stuff", (Supplier<String>) () -> "nonsense");
                    }
                };

        assertEquals(
                "crumpets",
                Underscore.result(object.entrySet(), item -> item.getKey().equals("cheese")));
        assertEquals(
                "nonsense",
                Underscore.result(object.entrySet(), item -> item.getKey().equals("stuff")));
        assertEquals(
                "result1",
                Underscore.result(asList("result1", "result2"), item -> item.equals("result1")));
        assertEquals(
                null,
                Underscore.result(asList("result1", "result2"), item -> item.equals("result3")));
    }
}
