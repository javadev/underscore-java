/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2024 Valentyn Kolesnikov
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
class UtilityTest {

    /*
    var stooge = {name: 'moe'};
    stooge === _.identity(stooge);
    => true
    */
    @Test
    void identity() {
        Map<String, String> object =
                new LinkedHashMap<String, String>() {
                    {
                        put("name", "moe");
                    }
                };
        assertEquals(object, Underscore.identity(object));
        String xml = " <product><a001>045293373</a001><a002>03</a002><productidentifier><b221>02</b221><b244>0545791324</b244></productidentifier><productidentifier><b221>03</b221><b244>9780545791328</b244></productidentifier><productidentifier><b221>13</b221><b244>2016014983</b244></productidentifier><productidentifier><b221>14</b221><b244>09780545791328</b244></productidentifier><productidentifier><b221>15</b221><b244>9780545791328</b244></productidentifier><descriptivedetail><x314>00</x314><b012>BB</b012><b333>B304</b333><b333>B401</b333><b333>B501</b333><measure><x315>01</x315><c094>10.60</c094><c095>in</c095></measure><measure><x315>02</x315><c094>8.80</c094><c095>in</c095></measure><measure><x315>03</x315><c094>1.40</c094><c095>in</c095></measure><measure><x315>08</x315><c094>3.5000</c094><c095>lb</c095></measure><x316>US</x316><productclassification><b274>01</b274><b275>4901.99.0070</b275></productclassification><collection><x329>20</x329><x330>INGRAM</x330><titledetail><b202>01</b202><titleelement><x409>02</x409><b203>Harry Potter</b203></titleelement><titleelement><x409>01</x409><x410>2</x410></titleelement></titledetail></collection><titledetail><b202>01</b202><titleelement><x409>01</x409><x501/><b031>Harry Potter and the Chamber of Secrets: The Illustrated Edition (Harry Potter, Book 2)</b031><b029>Volume 2</b029></titleelement></titledetail><contributor><b034>1</b034><b035>A01</b035><b039>J K</b039><b040>Rowling</b040><contributordate><x417>50</x417><j260>05</j260><b306>1965</b306></contributordate><b044><![CDATA[<p>J.K. ROWLING is the author of the enduringly popular, era-defining Harry Potter seven-book series, which have sold over 600 million copies in 85 languages, been listened to as audiobooks for over one billion hours and made into eight smash hit movies. To accompany the series, she wrote three short companion volumes for charity, including <i>Fantastic Beasts and Where to Find Them</i>, which went on to inspire a new series of films featuring Magizoologist Newt Scamander. Harry's story as a grown-up was continued in a stage play, <i>Harry Potter and the Cursed Child</i>, which J.K. Rowling wrote with playwright Jack Thorne and director John Tiffany.</p></p><p></p><p>In 2020, she returned to publishing for younger children with the fairy tale <i>The Ickabog</i>, the royalties for which she donated to her charitable trust, Volant, to help charities working to alleviate the social effects of the Covid 19 pandemic. Her latest children's novel, <i>The Christmas Pig</i>, was published in 2021.</p></p><p></p><p>J.K. Rowling has received many awards and honours for her writing, including for her detective series written under the name Robert Galbraith. She supports a wide number of humanitarian causes through Volant, and is the founder of the international children's care reform charity Lumos. J.K. Rowling lives in Scotland with her family.</p></p>]]></b044><contributorplace><x418>04</x418><b251>GB</b251></contributorplace></contributor><contributor><b034>2</b034><b035>A12</b035><b039>Jim</b039><b040>Kay</b040><b044><![CDATA[Jim Kay won the Kate Greenaway Medal in 2012 for his illustrations in <i>A Monster Calls</i> by Patrick Ness. Jim studied illustration at the University of Westminster and since graduating has worked in the archives of Tate Britain and the Royal Botanic Gardens at Kew. Jim has produced concept work for television and contributed to a group exhibition at the Victoria and Albert Museum in London. He now lives and works in Northamptonshire, England, with his wife.]]></b044></contributor><n386/></descriptivedetail><collateraldetail><textcontent><x426>02</x426><x427>00</x427><d104>Originally published in a slightly different form in the United Kingdom by Bloomsbury in 1998.</d104></textcontent><textcontent><x426>03</x426><x427>00</x427><d104 textformat = \"02\"><![CDATA[Award-winning artist Jim Kay illustrates year two of Harry Potter's adventures at Hogwarts, in a stunning, gift-ready format.<p></p>The Dursleys were so mean and hideous that summer that all Harry Potter wanted was to get back to the Hogwarts School for Witchcraft and Wizardry. But just as he's packing his bags, Harry receives a warning from a strange, impish creature named Dobby who says that if Harry Potter returns to Hogwarts, disaster will strike.And strike it does. For in Harry's second year at Hogwarts, fresh torments and horrors arise, including an outrageously stuck-up new professor, Gilderoy Lockhart, a spirit named Moaning Myrtle who haunts the girls' bathroom, and the unwanted attentions of Ron Weasley's younger sister, Ginny.But each of these seem minor annoyances when the real trouble begins, and someone -- or something -- starts turning Hogwarts students to stone. Could it be Draco Malfoy, a more poisonous rival than ever? Could it possibly be Hagrid, whose mysterious past is finally told? Or could it be the one everyone at Hogwarts most suspects... Harry Potter himself?]]></d104></textcontent></collateraldetail></product>";
        String json = U.xmlToJson(xml, U.XmlToJsonMode.REPLACE_MINUS_WITH_AT);
        System.out.println(json);
    }

