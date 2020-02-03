/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2020 Valentyn Kolesnikov
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
package com.github.underscore.lodash;

import com.github.underscore.BinaryOperator;
import com.github.underscore.BiFunction;
import com.github.underscore.Consumer;
import com.github.underscore.Function;
import com.github.underscore.Predicate;
import com.github.underscore.PredicateIndexed;
import com.github.underscore.Tuple;
import java.util.*;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class LodashTest {

/*
_.chunk(['a', 'b', 'c', 'd'], 2);
// → [['a', 'b'], ['c', 'd']]

_.chunk(['a', 'b', 'c', 'd'], 3);
// → [['a', 'b', 'c'], ['d']]
*/
    @Test
    public void chunk() {
        assertEquals("[[a, b], [c, d]]", U.chunk(asList("a", "b", "c", "d"), 2).toString());
        assertEquals("[[a, b], [c, d]]", new U<String>(asList("a", "b", "c", "d")).chunk(2).toString());
        assertEquals("[[a, b], [c, d]]", U.chain(asList("a", "b", "c", "d")).chunk(2).value().toString());
        assertEquals("[[a, b, c], [d]]", U.chunk(asList("a", "b", "c", "d"), 3).toString());
    }

/*
_.drop([1, 2, 3]);
// → [2, 3]

_.drop([1, 2, 3], 2);
// → [3]

_.drop([1, 2, 3], 5);
// → []

_.drop([1, 2, 3], 0);
// → [1, 2, 3]
*/
    @Test
    public void drop() {
        assertEquals("[2, 3]", U.drop(asList(1, 2, 3)).toString());
        assertEquals("[2, 3]", new U<Integer>(asList(1, 2, 3)).drop().toString());
        assertEquals("[2, 3]", U.chain(asList(1, 2, 3)).drop().value().toString());
        assertEquals("[3]", U.drop(asList(1, 2, 3), 2).toString());
        assertEquals("[3]", new U<Integer>(asList(1, 2, 3)).drop(2).toString());
        assertEquals("[3]", U.chain(asList(1, 2, 3)).drop(2).value().toString());
        assertEquals("[]", U.drop(asList(1, 2, 3), 5).toString());
        assertEquals("[1, 2, 3]", U.drop(asList(1, 2, 3), 0).toString());
    }

/*
_.dropRight([1, 2, 3]);
// → [1, 2]

_.dropRight([1, 2, 3], 2);
// → [1]

_.dropRight([1, 2, 3], 5);
// → []

_.dropRight([1, 2, 3], 0);
// → [1, 2, 3]
*/
    @Test
    public void dropRight() {
        assertEquals("[1, 2]", U.dropRight(asList(1, 2, 3)).toString());
        assertEquals("[1, 2]", new U<Integer>(asList(1, 2, 3)).dropRight().toString());
        assertEquals("[1, 2]", U.chain(asList(1, 2, 3)).dropRight().value().toString());
        assertEquals("[1]", U.dropRight(asList(1, 2, 3), 2).toString());
        assertEquals("[1]", new U<Integer>(asList(1, 2, 3)).dropRight(2).toString());
        assertEquals("[1]", U.chain(asList(1, 2, 3)).dropRight(2).value().toString());
        assertEquals("[]", U.dropRight(asList(1, 2, 3), 5).toString());
        assertEquals("[1, 2, 3]", U.dropRight(asList(1, 2, 3), 0).toString());
    }

/*
_.dropWhile([1, 2, 3], function(n) {
  return n < 3;
});
// → [3]
*/
    @Test
    public void dropWhile() {
        assertEquals("[3]", U.dropWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[3]", new U<Integer>(asList(1, 2, 3)).dropWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[3]", U.chain(asList(1, 2, 3)).dropWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).value().toString());
    }

/*
_.dropRightWhile([1, 2, 3], function(n) {
  return n > 2;
});
// → [1, 2]
*/
    @SuppressWarnings("unchecked")
    @Test
    public void dropRightWhile() {
        assertEquals("[1, 2]", U.dropRightWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 2;
            }
        }).toString());
        assertEquals("[1, 2]", new U(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 2;
            }
        }).toString());
        assertEquals("[1, 2]", U.chain(asList(1, 2, 3)).dropRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 2;
            }
        }).value().toString());
    }

