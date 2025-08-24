/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2025 Valentyn Kolesnikov <0009-0003-9608-3364@orcid.org>
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
@SuppressWarnings("java:S5785")
class ObjectsTest {

    /*
    _.keys({one: 1, two: 2, three: 3});
    => ["one", "two", "three"]
    */
    @Test
    void keys() {
        Set<String> result =
                Underscore.keys(
                        new LinkedHashMap<>() {
                            {
                                put("one", 1);
                                put("two", 2);
                                put("three", 3);
                            }
                        });
        assertEquals("[one, two, three]", result.toString());
    }

    /*
    _.values({one: 1, two: 2, three: 3});
    => [1, 2, 3]
    */
    @Test
    void values() {
        Collection<Integer> result =
                Underscore.values(
                        new LinkedHashMap<String, Integer>() {
                            {
                                put("one", 1);
                                put("two", 2);
                                put("three", 3);
                            }
                        });
        assertEquals("[1, 2, 3]", result.toString());
    }

    /*
    _.pairs({one: 1, two: 2, three: 3});
    => [["one", 1], ["two", 2], ["three", 3]]
    */
    @Test
    void pairs() {
        List<Map.Entry<String, Integer>> result =
                Underscore.pairs(
                        new LinkedHashMap<>() {
                            {
                                put("one", 1);
                                put("two", 2);
                                put("three", 3);
                            }
                        });
        assertEquals("[one=1, two=2, three=3]", result.toString());
    }

    /*
    _.invert({Moe: "Moses", Larry: "Louis", Curly: "Jerome"});
    => {Moses: "Moe", Louis: "Larry", Jerome: "Curly"};
    */
    @Test
    void invert() {
        List<Map.Entry<String, String>> result =
                Underscore.invert(
                        new LinkedHashMap<>() {
                            {
                                put("Moe", "Moses");
                                put("Larry", "Louis");
                                put("Curly", "Jerome");
                            }
                        });
        assertEquals("[Moses=Moe, Louis=Larry, Jerome=Curly]", result.toString());
    }

    /*
    _.functions(_);
    => ["all", "any", "bind", "bindAll", "clone", "compact", "compose" ...
    */
    @Test
    void functions() {
        List<String> result = Underscore.functions(Underscore.class);
        assertEquals(5, Underscore.first(result, 5).size());
    }

    /*
    _.methods(_);
    => ["all", "any", "bind", "bindAll", "clone", "compact", "compose" ...
    */
    @Test
    void methods() {
        List<String> result = Underscore.methods(Underscore.class);
        assertEquals(5, Underscore.first(result, 5).size());
    }

    /*
    _.pick({name: 'moe', age: 50, userid: 'moe1'}, 'name', 'age');
    => {name: 'moe', age: 50}
    _.pick({name: 'moe', age: 50, userid: 'moe1'}, function(value, key, object) {
      return _.isNumber(value);
    });
    => {age: 50}
    */
    @Test
    void pick() {
        final List<Map.Entry<String, Object>> result =
                Underscore.pick(
                        new LinkedHashMap<>() {
                            {
                                put("name", "moe");
                                put("age", 50);
                                put("userid", "moe1");
                            }
                        },
                        "name",
                        "age");
        assertEquals("[name=moe, age=50]", result.toString());
        final List<Map.Entry<String, Object>> result2 =
                Underscore.pick(
                        new LinkedHashMap<>() {
                            {
                                put("name", "moe");
                                put("age", 50);
                                put("userid", "moe1");
                            }
                        },
                        Number.class::isInstance);
        assertEquals("[age=50]", result2.toString());
    }

    /*
    _.omit({name: 'moe', age: 50, userid: 'moe1'}, 'userid');
    => {name: 'moe', age: 50}
    _.omit({name: 'moe', age: 50, userid: 'moe1'}, function(value, key, object) {
      return _.isNumber(value);
    });
    => {name: 'moe', userid: 'moe1'}
    */
    @Test
    void omit() {
        final List<Map.Entry<String, Object>> result =
                Underscore.omit(
                        new LinkedHashMap<>() {
                            {
                                put("name", "moe");
                                put("age", 50);
                                put("userid", "moe1");
                            }
                        },
                        "userid");
        assertEquals("[name=moe, age=50]", result.toString());
        final List<Map.Entry<String, Object>> result2 =
                Underscore.omit(
                        new LinkedHashMap<>() {
                            {
                                put("name", "moe");
                                put("age", 50);
                                put("userid", "moe1");
                            }
                        },
                        Number.class::isInstance);
        assertEquals("[name=moe, userid=moe1]", result2.toString());
    }

