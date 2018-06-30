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

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class ChainingTest {

/*
var stooges = [{name: 'curly', age: 25}, {name: 'moe', age: 21}, {name: 'larry', age: 23}];
var youngest = _.chain(stooges)
  .sortBy(function(stooge){ return stooge.age; })
  .map(function(stooge){ return stooge.name + ' is ' + stooge.age; })
  .first()
  .value();
=> "moe is 21"
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain() {
        final List<Map<String, Object>> stooges = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { { put("name", "curly"); put("age", 25); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "moe"); put("age", 21); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "larry"); put("age", 23); } });
        } };
        final String youngest = U.chain(stooges)
            .sortBy(
                new Function<Map<String, Object>, Integer>() {
                public Integer apply(Map<String, Object> item) {
                    return (Integer) item.get("age");
                }
            })
            .map(
                new Function<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item();
        assertEquals("moe is 21", youngest);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chainSet() {
        final Set<Map<String, Object>> stooges = new HashSet<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { { put("name", "curly"); put("age", 25); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "moe"); put("age", 21); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "larry"); put("age", 23); } });
        } };
        final String youngest = U.chain(stooges)
            .sortBy(
                new Function<Map<String, Object>, Integer>() {
                public Integer apply(Map<String, Object> item) {
                    return (Integer) item.get("age");
                }
            })
            .map(
                new Function<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item();
        assertEquals("moe is 21", youngest);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chainObj() {
        final Set<Map<String, Object>> stooges = new HashSet<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { { put("name", "curly"); put("age", 25); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "moe"); put("age", 21); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "larry"); put("age", 23); } });
        } };
        final String youngest = new U(stooges).chain()
            .sortBy(
                new Function<Map<String, Object>, Integer>() {
                public Integer apply(Map<String, Object> item) {
                    return (Integer) item.get("age");
                }
            })
            .map(
                new Function<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item().toString();
        assertEquals("moe is 21", youngest);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chainArray() {
        final List<Map<String, Object>> stooges = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { { put("name", "curly"); put("age", 25); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "moe"); put("age", 21); } });
            add(new LinkedHashMap<String, Object>() { { put("name", "larry"); put("age", 23); } });
        } };
        final String youngest = U.chain(U.toArray(stooges))
            .sortBy(
                new Function<Map<String, Object>, Integer>() {
                public Integer apply(Map<String, Object> item) {
                    return (Integer) item.get("age");
                }
            })
            .map(
                new Function<Map<String, Object>, String>() {
                public String apply(Map<String, Object> item) {
                    return item.get("name") + " is " + item.get("age");
                }
            })
            .first().item();
        assertEquals("moe is 21", youngest);
    }

/*
var lyrics = [
  {line: 1, words: "I'm a lumberjack and I'm okay"},
  {line: 2, words: "I sleep all night and I work all day"},
  {line: 3, words: "He's a lumberjack and he's okay"},
  {line: 4, words: "He sleeps all night and he works all day"}
];

_.chain(lyrics)
  .map(function(line) { return line.words.split(' '); })
  .flatten()
  .reduce(function(counts, word) {
    counts[word] = (counts[word] || 0) + 1;
    return counts;
  }, {})
  .value();

=> {lumberjack: 2, all: 4, night: 2 ... }
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain2() {
        final List<Map<String, Object>> lyrics = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { {
                put("line", 1); put("words", "I'm a lumberjack and I'm okay"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 2); put("words", "I sleep all night and I work all day"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 3); put("words", "He's a lumberjack and he's okay"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 4); put("words", "He sleeps all night and he works all day"); } });
        } };
        final String result = U.chain(lyrics)
            .map(
                new Function<Map<String, Object>, List<String>>() {
                public List<String> apply(Map<String, Object> item) {
                    return asList(String.valueOf(item.get("words")).split(" "));
                }
            })
            .flatten()
            .reduce(
                new BiFunction<Map<String, Integer>, String, Map<String, Integer>>() {
                public Map<String, Integer> apply(Map<String, Integer> accum, String item) {
                    if (accum.get(item) == null) {
                        accum.put(item, 1);
                    } else {
                        accum.put(item, accum.get(item) + 1);
                    }
                    return accum;
                }
            },
            new LinkedHashMap<String, Integer>()
            )
            .item().toString();
        assertEquals("{I'm=2, a=2, lumberjack=2, and=4, okay=2, I=2, sleep=1, all=4, night=2, work=1, day=2, He's=1,"
            + " he's=1, He=1, sleeps=1, he=1, works=1}", result);
    }

/*
var lyrics = [
  {line: 1, words: "I'm a lumberjack and I'm okay"},
  {line: 2, words: "I sleep all night and I work all day"},
  {line: 3, words: "He's a lumberjack and he's okay"},
  {line: 4, words: "He sleeps all night and he works all day"}
];

_.chain(lyrics)
  .map(function(line) { return line.words.split(' '); })
  .flatten()
  .reduceRight(function(counts, word) {
    counts[word] = (counts[word] || 0) + 1;
    return counts;
  }, {})
  .value();

=> {day=2, all=4, works=1 ... }
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain3() {
        final List<Map<String, Object>> lyrics = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { {
                put("line", 1); put("words", "I'm a lumberjack and I'm okay"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 2); put("words", "I sleep all night and I work all day"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 3); put("words", "He's a lumberjack and he's okay"); } });
            add(new LinkedHashMap<String, Object>() { {
                put("line", 4); put("words", "He sleeps all night and he works all day"); } });
        } };
        final String result = U.chain(lyrics)
            .map(
                new Function<Map<String, Object>, List<String>>() {
                public List<String> apply(Map<String, Object> item) {
                    return asList(String.valueOf(item.get("words")).split(" "));
                }
            })
            .flatten()
            .reduceRight(
                new BiFunction<Map<String, Integer>, String, Map<String, Integer>>() {
                public Map<String, Integer> apply(Map<String, Integer> accum, String item) {
                    if (accum.get(item) == null) {
                        accum.put(item, 1);
                    } else {
                        accum.put(item, accum.get(item) + 1);
                    }
                    return accum;
                }
            },
            new LinkedHashMap<String, Integer>()
            )
            .item().toString();
        assertEquals("{day=2, all=4, works=1, he=1, and=4, night=2, sleeps=1,"
                + " He=1, okay=2, he's=1, lumberjack=2, a=2, He's=1, work=1, I=2, sleep=1, I'm=2}", result);
    }

/*
var doctors = [
    { number: 1,  actor: "William Hartnell",      begin: 1963, end: 1966 },
    { number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 },
    { number: 10, actor: "David Tennant",         begin: 2005, end: 2010 }
];
_.chain(doctors)
    .filter(function(doctor) {
        return doctor.begin > 2000;
    })
    .reject(function(doctor) {
        return doctor.begin > 2009;
    })
    .map(function(doctor) {
        return {
            doctorNumber: "#" + doctor.number,
            playedBy: doctor.actor,
            yearsPlayed: doctor.end - doctor.begin + 1
        };
    })
    .value();

=>  [{ doctorNumber: "#9",  playedBy: "Christopher Eccleston", yearsPlayed: 1 }]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain4() {
        final List<Map<String, Object>> doctors = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { {
                put("number", 1); put("actor", "William Hartnell"); put("begin", 1963); put("end", 1966); } });
            add(new LinkedHashMap<String, Object>() { {
                put("number", 9); put("actor", "Christopher Eccleston"); put("begin", 2005); put("end", 2005); } });
            add(new LinkedHashMap<String, Object>() { {
                put("number", 10); put("actor", "David Tennant"); put("begin", 2005); put("end", 2010); } });
        } };
        final String result = U.chain(doctors)
            .filter(
                new Predicate<Map<String, Object>>() {
                public boolean test(Map<String, Object> item) {
                    return (Integer) item.get("begin") > 2000;
                }
            })
            .reject(
                new Predicate<Map<String, Object>>() {
                public boolean test(Map<String, Object> item) {
                    return (Integer) item.get("end") > 2009;
                }
            })
            .map(
                new Function<Map<String, Object>, Map<String, Object>>() {
                public Map<String, Object> apply(final Map<String, Object> item) {
                    return new LinkedHashMap<String, Object>() { {
                        put("doctorNumber", "#" + item.get("number"));
                        put("playedBy", item.get("actor"));
                        put("yearsPlayed", (Integer) item.get("end") - (Integer) item.get("begin") + 1);
                    } };
                }
            })
            .value().toString();
        assertEquals("[{doctorNumber=#9, playedBy=Christopher Eccleston, yearsPlayed=1}]", result);
    }

/*
var doctors = [
    { number: 1,  actor: "William Hartnell",      begin: 1963, end: 1966 },
    { number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 },
    { number: 10, actor: "David Tennant",         begin: 2005, end: 2010 }
];
_.chain(doctors)
    .skip(1)
    .limit(1)
    .value();

=>  [{ number: 9,  actor: "Christopher Eccleston", begin: 2005, end: 2005 }]
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain5() {
        final List<Map<String, Object>> doctors = new ArrayList<Map<String, Object>>() { {
            add(new LinkedHashMap<String, Object>() { {
                put("number", 1); put("actor", "William Hartnell"); put("begin", 1963); put("end", 1966); } });
            add(new LinkedHashMap<String, Object>() { {
                put("number", 9); put("actor", "Christopher Eccleston"); put("begin", 2005); put("end", 2005); } });
            add(new LinkedHashMap<String, Object>() { {
                put("number", 10); put("actor", "David Tennant"); put("begin", 2005); put("end", 2010); } });
        } };
        final String result = U.chain(doctors)
            .skip(1)
            .limit(1)
            .value().toString();
        assertEquals("[{number=9, actor=Christopher Eccleston, begin=2005, end=2005}]", result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chain6() {
        final List<Comparable> result = U.chain(U.chain(U.class.getDeclaredMethods())
            .reduce(new BiFunction<List<String>, Method, List<String>>() {
                public List<String> apply(final List<String> accum, final Method method) {
                    accum.add(method.getName());
                    return accum;
                }
            }, new ArrayList<String>()).item())
            .reject(new Predicate<String>() {
                public boolean test(final String name) {
                    return name.contains("$");
                }
            })
            .uniq()
            .sort()
            .first(4)
            .value();
        assertEquals(4, result.size());
    }

/*
var words = ["Gallinule", "Escambio", "Aciform", "Entortilation", "Extensibility"];
var sum = _(words)
        .filter(function(w){return w[0] == "E"})
        .map(function(w){return w.length})
        .reduce(function(acc, curr){return acc + curr});
=> 34
*/
    @Test
    @SuppressWarnings("unchecked")
    public void chain7() {
        String[] words = new String[] {"Gallinule", "Escambio", "Aciform", "Entortilation", "Extensibility"};
        int sum = U.chain(words)
          .filter(new Predicate<String>() {
              public boolean test(String w) {
                  return w.startsWith("E");
              }
          })
          .map(new Function<String, Integer>() {
              public Integer apply(String w) {
                  return w.length();
              }
          })
          .reduce(new BiFunction<Integer, Integer, Integer>() {
              public Integer apply(Integer accum, Integer length) {
                  return accum + length;
              }
          }, 0).item();
        assertEquals(34, sum);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chain8() {
        final List<Comparable> result = U.chain(U.class.getDeclaredMethods())
            .map(new Function<Method, String>() {
                public String apply(final Method method) {
                    return method.getName();
                }
            })
            .reject(new Predicate<String>() {
                public boolean test(final String name) {
                    return name.contains("$");
                }
            })
            .uniq(new Function<String, Character>() {
                public Character apply(final String name) {
                    // Contrived example to test that .uniq returns
                    // a Chain<String> rather than a Chain<Character>.
                    return name.charAt(0);
                }
            })
            .sort()
            .first(4)
            .value();
        assertEquals(4, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void chain9() {
        final List<Comparable> result = U.chain(U.chain(U.class.getDeclaredMethods())
            .reduce(new BiFunction<List<String>, Method, List<String>>() {
                public List<String> apply(final List<String> accum, final Method method) {
                    accum.add(method.getName());
                    return accum;
                }
            }, new ArrayList<String>()).item())
            .filterFalse(new Predicate<String>() {
                public boolean test(final String name) {
                    return name.contains("$");
                }
            })
            .uniq()
            .sort()
            .first(4)
            .value();
        assertEquals(4, result.size());
    }

    @Test
    public void chainToMap() {
        assertEquals("{name1=one, name2=two}", U.chain((new LinkedHashMap<String, String>() { {
            put("name1", "one");
            put("name2", "two");
        } }).entrySet()).toMap().item().toString());
    }

    @Test
    public void chainRange() {
        assertEquals("[0, 1, 2]", U.chain(U.range(3)).value().toString());
    }

}