/*
_.fill([1, 2, 3], 4)
// → [4, 4, 4]
_.fill(["test1", "test2", "test3"], "res")
// → ["res", "res", "res"]
*/
    @Test
    public void fill() {
        assertEquals("[2, 2, 2]", U.fill(new ArrayList<Number>(Collections.nCopies(3, 0)), 2).toString());
        List<Object> array = new ArrayList<Object>(asList(4, 6, 8));
        U.fill(array, "*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3));
        new U<Object>(array).fill("a");
        assertEquals("[a, a, a]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3));
        U.chain(array).fill("a");
        array = new ArrayList<Object>(asList(4, 6, 8));
        new U<Object>(array).fill("*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
        array = new ArrayList<Object>(asList(4, 6, 8));
        U.chain(array).fill("*", 1, 2);
        assertEquals("[4, *, 8]", array.toString());
        List<Number> list = new ArrayList<Number>();
        list.add(1);
        list.add(2);
        list.add(3);
        final List<Number> result1 = U.fill(list, 4);
        assertEquals("[4, 4, 4]", result1.toString());
        List<String> list1 = new ArrayList<String>();
        list1.add("test1");
        list1.add("test1");
        list1.add("test1");
        final List<String> result2 = U.fill(list1, "res");
        assertEquals("[res, res, res]", result2.toString());
        final Number[] result3 = U.fill(new Number[] {1, 2, 3}, 4);
        assertEquals("[4, 4, 4]", asList(result3).toString());
    }

/*
_.flattenDeep([1, [2, 3, [4]]]);
// → [1, 2, 3, 4]
*/
    @SuppressWarnings("unchecked")
    @Test
    public void flattenDeep() {
        final List<Integer> result = U.flattenDeep(asList(1, asList(2, 3, asList(asList(4)))));
        assertEquals("[1, 2, 3, 4]", result.toString());
        final List<Integer> result2 = new U(asList(1, asList(2, 3, asList(asList(4))))).flattenDeep();
        assertEquals("[1, 2, 3, 4]", result2.toString());
        final List<?> resultChain = U.chain(asList(1, asList(2, 3, asList(asList(4))))).flattenDeep().value();
        assertEquals("[1, 2, 3, 4]", resultChain.toString());
    }

/*
var array = [1, 2, 3, 1, 2, 3];

_.pull(array, 2, 3);
console.log(array);
// → [1, 1]
*/
    @Test
    public void pull() {
        List<Object> array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        U.pull(array, 2, 3);
        assertEquals("[1, 1]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        new U<Object>(array).pull(2, 3);
        assertEquals("[1, 1]", array.toString());
        array = new ArrayList<Object>(asList(1, 2, 3, 1, 2, 3));
        U.chain(array).pull(2, 3);
        assertEquals("[1, 1]", array.toString());
    }

/*
var array = [5, 10, 15, 20];
var evens = _.pullAt(array, 1, 3);

console.log(array);
// → [5, 15]

console.log(evens);
// → [10, 20]
*/
    @Test
    public void pullAt() {
        List<Object> array = new ArrayList<Object>(asList(5, 10, 15, 20));
        List<Object> events = U.pullAt(array, 1, 3);
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
        array = new ArrayList<Object>(asList(5, 10, 15, 20));
        events = new U<Object>(array).pullAt(1, 3);
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
        array = new ArrayList<Object>(asList(5, 10, 15, 20));
        events = U.chain(array).pullAt(1, 3).value();
        assertEquals("[5, 15]", array.toString());
        assertEquals("[10, 20]", events.toString());
    }

/*
var array = [1, 2, 3, 4];
var evens = _.remove(array, function(n) {
  return n % 2 == 0;
});

console.log(array);
// → [1, 3]

console.log(evens);
// → [2, 4]
*/
    @Test
    public void remove() {
        List<Integer> array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        List<Integer> evens = U.remove(array, new Predicate<Integer>() {
            public boolean test(final Integer n) {
                return n % 2 == 0;
            }
        });
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
        array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        evens = new U<Integer>(array).remove(new Predicate<Integer>() {
            public boolean test(final Integer n) {
                return n % 2 == 0;
            }
        });
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
        array = new ArrayList<Integer>(asList(1, 2, 3, 4));
        evens = U.chain(array).remove(new Predicate<Integer>() {
            public boolean test(final Integer n) {
                return n % 2 == 0;
            }
        }).value();
        assertEquals("[1, 3]", array.toString());
        assertEquals("[2, 4]", evens.toString());
    }

/*
_.take([1, 2, 3]);
// → [1]

_.take([1, 2, 3], 2);
// → [1, 2]

_.take([1, 2, 3], 5);
// → [1, 2, 3]

_.take([1, 2, 3], 0);
// → []
*/
    @Test
    public void take() {
        assertEquals("[1]", U.take(asList(1, 2, 3)).toString());
        assertEquals("[1]", new U<Integer>(asList(1, 2, 3)).take().toString());
        assertEquals("[1]", U.chain(asList(1, 2, 3)).take().value().toString());
        assertEquals("[1, 2]", U.take(asList(1, 2, 3), 2).toString());
        assertEquals("[1, 2]", new U<Integer>(asList(1, 2, 3)).take(2).toString());
        assertEquals("[1, 2]", U.chain(asList(1, 2, 3)).take(2).value().toString());
        assertEquals("[1, 2, 3]", U.take(asList(1, 2, 3), 5).toString());
        assertEquals("[]", U.take(asList(1, 2, 3), 0).toString());
    }

/*
_.takeRight([1, 2, 3]);
// → [3]

_.takeRight([1, 2, 3], 2);
// → [2, 3]

_.takeRight([1, 2, 3], 5);
// → [1, 2, 3]

_.takeRight([1, 2, 3], 0);
// → []
*/
    @Test
    public void takeRight() {
        assertEquals("[3]", U.takeRight(asList(1, 2, 3)).toString());
        assertEquals("[3]", new U<Integer>(asList(1, 2, 3)).takeRight().toString());
        assertEquals("[3]", U.chain(asList(1, 2, 3)).takeRight().value().toString());
        assertEquals("[2, 3]", U.takeRight(asList(1, 2, 3), 2).toString());
        assertEquals("[2, 3]", new U<Integer>(asList(1, 2, 3)).takeRight(2).toString());
        assertEquals("[2, 3]", U.chain(asList(1, 2, 3)).takeRight(2).value().toString());
        assertEquals("[1, 2, 3]", U.takeRight(asList(1, 2, 3), 5).toString());
        assertEquals("[]", U.takeRight(asList(1, 2, 3), 0).toString());
    }

/*
_.takeWhile([1, 2, 3], function(n) {
  return n < 3;
});
// → [1, 2]
*/
    @Test
    public void takeWhile() {
        assertEquals("[1, 2]", U.takeWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[1, 2]", new U<Integer>(asList(1, 2, 3)).takeWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).toString());
        assertEquals("[1, 2]", U.chain(asList(1, 2, 3)).takeWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n < 3;
            }
        }).value().toString());
    }

/*
_.takeRightWhile([1, 2, 3], function(n) {
  return n > 1;
});
// → [2, 3]
*/
    @Test
    public void takeRightWhile() {
        assertEquals("[2, 3]", U.takeRightWhile(asList(1, 2, 3), new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[2, 3]", new U<Integer>(asList(1, 2, 3)).takeRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 1;
            }
        }).toString());
        assertEquals("[2, 3]", U.chain(asList(1, 2, 3)).takeRightWhile(new Predicate<Integer>() {
            public boolean test(Integer n) {
                return n > 1;
            }
        }).value().toString());
    }