    /*
    var iceCream = {flavor: "chocolate"};
    _.defaults(iceCream, {flavor: "vanilla", sprinkles: "lots"});
    => {flavor: "chocolate", sprinkles: "lots"}
    */
    @Test
    void defaults() {
        Map<String, String> iceCream =
                new LinkedHashMap<>() {
                    {
                        put("flavor", "chocolate");
                    }
                };
        Map<String, String> result =
                Underscore.defaults(
                        iceCream,
                        new LinkedHashMap<>() {
                            {
                                put("flavor", "vanilla");
                                put("sprinkles", "lots");
                            }
                        });
        assertEquals("{flavor=chocolate, sprinkles=lots}", result.toString());
    }

    /*
    _.clone({name: 'moe'});
    => {name: 'moe'};
    */
    @Test
    @SuppressWarnings("unchecked")
    void cloneMap() {
        Map<String, String> result =
                (Map<String, String>)
                        Underscore.clone(
                                new LinkedHashMap<String, String>() {
                                    {
                                        put("name", "moe");
                                    }
                                });
        assertEquals("{name=moe}", result.toString());
        Integer[] result2 = Underscore.clone(new Integer[] {1, 2, 3, 4, 5});
        assertEquals("[1, 2, 3, 4, 5]", asList(result2).toString());
    }

    @Test
    void cloneError() {
        class Test {}
        Test test = new Test();
        assertThrows(IllegalArgumentException.class, () -> Underscore.clone(test));
    }

    @Test
    void cloneError2() {
        @SuppressWarnings("java:S1172")
        class Test implements Cloneable {
            public Object clone(String arg) {
                return null;
            }
        }
        Test test = new Test();
        assertThrows(IllegalArgumentException.class, () -> Underscore.clone(test));
    }

    @Test
    void cloneError3() {
        class Test implements Cloneable {
            public Object clone() throws CloneNotSupportedException {
                super.clone();
                throw new RuntimeException();
            }
        }
        Test test = new Test();
        assertThrows(IllegalArgumentException.class, () -> Underscore.clone(test));
    }

    /*
    var stooge = {name: 'moe', luckyNumbers: [13, 27, 34]};
    var clone  = {name: 'moe', luckyNumbers: [13, 27, 34]};
    stooge == clone;
    => false
    _.isEqual(stooge, clone);
    => true
    _.isEqual(null, null)
    => true
    _.isEqual('Curly', 'Curly')
    => true
    */
    @Test
    void isEqual() {
        Map<String, Object> stooge =
                new LinkedHashMap<>() {
                    {
                        put("name", "moe");
                        put("luckyNumbers", asList(13, 27, 34));
                    }
                };
        Map<String, Object> clone =
                new LinkedHashMap<>() {
                    {
                        put("name", "moe");
                        put("luckyNumbers", asList(13, 27, 34));
                    }
                };
        assertNotSame(stooge, clone);
        assertTrue(Underscore.isEqual(stooge, clone));
        assertTrue(Underscore.isEqual(null, null));
        assertFalse(Underscore.isEqual(stooge, null));
        assertFalse(Underscore.isEqual(null, clone));
        assertTrue(Underscore.isEqual("Curly", "Curly"));
        assertTrue(Underscore.isEqual(0, -0));
        assertTrue(Underscore.isEqual(75, 75));
    }

    @Test
    @SuppressWarnings("unchecked")
    void isEmpty() {
        assertTrue(Underscore.isEmpty((List<Object>) null));
        assertTrue(Underscore.isEmpty(new ArrayList<String>()));
        assertTrue(new Underscore<>((List<Object>) null).isEmpty());
        assertTrue(new Underscore<>(new ArrayList<String>()).isEmpty());
        assertTrue(Underscore.chain((List<Object>) null).isEmpty());
        assertTrue(Underscore.chain(new ArrayList<String>()).isEmpty());
        assertFalse(Underscore.isEmpty(Collections.singletonList("")));
        assertFalse(new Underscore<>(Collections.singletonList("")).isEmpty());
        assertFalse(Underscore.chain(Collections.singletonList("")).isEmpty());
        assertTrue(Underscore.isEmpty((Map<String, Object>) null));
        assertTrue(Underscore.isEmpty(new HashMap<String, String>()));
        assertFalse(
                Underscore.isEmpty(
                        new HashMap<String, String>() {
                            {
                                put("", "");
                            }
                        }));
    }