    /*
    var stooge = {name: 'moe'};
    stooge === _.constant(stooge)();
    => true
    */
    @Test
    void constant() {
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
    void property() {
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
    void propertyOf() {
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
    void random() {
        int result = Underscore.random(0, 100);
        assertTrue(result >= 0);
        assertTrue(result <= 100);
    }

    /*
    _.random(0, 1);
    => 0 or 1
    */
    @Test
    void random2() {
        int result = Underscore.random(0, 1);
        assertTrue(result >= 0);
        assertTrue(result <= 1);
    }

    /*
    _.random(0);
    => 0
    */
    @Test
    void random3() {
        int result = Underscore.random(0);
        assertEquals(0, result);
    }

    /*
    _.now();
    => 1392066795351
    */
    @Test
    void now() {
        assertTrue(Underscore.now() <= new Date().getTime());
    }

    /*
    _.uniqueId('contact_');
    => 'contact_104'
    */
    @Test
    void uniqueId() {
        assertTrue(Underscore.uniqueId("contact_").matches("contact_\\d+"));
        assertTrue(Underscore.uniqueId(null).matches("\\d+"));
    }

    @Test
    void uniquePassword() {
        String password = Underscore.uniquePassword();
        assertTrue(
                password.matches("[A-Z]+[0-9]+.*?[a-z]+.*"),
                "Password doesn't matches the pattern [A-Z]+[0-9]+.*?[a-z]+ " + password);
    }

    /*
    _.times(3, function(n){ genie.grantWishNumber(n); });
    */
    @Test
    void times() {
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
    void mixin() {
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
    void escape() {
        assertEquals(
                "Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;",
                Underscore.escape("Curly, Larry & Moe < > \"'`"));
    }

    /*
    _.unescape('Curly, Larry &amp; Moe &lt; &gt; &quot;&#x27;&#x60;');
    => "Curly, Larry & Moe < > \"'`"
    */
    @Test
    void unescape() {
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
    void template() {
        Underscore.Template<Map<String, Object>> compiled =
                Underscore.template("hello: <%= name %>");
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
    void template2() {
        Underscore.Template<Map<String, Object>> compiled =
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
    void template3() {
        Underscore.Template<Map<String, Object>> compiled =
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
    void template4() {
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
        Underscore.Template<Map<String, Object>> compiled =
                Underscore.template("hello: {{= name }}");
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
    void templateValue() {
        Underscore.Template<Map<String, Object>> template =
                Underscore.template("<b><%- value %></b>");
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
    void templateValue2() {
        Underscore.Template<Map<String, Object>> template =
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
    void templateValue3() {
        Underscore.Template<Map<String, Object>> template =
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
    void templateValue4() {
        Underscore.Template<Map<String, Object>> template =
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
    void templateCheck() {
        Underscore.Template<Map<String, Object>> compiled =
                Underscore.template("hello: <%= name %>");
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
    void templateCheck2() {
        Underscore.Template<Map<String, Object>> compiled =
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
        Underscore.Template<Map<String, Object>> compiled2 =
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
        Underscore.Template<Map<String, Object>> compiled3 =
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
    void format() {
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

    /*
    var object = {cheese: 'crumpets', stuff: function(){ return 'nonsense'; }};
    _.result(object, 'cheese');
    => "crumpets"
    _.result(object, 'stuff');
    => "nonsense"
    */
    @Test
    void result() {
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

    @Test
    void joinToString() {
        assertEquals("[]", U.joinToString(List.of(), ",",
                "[", "]", 3, "...", null));
        assertEquals("[1,2,3]", U.joinToString(List.of(1, 2, 3), ",",
                "[", "]", -1, "...", null));
    }
}