/*
_.xor([1, 2], [4, 2]);
// → [1, 4]
*/
    @SuppressWarnings("unchecked")
    @Test
    public void xor() {
        assertEquals("[1, 4]", U.xor(asList(1, 2), asList(4, 2)).toString());
        assertEquals("[1, 4]", new U<Integer>(asList(1, 2)).xor(asList(4, 2)).toString());
        assertEquals("[1, 4]", U.chain(asList(1, 2)).xor(asList(4, 2)).value().toString());
    }


/*
_.at(['a', 'b', 'c'], 0, 2);
// → ['a', 'c']
*/
    @Test
    public void at() {
        assertEquals("[a, c]", U.at(asList("a", "b", "c"), 0, 2).toString());
        assertEquals("[a, c]", new U<String>(asList("a", "b", "c")).at(0, 2).toString());
        assertEquals("[a, c]", U.chain(asList("a", "b", "c")).at(0, 2).value().toString());
    }

/*
_.get({"a":[{"b":{"c":"d"}}]}, "a[0].b.c");
// → "d"
*/
    @SuppressWarnings("unchecked")
    @Test
    public void get() {
        assertEquals("d", U.<String>get(
                (Map<String, Object>) U.fromJson("{\"a\":[{\"b\":{\"c\":\"d\"}}]}"), "a[0].b.c"));
    }