    @Test
    @SuppressWarnings("unchecked")
    void isNotEmpty() {
        assertFalse(Underscore.isNotEmpty((List<Object>) null));
        assertFalse(Underscore.isNotEmpty(new ArrayList<String>()));
        assertFalse(new Underscore<>((List<Object>) null).isNotEmpty());
        assertFalse(new Underscore<>(new ArrayList<String>()).isNotEmpty());
        assertFalse(Underscore.chain((List<Object>) null).isNotEmpty());
        assertFalse(Underscore.chain(new ArrayList<String>()).isNotEmpty());
        assertTrue(Underscore.isNotEmpty(Collections.singletonList("")));
        assertTrue(new Underscore<>(Collections.singletonList("")).isNotEmpty());
        assertTrue(Underscore.chain(Collections.singletonList("")).isNotEmpty());
        assertFalse(Underscore.isNotEmpty((Map<String, Object>) null));
        assertFalse(Underscore.isNotEmpty(new HashMap<String, String>()));
        assertTrue(
                Underscore.isNotEmpty(
                        new HashMap<String, String>() {
                            {
                                put("", "");
                            }
                        }));
    }

    /*
    _.isObject({});
    => true
    _.isObject(1);
    => false
    */
    @Test
    void isObject() {
        assertTrue(Underscore.isObject(new LinkedHashMap<String, String>()));
        assertFalse(Underscore.isObject(null));
        assertFalse(Underscore.isObject("string"));
    }

    /*
    _.isArray([1,2,3]);
    => true
    */
    @Test
    void isArray() {
        assertTrue(Underscore.isArray(new int[] {1, 2, 3, 4, 5}));
        assertFalse(Underscore.isArray(null));
        assertFalse(Underscore.isArray("string"));
    }

    /*
    _.isString("moe");
    => true
    */
    @Test
    void isString() {
        assertTrue(Underscore.isString("moe"));
    }

    /*
    _.isNumber(8.4 * 5);
    => true
    */
    @Test
    void isNumber() {
        assertTrue(Underscore.isNumber(8.4 * 5));
    }

    /*
    _.isBoolean(null);
    => false
    */
    @Test
    void isBoolean() {
        assertTrue(Underscore.isBoolean(false));
        assertFalse(Underscore.isBoolean(null));
    }

    /*
    _.isFunction(alert);
    => true
    */
    @Test
    void isFunction() {
        assertTrue(Underscore.isFunction((Function<String, Integer>) arg -> null));
    }

    /*
    _.isDate(new Date());
    => true
    */
    @Test
    void isDate() {
        assertTrue(Underscore.isDate(new java.util.Date()));
        assertFalse(Underscore.isDate(null));
    }

    /*
    _.isRegExp(/moe/);
    => true
    */
    @Test
    void isRegExp() {
        assertTrue(Underscore.isRegExp(java.util.regex.Pattern.compile("moe")));
        assertFalse(Underscore.isRegExp(null));
    }

    /*
    _.isNull(null);
    => true
    */
    @Test
    void isNull() {
        assertTrue(Underscore.isNull(null));
        assertFalse(Underscore.isNull(""));
    }

    /*
    try {
      throw new TypeError("Example");
    } catch (o_O) {
      _.isError(o_O)
    }
    => true
    */
    @Test
    void isError() {
        assertTrue(Underscore.isError(new Exception()));
        assertFalse(Underscore.isError(null));
    }

    /*
    _.chain([1,2,3,200])
      .filter(function(num) { return num % 2 == 0; })
      .tap(alert)
      .map(function(num) { return num * num })
      .value();
    => // [2, 200] (alerted)
    => [4, 40000]
    */
    @Test
    void tap() {
        final List<Map.Entry<String, Integer>> result = new ArrayList<>();
        Underscore.tap(
                (new LinkedHashMap<String, Integer>() {
                            {
                                put("a", 1);
                                put("b", 2);
                                put("c", 3);
                            }
                        })
                        .entrySet(),
                result::add);
        assertEquals("[a=1, b=2, c=3]", result.toString());
        final List<Map.Entry<String, Integer>> resultChain = new ArrayList<>();
        Underscore.chain(
                        (new LinkedHashMap<String, Integer>() {
                                    {
                                        put("a", 1);
                                        put("b", 2);
                                        put("c", 3);
                                    }
                                })
                                .entrySet())
                .tap(resultChain::add);
        assertEquals("[a=1, b=2, c=3]", resultChain.toString());
    }

    /*
    _.has({a: 1, b: 2, c: 3}, "b");
    => true
    */
    @Test
    void has() {
        boolean result =
                Underscore.has(
                        new LinkedHashMap<String, Integer>() {
                            {
                                put("a", 1);
                                put("b", 2);
                                put("c", 3);
                            }
                        },
                        "b");
        assertTrue(result);
    }