/*
_.set({"a":[{"b":{"c":"d"}}]}, "a[0].b.c", "e");
// → "{a=[{b={c=e}}]}"
*/
    @SuppressWarnings("unchecked")
    @Test
    public void set() {
        assertEquals("d", U.<String>set(
            (Map<String, Object>) U.fromJson("{\"a\":[{\"b\":{\"c\":\"d\"}}]}"), "a[0].b.c", "e").toString());
        assertEquals("{b={c=d}}", U.set(
            (Map<String, Object>) U.fromJson("{\"a\":[{\"b\":{\"c\":\"d\"}}]}"), "a[0]", "e").toString());
        Map<String, Object> map = U.newLinkedHashMap();
        Map<String, Object> map2 = U.newLinkedHashMap();
        Map<String, Object> map3 = U.newLinkedHashMap();
        map.put("a", map2);
        map2.put("#item", map3);
        map3.put("b", "c");
        assertEquals("c", U.<String>set(map, "a.b", "b").toString());
        assertNull(U.<String>set((Map<String, Object>) null, "a", "b"));
        assertNull(U.<String>set(U.<String, Object>newLinkedHashMap(), "a.b", "b"));
        Map<String, Object> map4 = U.newLinkedHashMap();
        map4.put("a", "b");
        assertNull(U.<String>set(map4, "a.b", "b"));
    }

    @Test
    @SuppressWarnings("serial")
    public void getNull() {
        assertNull(U.<String>get((Map<String, Object>) null, "a[0].b.c"));
        assertNull(U.<String>get(new LinkedHashMap<String, Object>() { {
            put("b", LodashTest.class); } }, "a[0].b.c"));
        assertNull(U.<String>get(new LinkedHashMap<String, Object>() { {
            put("a", LodashTest.class); } }, "a[0].b.c"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getNotFound() {
        assertNull(U.<String>get(
            (Map<String, Object>) U.fromJson("{\"a\":[{\"b\":{\"c\":\"d\"}}]}"), "a[0].b.d"));
        assertNull(U.<String>get(
            (Map<String, Object>) U.fromJson("{\"a\":[{\"b\":{\"c\":\"d\"}}]}"), "a[0].d.c"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fetchGet() {
        U.FetchResponse result = U.fetch(
            "https://support.oneskyapp.com/hc/en-us/article_attachments/202761627/example_1.json");
        result.json();
//        assertEquals("{\"gameId\":483159,\"knight\":{\"name\":"
//            + "\"Sir. Russell Jones of Alberta\",\"attack\":2,\"armor\":7,\"agility\":3,\"endurance\":8}}",
//            result.text());
//        assertEquals("Sir. Russell Jones of Alberta",
//            (String) U.get((Map<String, Object>) result.json(), "knight.name"));
        U.Chain<?> resultChain = U.chain("https://www.dragonsofmugloar.com/api/game/483159").fetch();
//        assertEquals("{\"gameId\":483159,\"knight\":{\"name\":"
//            + "\"Sir. Russell Jones of Alberta\",\"attack\":2,\"armor\":7,\"agility\":3,\"endurance\":8}}",
//            resultChain.item());
        U.chain("http://www.dragonsofmugloar.com/api/game/483159").fetch();
    }

    @Test
    public void fetchGetWithTimeouts() {
        U.FetchResponse result = U.fetch("https://www.dragonsofmugloar.com/api/game/483159", 30000, 30000);
//        assertEquals("{\"gameId\":483159,\"knight\":{\"name\":"
//            + "\"Sir. Russell Jones of Alberta\",\"attack\":2,\"armor\":7,\"agility\":3,\"endurance\":8}}",
//            result.text());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void fetchGetXml() {
        U.FetchResponse result = U.fetch("https://www.w3schools.com/xml/note.xml");
        assertEquals("Tove", (String) U.get((Map<String, Object>) result.xml(), "note.to"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fetchResponseError() {
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream() {
            public String toString(String encoding) throws java.io.UnsupportedEncodingException {
                throw new java.io.UnsupportedEncodingException();
            }
        };
        new U.FetchResponse(true, 100, null, stream).text();
    }

    @Test
    public void fetchResponseBlob() {
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        assertArrayEquals(new byte[0], new U.FetchResponse(true, 100, null, stream).blob());
        assertNull(new U.FetchResponse(true, 100, null, stream).getHeaderFields());
        assertEquals(true, new U.FetchResponse(true, 100, null, stream).isOk());
        assertEquals(100, new U.FetchResponse(true, 100, null, stream).getStatus());
    }

    @Test
    public void fetchGetHttps() {
        U.FetchResponse result = U.fetch("https://api.lob.com/v1/addresses");
        assertEquals("{\n"
            + "    \"error\": {\n"
            + "        \"message\": \"Missing authentication\",\n"
            + "        \"status_code\": 401\n"
            + "    }\n"
            + "}", result.text());
    }

    @Test
    public void fetchPut() {
        U.FetchResponse result = U.fetch("https://www.dragonsofmugloar.com/api/game/31906/solution", "PUT", "{"
            + "    \"dragon\": {"
            + "        \"scaleThickness\": 4,"
            + "        \"clawSharpness\": 2,"
            + "        \"wingStrength\": 4,"
            + "        \"fireBreath\": 10"
            + "    }"
            + "}");
//        assertEquals("{\"status\":\"Victory\",\"message\":\"Dragon was successful in a glorious battle\"}",
//            result.text());
        U.FetchResponse result2 = U.fetch("https://www.dragonsofmugloar.com/api/game/31906/solution", "PUT", "{"
            + "    \"dragon\": {"
            + "        \"scaleThickness\": 4,"
            + "        \"clawSharpness\": 2,"
            + "        \"wingStrength\": 4,"
            + "        \"fireBreath\": 10"
            + "    }"
            + "}", null, null, null);
//        assertEquals("{\"status\":\"Defeat\",\"message\":"
//            + "\"No dragon showed up, knight dealt his deeds as he pleased.\"}", result2.text());
        U.Chain resultChain = U.chain("https://www.dragonsofmugloar.com/api/game/31906/solution").fetch("PUT", "{"
            + "    \"dragon\": {"
            + "        \"scaleThickness\": 4,"
            + "        \"clawSharpness\": 2,"
            + "        \"wingStrength\": 4,"
            + "        \"fireBreath\": 10"
            + "    }"
            + "}");
//        assertEquals("{\"status\":\"Victory\",\"message\":\"Dragon was successful in a glorious battle\"}",
//            resultChain.item());
    }

    @Test
    public void noHostnameVerifier() {
        new U.NoHostnameVerifier().verify("", (javax.net.ssl.SSLSession) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fetchWrongUrl() {
        U.fetch("ttt");
    }

    @Test
    public void xmlToJson() {
        assertEquals("{\n"
                + "  \"root\": [\n"
                + "    \"1\",\n"
                + "    \"2\"\n"
                + "  ],\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.xmlToJson("<root><element>1</element><element>2</element></root>"));
        assertEquals("{\n"
                + "  \"root\": [\n"
                + "    \"1\",\n"
                + "    \"2\"\n"
                + "  ],\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.chain("<root><element>1</element><element>2</element></root>").xmlToJson().item());
        assertEquals("{\n"
                + "  \"a\": {\n"
                + "    \"b\": [\n"
                + "      {\n"
                + "      },\n"
                + "      {\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.xmlToJson("<a>\n  <b></b>\n  <b></b>\n</a>"));
        assertEquals("[\n]", U.xmlToJson("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<root empty-array=\"true\"></root>"));
        assertEquals("{\n"
                + "  \"a\": null,\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.xmlToJson("<a/>", U.Mode.REPLACE_SELF_CLOSING_WITH_NULL));
        assertEquals("{\n"
                + "  \"a\": {\n"
                + "    \"-b\": \"c\"\n"
                + "  },\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.xmlToJson("<a b=\"c\"/>", U.Mode.REPLACE_SELF_CLOSING_WITH_NULL));
        assertEquals("{\n"
                + "  \"a\": {\n"
                + "    \"b\": [\n"
                + "      null,\n"
                + "      null\n"
                + "    ]\n"
                + "  },\n"
                + "  \"#omit-xml-declaration\": \"yes\"\n"
                + "}",
            U.xmlToJson("<a><b/><b/></a>", U.Mode.REPLACE_SELF_CLOSING_WITH_NULL));
        Map<String, Object> map = U.newLinkedHashMap();
        map.put("-self-closing", "false");
        U.replaceSelfClosingWithNull(map);
        Map<String, Object> map2 = U.newLinkedHashMap();
        List<Object> list = U.newArrayList();
        list.add(U.newArrayList());
        map2.put("list", list);
        U.replaceSelfClosingWithNull(map2);
    }

    @Test
    public void removeMapKey() {
        Map<String, Object> map = U.newLinkedHashMap();
        map.put("-self-closing", "false");
        U.remove(map, "test");
        U.remove(map, "-self-closing");
        Map<String, Object> map2 = U.newLinkedHashMap();
        List<Object> list = U.newArrayList();
        list.add(U.newArrayList());
        list.add(U.newLinkedHashMap());
        map2.put("list", list);
        U.remove(map2, "test");
        map2.put("list", U.newLinkedHashMap());
        U.remove(map2, "test");
    }

    @Test
    public void jsonToXml() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a></a>", U.jsonToXml("{\n  \"a\": {\n  }\n}"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a></a>",
            U.chain("{\n  \"a\": {\n  }\n}").jsonToXml().item());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root empty-array=\"true\"></root>",
            U.jsonToXml("[]"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>\n"
                + "  <a>\n"
                + "    <b>v1</b>\n"
                + "  </a>\n"
                + "  <c>\n"
                + "    <element>v1</element>\n"
                + "    <element>v2</element>\n"
                + "    <element>v3</element>\n"
                + "  </c>\n"
                + "</root>",
            U.jsonToXml("{\"a\" : {\n \"b\" : \"v1\" }, \"c\" : [\"v1\", \"v2\", \"v3\"]}"));
    }

    @Test
    public void formatXml() {
        assertEquals("<root>\n"
            + "   <element>1</element>\n"
            + "   <element>2</element>\n"
            + "</root>",
            U.formatXml("<root><element>1</element><element>2</element></root>"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n"
            + "   <element>1</element>\n"
            + "   <element>2</element>\n"
            + "</root>",
            U.formatXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>1</element>"
                + "<element>2</element></root>"));
        assertEquals("<a>\n   <b></b>\n   <b></b>\n</a>",
            U.formatXml("<a>\n  <b></b>\n  <b></b>\n</a>"));
        assertEquals("<a>\n    <b></b>\n    <b></b>\n</a>",
            U.formatXml("<a>\n  <b></b>\n  <b></b>\n</a>", Xml.XmlStringBuilder.Step.FOUR_SPACES));
        assertEquals("<a><b></b><b></b></a>",
            U.formatXml("<a>\n  <b></b>\n  <b></b>\n</a>", Xml.XmlStringBuilder.Step.COMPACT));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b></b><b></b></a>",
            U.formatXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?><a>\n  <b></b>\n  <b></b>\n</a>",
                Xml.XmlStringBuilder.Step.COMPACT));
        assertEquals("<a>\n\t<b></b>\n\t<b></b>\n</a>",
            U.formatXml("<a>\n  <b></b>\n  <b></b>\n</a>", Xml.XmlStringBuilder.Step.TABS));
    }

    @Test
    public void formatJson() {
        assertEquals("{\n   \"a\": {\n   }\n}", U.formatJson("{\n  \"a\": {\n  }\n}"));
        assertEquals("[\n]", U.formatJson("[]"));
        assertEquals("{\n    \"a\": {\n    }\n}",
            U.formatJson("{\n  \"a\": {\n  }\n}", Json.JsonStringBuilder.Step.FOUR_SPACES));
        assertEquals("{\"a\":{}}",
            U.formatJson("{\n  \"a\": {\n  }\n}", Json.JsonStringBuilder.Step.COMPACT));
        assertEquals("{\n\t\"a\": {\n\t}\n}",
            U.formatJson("{\n  \"a\": {\n  }\n}", Json.JsonStringBuilder.Step.TABS));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void removeMinusesAndConvertNumbers() {
        Map<String, Object> result = U.removeMinusesAndConvertNumbers((Map<String, Object>) U.fromXml("<a/>"));
        assertEquals("{a={}}", result.toString());
        Map<String, Object> result2 = U.removeMinusesAndConvertNumbers((Map<String, Object>) U.fromXml("<a b=\"c\"/>"));
        assertEquals("{a={b=c}}", result2.toString());
        Map<String, Object> result3 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b/><b/></a>"));
        assertEquals("{a={b=[{}, {}]}}", result3.toString());
        Map<String, Object> result4 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"1\"/></a>"));
        assertEquals("{a={b={c=1}}}", result4.toString());
        Map<String, Object> result5 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"-1e1\"/></a>"));
        assertEquals("{a={b={c=-10.0}}}", result5.toString());
        Map<String, Object> result6 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"-1E1\"/></a>"));
        assertEquals("{a={b={c=-10.0}}}", result6.toString());
        Map<String, Object> result7 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"1.a\"/></a>"));
        assertEquals("{a={b={c=1.a}}}", result7.toString());
        Map<String, Object> result8 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"1.-\"/></a>"));
        assertEquals("{a={b={c=1.-}}}", result8.toString());
        Map<String, Object> result9 = U.removeMinusesAndConvertNumbers(
            (Map<String, Object>) U.fromXml("<a><b c=\"+1ee\"/></a>"));
        assertEquals("{a={b={c=+1ee}}}", result9.toString());
        Map<String, Object> map = U.newLinkedHashMap();
        List<Object> list = U.newArrayList();
        list.add(U.newArrayList());
        map.put("list", list);
        Map<String, Object> result10 = U.removeMinusesAndConvertNumbers(map);
        assertEquals("{list=[[]]}", result10.toString());
    }

    @Test
    public void objectBuilder() {
        U.Builder builder = U.objectBuilder().add("1", "2").add("2");
        builder.add(builder);
        builder.toJson();
        U.Builder.fromJson("{}");
        builder.toXml();
        U.Builder.fromXml("<a/>");
        builder.set("1", "3");
        builder.toString();
        assertEquals("{1=3}", builder.build().toString());
        Map<String, Object> value = U.objectBuilder()
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            .add("address", U.objectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021"))
            .add("phoneNumber", U.objectBuilder()
                .add(U.objectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(U.objectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567")))
            .build();
        assertEquals("{firstName=John, lastName=Smith, age=25, address={streetAddress=21 2nd Street, "
            + "city=New York, state=NY, postalCode=10021}, phoneNumber={0={type=home, number=212 555-1234}, "
            + "1={type=fax, number=646 555-4567}}}", value.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void main() {
        new U(new ArrayList<String>());
        new U("");
        new U(asList()).chain();
        new Json();
        new Xml();
        U.chain(new ArrayList<String>());
        U.chain(new ArrayList<String>(), 1);
        U.chain(new HashSet<String>());
        U.chain(new String[] {});
        U.chain("");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void chain() {
        U.chain(new String[] {""}).first();
        U.chain(new String[] {""}).first(1);
        U.chain(new String[] {""}).firstOrNull();
        U.chain(new String[] {""}).firstOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).initial();
        U.chain(new String[] {""}).initial(1);
        U.chain(new String[] {""}).last();
        U.chain(new String[] {""}).last(1);
        U.chain(new String[] {""}).lastOrNull();
        U.chain(new String[] {""}).lastOrNull(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).rest();
        U.chain(new String[] {""}).rest(1);
        U.chain(new String[] {""}).compact();
        U.chain(new String[] {""}).compact("1");
        U.chain(new String[] {""}).flatten();
        U.chain(new Integer[] {0}).map(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).mapIndexed(new BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer index, Integer value) { return value; } });
        U.chain(new String[] {""}).filter(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).filterIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        U.chain(new String[] {""}).reject(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).rejectIndexed(new PredicateIndexed<String>() {
            public boolean test(int index, String str) { return true; } });
        U.chain(new String[] {""}).filterFalse(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).reduce(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        U.chain(new String[] {""}).reduce(new BinaryOperator<String>() {
            public String apply(String accum, String str) { return null; } });
        U.chain(new String[] {""}).reduceRight(new BiFunction<String, String, String>() {
            public String apply(String accum, String str) { return null; } }, "");
        U.chain(new String[] {""}).reduceRight(new BinaryOperator<String>() {
            public String apply(String accum, String str) { return null; } });
        U.chain(new String[] {""}).find(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).findLast(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new Integer[] {0}).max();
        U.chain(new Integer[] {0}).max(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).min();
        U.chain(new Integer[] {0}).min(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).sort();
        U.chain(new Integer[] {0}).sortWith(new Comparator<Integer>() {
            public int compare(Integer value1, Integer value2) { return value1; } });
        U.chain(new Integer[] {0}).sortBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).sortBy("");
        U.chain(new Integer[] {0}).groupBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).groupBy(
            new Function<Integer, Integer>() {
            public Integer apply(Integer num) {
                return num;
            }
            },
            new BinaryOperator<Integer>() {
            public Integer apply(Integer a, Integer b) {
                return a;
            }
            });
        U.chain(new Integer[] {0}).indexBy("");
        U.chain(new Integer[] {0}).countBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).countBy();
        U.chain(new Integer[] {0}).shuffle();
        U.chain(new Integer[] {0}).sample();
        U.chain(new Integer[] {0}).sample(1);
        U.chain(new int[] {0}).value();
        U.chain(new String[] {""}).tap(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).forEach(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).forEachRight(new Consumer<String>() {
            public void accept(String str) {
            } });
        U.chain(new String[] {""}).every(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).some(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).count(new Predicate<String>() {
            public boolean test(String str) { return true; } });
        U.chain(new String[] {""}).contains("");
        U.chain(new String[] {""}).invoke("toString", Collections.emptyList());
        U.chain(new String[] {""}).invoke("toString");
        U.chain(new String[] {""}).pluck("toString");
        U.chain(new String[] {""}).where(Collections.<Tuple<String, String>>emptyList());
        U.chain(new String[] {""}).findWhere(Collections.<Tuple<String, String>>emptyList());
        U.chain(new Integer[] {0}).uniq();
        U.chain(new Integer[] {0}).uniq(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
        U.chain(new Integer[] {0}).distinct();
        U.chain(new Integer[] {0}).distinctBy(new Function<Integer, Integer>() {
            public Integer apply(Integer value) { return value; } });
    }

    @SuppressWarnings("unchecked")
    @Test
    public void chain2() {
        U.chain(new String[] {""}).union();
        U.chain(new String[] {""}).intersection();
        U.chain(new String[] {""}).difference();
        U.chain(new String[] {""}).range(0);
        U.chain(new String[] {""}).range(0, 0);
        U.chain(new String[] {""}).range(0, 0, 1);
        U.chain(new String[] {""}).chunk(1);
        U.chain(new String[] {""}).chunk(1, 2);
        U.chain(new String[] {""}).chunkFill(1, "");
        U.chain(new String[] {""}).chunkFill(1, 2, "");
        U.chain(new String[] {""}).cycle(1);
        U.chain(new String[] {""}).interpose("");
        U.chain(new String[] {""}).interposeByList(Collections.<String>emptyList());
        U.chain(new String[] {""}).concat();
        U.chain(new String[] {""}).slice(0);
        U.chain(new String[] {""}).slice(0, 0);
        U.chain(new String[] {""}).reverse();
        U.chain(new String[] {""}).join();
        U.chain(new String[] {""}).join("");
        U.chain(new String[] {""}).skip(0);
        U.chain(new String[] {""}).limit(0);
        U.chain(new LinkedHashMap<Integer, Integer>().entrySet()).toMap();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stackoverflow() {
        // http://stackoverflow.com/questions/443499/convert-json-to-map
        String json = "{"
            + "    \"data\" :"
            + "    {"
            + "        \"field1\" : \"value1\","
            + "        \"field2\" : \"value2\""
            + "    }"
            + "}";

        Map<String, Object> data = (Map) U.get((Map<String, Object>) U.fromJson(json), "data");
        assertEquals("{field1=value1, field2=value2}", data.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stackoverflow2() {
        // http://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap
        String json = "{"
            + "\"name\" : \"abc\" ,"
            + "\"email id \" : [\"abc@gmail.com\",\"def@gmail.com\",\"ghi@gmail.com\"]"
            + "}";
        String data = (String) U.get((Map<String, Object>) U.fromJson(json), "email id .1");
        assertEquals("def@gmail.com", data);
    }

    @Test
    public void stackoverflow3() {
        // http://stackoverflow.com/questions/4550662/how-do-you-find-the-sum-of-all-the-numbers-in-an-array-in-java
        int sum = U.sum(Arrays.asList(1, 2, 3, 4));
        assertEquals(10, sum);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sqlrujava() {
        // http://www.sql.ru/forum/1232207/kak-pravilno-razobrat-json-org-json-simple
        String json =
        "{"
        + "  \"from_excel\":["
        + "    {"
        + "      \"solution\":\"Fisrt\","
        + "      \"num\":\"1\""
        + "    },"
        + "    {"
        + "      \"solution\":\"Second\","
        + "      \"num\":\"2\""
        + "    },"
        + "    {"
        + "      \"solution\":\"third\","
        + "      \"num\":\"3\""
        + "    },"
        + "    {"
        + "      \"solution\":\"fourth\","
        + "      \"num\":\"4\""
        + "    },"
        + "    {"
        + "      \"solution\":\"fifth\","
        + "      \"num\":\"5\""
        + "    }"
        + "  ]"
        + "}";

        List<Map<String, Object>> fromExcelData = (List<Map<String, Object>>) U.get(
            (Map<String, Object>) U.fromJson(json), "from_excel");
        assertEquals("[{solution=Fisrt, num=1}, {solution=Second, num=2}, {solution=third, num=3}, "
            + "{solution=fourth, num=4}, {solution=fifth, num=5}]", fromExcelData.toString());
        List<String> solutions = U.map(fromExcelData, new Function<Map<String, Object>, String>() {
            public String apply(Map<String, Object> item) {
                return (String) item.get("solution");
            }
        });
        assertEquals("[Fisrt, Second, third, fourth, fifth]", solutions.toString());
    }

    @Test
    public void stackoverflow4() {
        // http://stackoverflow.com/questions/25085399/converting-xml-into-java-mapstring-integer
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
        + "<root>\n"
        + "   <Durapipe type=\"int\">1</Durapipe>\n"
        + "   <EXPLAIN type=\"int\">2</EXPLAIN>\n"
        + "   <woods type=\"int\">2</woods>\n"
        + "   <hanging type=\"int\">3</hanging>\n"
        + "   <hastily type=\"int\">2</hastily>\n"
        + "   <localized type=\"int\">1</localized>\n"
        + "   <Schuster type=\"int\">5</Schuster>\n"
        + "   <regularize type=\"int\">1</regularize>\n"
        + "   <LASR type=\"int\">1</LASR>\n"
        + "   <LAST type=\"int\">22</LAST>\n"
        + "   <Gelch type=\"int\">2</Gelch>\n"
        + "   <Gelco type=\"int\">26</Gelco>\n"
        + "</root>";

        assertEquals("{Durapipe={-type=int, #text=1}, EXPLAIN={-type=int, #text=2}, "
                + "woods={-type=int, #text=2}, hanging={-type=int, #text=3}, "
                + "hastily={-type=int, #text=2}, localized={-type=int, #text=1}, "
                + "Schuster={-type=int, #text=5}, regularize={-type=int, #text=1}, "
                + "LASR={-type=int, #text=1}, LAST={-type=int, #text=22}, "
                + "Gelch={-type=int, #text=2}, Gelco={-type=int, #text=26}}",
                U.fromXml(xml).toString());
    }

    @Test
    public void stackoverflow5() {
        // https://stackoverflow.com/questions/59429211/
        // convert-xml-to-json-and-vice-versa-and-also-how-to-identify-rest-endpoint-while
        String xmlData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
        + " xmlns:urn=\"urn:ahc.com:dms:wsdls:organization\">\n"
        + "   <soapenv:Header/>\n"
        + "   <soapenv:Body>\n"
        + "      <urn:getRoles>\n"
        + "         <getRolesRequest>\n"
        + "            <Type>ABC</Type>\n"
        + "         </getRolesRequest>\n"
        + "      </urn:getRoles>\n"
        + "   </soapenv:Body>\n"
        + "</soapenv:Envelope>";
        Map<String, Object> jsonData = U.<Map<String, Object>>get(
            U.<Map<String, Object>>fromXmlWithoutNamespaces(xmlData), "Envelope.Body.getRoles");
        assertEquals("{getRolesRequest={Type=ABC}}", jsonData.toString());
    }

    @Test
    public void stackoverflow6() {
        // https://stackoverflow.com/questions/59585708/getting-null-pointer-while-reading-the-fileds-from-json-to-pojo
        String jsonData = "{\n"
        + "    \"TEST\": {\n"
        + "        \"NAME\": \"PART_TRAN\",\n"
        + "        \"VERSION\": \"9.0\",\n"
        + "        \"ID\": \"----\",\n"
        + "        \"SEGMENT\": {\n"
        + "            \"TYPE\": \"R\",\n"
        + "            \"CLIENT_ID\": \"----\",\n"
        + "            \"UN_NUM\": \"UN\"\n"
        + "        }"
        + "    }"
        + "}";
        Map<String, Object> jsonObject = U.fromJsonMap(jsonData);
        assertEquals("R", U.<String>get(jsonObject, "TEST.SEGMENT.TYPE"));
        assertEquals("UN", U.<String>get(jsonObject, "TEST.SEGMENT.UN_NUM"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sqlru1() {
        // http://www.sql.ru/forum/1296485/poluchit-nazvaniya-iz-json-v-jsp
        String json = "{\"memory\":[{\"alert\":\"false\",\"value\":\"50%\"}],\"cpu\":"
        + "[{\"alert\":\"true\",\"value\":\"100%\"}],\"hdd\":[{\"alert\":\"false\",\"value\":\"80%\"}]}";

        assertEquals("[memory, cpu, hdd]", U.keys((Map<String, Object>) U.fromJson(json)).toString());
    }

    @Test
    public void sqlru2() {
        // https://www.sql.ru/forum/1321326/kolichestvo-naydennyh-slov-v-stroke
        assertEquals(2, U.countBy(U.words("Маша ищет Мишу а Миша ищет Машу")).get("ищет").intValue());
    }
}