    /*
    var stooge = {name: 'moe', age: 32};
    _.isMatch(stooge, {age: 32});
    => true
    */
    @Test
    void isMatch() {
        Map<String, Object> stooge =
                new LinkedHashMap<>() {
                    {
                        put("name", "moe");
                        put("age", 32);
                    }
                };
        assertTrue(
                Underscore.isMatch(
                        stooge,
                        new LinkedHashMap<>() {
                            {
                                put("age", 32);
                            }
                        }));
        Map<String, Object> stooge2 =
                new LinkedHashMap<>() {
                    {
                        put("name", "moe");
                        put("age", 33);
                    }
                };
        assertFalse(
                Underscore.isMatch(
                        stooge2,
                        new LinkedHashMap<>() {
                            {
                                put("age", 32);
                            }
                        }));
        Map<String, Object> stooge3 =
                new LinkedHashMap<>() {
                    {
                        put("name", "moe");
                    }
                };
        assertFalse(
                Underscore.isMatch(
                        stooge3,
                        new LinkedHashMap<>() {
                            {
                                put("age", 32);
                            }
                        }));
    }

    /*
    var ready = _.matcher({selected: true, visible: true});
    var readyToGoList = _.filter(list, ready);
    */
    @Test
    @SuppressWarnings("unchecked")
    void matcher() {
        List<Map<String, Object>> list =
                Arrays.asList(
                        new LinkedHashMap<>() {
                            {
                                put("name", "moe");
                                put("selected", true);
                                put("visible", true);
                            }
                        },
                        new LinkedHashMap<>() {
                            {
                                put("name", "larry");
                                put("selected", true);
                                put("visible", false);
                            }
                        },
                        new LinkedHashMap<>() {
                            {
                                put("name", "curly");
                            }
                        });
        Predicate<Map<String, Object>> ready =
                Underscore.matcher(
                        new LinkedHashMap<>() {
                            {
                                put("selected", true);
                                put("visible", true);
                            }
                        });
        List<Map<String, Object>> result = Underscore.filter(list, ready);
        assertEquals("[{name=moe, selected=true, visible=true}]", result.toString());
    }

    /*
    _.findKey([1, 2, 3], function(item) {return item % 2  === 0; });
    => 2
    */
    @Test
    void findKey() {
        final Integer result = Underscore.findKey(asList(1, 2, 3), item -> item % 2 == 0);
        assertEquals(2, result.intValue());
        final Integer resultNotFound = Underscore.findKey(asList(1, 2, 3), item -> item > 3);
        assertNull(resultNotFound);
        final Integer resultArray =
                Underscore.findKey(new Integer[] {1, 2, 3}, item -> item % 2 == 0);
        assertEquals(2, resultArray.intValue());
    }

    /*
    _.findLastKey([1, 2, 3, 4, 5], function(item) {return item % 2  === 0; });
    => 4
    */
    @Test
    void findLastKey() {
        final Integer result = Underscore.findLastKey(asList(1, 2, 3, 4, 5), item -> item % 2 == 0);
        assertEquals(4, result.intValue());
        final Integer resultNotFound =
                Underscore.findLastKey(asList(1, 2, 3, 4, 5), item -> item > 5);
        assertNull(resultNotFound);
        final Integer resultArray =
                Underscore.findLastKey(new Integer[] {1, 2, 3, 4, 5}, item -> item % 2 == 0);
        assertEquals(4, resultArray.intValue());
    }

    /*
    _.extend({name: 'moe'}, {age: 50});
    => {name: 'moe', age: 50}
    */
    @Test
    @SuppressWarnings("unchecked")
    void extend() {
        assertEquals(
                "{name=moe, age=50}",
                Underscore.extend(
                                new LinkedHashMap<String, Object>() {
                                    {
                                        put("name", "moe");
                                    }
                                },
                                new LinkedHashMap<>() {
                                    {
                                        put("age", 50);
                                    }
                                })
                        .toString());
    }

    /*
    _.mapObject({start: 5, end: 12}, function(val, key) {
      return val + 5;
    });
    => {start: 10, end: 17}
    */
    @Test
    void mapObject() {
        List<Map.Entry<String, Integer>> result =
                Underscore.mapObject(
                        new LinkedHashMap<>() {
                            {
                                put("start", 5);
                                put("end", 12);
                            }
                        },
                        item -> item + 5);
        assertEquals("[start=10, end=17]", result.toString());
    }
}
