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
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
class FunctionsTest {

    /*
    var func = function(greeting){ return greeting + ': ' + this.name };
    func = _.bind(func, {name: 'moe'}, 'hi');
    func();
    => 'hi: moe'
    */
    @Test
    void bind() {
        class GreetingFunction implements Function<String, String> {
            private final String name;

            public GreetingFunction(final String name) {
                this.name = name;
            }

            public String apply(final String greeting) {
                return greeting + ": " + this.name;
            }
        }
        assertEquals("hi: moe", Underscore.bind(new GreetingFunction("moe")).apply("hi"));
    }

    /*
    var subtract = function(a, b) { return b - a; };
    sub5 = _.partial(subtract, 5);
    sub5(20);
    => 15
    */
    @Test
    void partial() {
        class SubtractFunction implements Function<Integer, Integer> {
            private final Integer arg1;

            public SubtractFunction(final Integer arg1) {
                this.arg1 = arg1;
            }

            public Integer apply(final Integer arg2) {
                return arg2 - arg1;
            }
        }
        Function<Integer, Integer> sub5 = new SubtractFunction(5);
        assertEquals(15, sub5.apply(20).intValue());
    }

    /*
    var fibonacci = _.memoize(function(n) {
      return n < 2 ? n: fibonacci(n - 1) + fibonacci(n - 2);
    });
    */
    @Test
    void memoize() {
        class FibonacciFuncion1 extends MemoizeFunction<Integer, Integer> {
            public Integer calc(final Integer n) {
                return n < 2 ? n : apply(n - 1) + apply(n - 2);
            }
        }
        assertEquals(55, new FibonacciFuncion1().apply(10).intValue());
        Function<Integer, Integer> memoizeFunction =
                Underscore.memoize(
                        new Function<Integer, Integer>() {
                            public Integer apply(final Integer n) {
                                return n < 2 ? n : apply(n - 1) + apply(n - 2);
                            }
                        });
        assertEquals(55, memoizeFunction.apply(10).intValue());
    }

    /*
    var counter = 0;
    var incr = function(){ counter++; };
    var throttleIncr = _.throttle(incr, 32);
    throttleIncr(); throttleIncr();
    _.delay(throttleIncr, 16);
    _.delay(function(){ equal(counter, 1, 'incr was throttled'); }, 96);
    */

    @Test
    void throttle() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    counter[0]++;
                    return null;
                };
        final Supplier<Void> throttleIncr = Underscore.throttle(incr, 50);
        throttleIncr.get();
        throttleIncr.get();
        Underscore.delay(throttleIncr, 16);
        Underscore.delay(
                (Supplier<Void>)
                        () -> {
                            assertEquals(1, counter[0].intValue(), "incr was throttled");
                            throttleIncr.get();
                            return null;
                        },
                60);
        await().atMost(180, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            throttleIncr.get();
                            return true;
                        });
    }

    /*
    var counter = 0;
    var incr = function(){ counter++; };
    var debouncedIncr = _.debounce(incr, 32);
    debouncedIncr(); debouncedIncr();
    _.delay(debouncedIncr, 16);
    _.delay(function(){ equal(counter, 1, 'incr was debounced'); }, 96);
    */

    @Test
    void debounce() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    counter[0]++;
                    return null;
                };
        Supplier<Void> debouncedIncr = Underscore.debounce(incr, 50);
        debouncedIncr.get();
        debouncedIncr.get();
        Underscore.delay(debouncedIncr, 16);
        Underscore.delay(
                (Supplier<Void>)
                        () -> {
                            assertEquals(1, counter[0].intValue(), "incr was debounced");
                            return null;
                        },
                60);
        await().atMost(120, TimeUnit.MILLISECONDS).until(() -> true);
    }

    /*
    _.defer(function(){ alert('deferred'); });
    // Returns from the function before the alert runs.
    */
    @Test
    void defer() {
        final Integer[] counter = new Integer[] {0};
        Underscore.defer(
                (Supplier<Void>)
                        () -> {
                            try {
                                TimeUnit.MILLISECONDS.sleep(16);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            counter[0]++;
                            return null;
                        });
        assertEquals(0, counter[0].intValue(), "incr was debounced");
        await().atLeast(60, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertEquals(1, counter[0].intValue(), "incr was debounced");
                            return true;
                        });
        Underscore.defer(() -> {
        });
    }

    /*
    var initialize = _.once(createApplication);
    initialize();
    initialize();
    // Application is only created once.
    */
    @Test
    void once() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Integer> incr =
                () -> {
                    counter[0]++;
                    return counter[0];
                };
        final Supplier<Integer> onceIncr = Underscore.once(incr);
        onceIncr.get();
        onceIncr.get();
        await().atLeast(60, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertEquals(1, counter[0].intValue(), "incr was called only once");
                            assertEquals(
                                    1,
                                    onceIncr.get().intValue(),
                                    "stores a memo to the last value");
                            return true;
                        });
    }

    /*
    var hello = function(name) { return "hello: " + name; };
    hello = _.wrap(hello, function(func) {
      return "before, " + func("moe") + ", after";
    });
    hello();
    => 'before, hello: moe, after'
    */
    @Test
    void wrap() {
        Function<String, String> hello = name -> "hello: " + name;
        Function<Void, String> result =
                Underscore.wrap(hello, func -> "before, " + func.apply("moe") + ", after");
        assertEquals("before, hello: moe, after", result.apply(null));
    }

    /*
    var isFalsy = _.negate(Boolean);
    _.find([-2, -1, 0, 1, 2], isFalsy);
    => 0
    */
    @Test
    void negate() {
        Predicate<Integer> isFalsy = Underscore.negate(item -> item != 0);
        Optional<Integer> result = Underscore.find(asList(-2, -1, 0, 1, 2), isFalsy);
        assertEquals(0, result.get().intValue());
    }

    /*
    var greet    = function(name){ return "hi: " + name; };
    var exclaim  = function(statement){ return statement.toUpperCase() + "!"; };
    var welcome = _.compose(greet, exclaim);
    welcome('moe');
    => 'hi: MOE!'
    */
    @Test
    @SuppressWarnings("unchecked")
    void compose() {
        Function<String, String> greet = name -> "hi: " + name;
        Function<String, String> exclaim = statement -> statement.toUpperCase() + "!";
        Function<String, String> welcome = Underscore.compose(greet, exclaim);
        assertEquals("hi: MOE!", welcome.apply("moe"));
    }

    /*
    var renderNotes = _.after(notes.length, render);
    _.each(notes, function(note) {
      note.asyncSave({success: renderNotes});
    });
    // renderNotes is run once, after all notes have saved.
    */
    @Test
    void after() {
        final List<Integer> notes = asList(1, 2, 3);
        final Supplier<Integer> renderNotes = Underscore.after(notes.size(), () -> 4);
        final List<Integer> result = new ArrayList<>();
        Underscore.<Integer>each(
                notes,
                item -> {
                    result.add(item);
                    Integer afterResult = renderNotes.get();
                    if (afterResult != null) {
                        result.add(afterResult);
                    }
                });
        assertEquals("[1, 2, 3, 4]", result.toString());
    }

    /*
    var monthlyMeeting = _.before(3, askForRaise);
    monthlyMeeting();
    monthlyMeeting();
    monthlyMeeting();
    // the result of any subsequent calls is the same as the second call
    */
    @Test
    void before() {
        final List<Integer> notes = asList(1, 2, 3);
        final Supplier<Integer> renderNotes = Underscore.before(notes.size() - 1, () -> 4);
        final List<Integer> result = new ArrayList<>();
        Underscore.<Integer>each(
                notes,
                item -> {
                    result.add(item);
                    Integer afterResult = renderNotes.get();
                    if (afterResult != null) {
                        result.add(afterResult);
                    }
                });
        assertEquals("[1, 4, 2, 4, 3, 4]", result.toString());
    }

    /*
    var stooges = [{name: 'curly', age: 25}, {name: 'moe', age: 21}, {name: 'larry', age: 23}];
    _.map(stooges, _.iteratee('age'));
    => [25, 21, 23]
    */
    @Test
    @SuppressWarnings("unchecked")
    void iteratee() {
        List<Map<String, Object>> stooges =
                Arrays.<Map<String, Object>>asList(
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "curly");
                                put("age", 25);
                            }
                        },
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "moe");
                                put("age", 21);
                            }
                        },
                        new LinkedHashMap<String, Object>() {
                            {
                                put("name", "larry");
                                put("age", 23);
                            }
                        });
        final List<Object> result = Underscore.map(stooges, Underscore.iteratee("age"));
        assertEquals("[25, 21, 23]", result.toString());
    }

    @Test
    void setTimeout() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    counter[0]++;
                    return null;
                };
        Underscore.setTimeout(incr, 0);
        await().atLeast(40, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertEquals(1, counter[0].intValue());
                            return true;
                        });
    }

    @Test
    void clearTimeout() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    counter[0]++;
                    return null;
                };
        java.util.concurrent.ScheduledFuture future = Underscore.setTimeout(incr, 20);
        Underscore.clearTimeout(future);
        Underscore.clearTimeout(null);
        await().atLeast(40, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertEquals(0, counter[0].intValue());
                            return true;
                        });
    }

    @Test
    void setInterval() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    if (counter[0] < 4) {
                        counter[0]++;
                    }
                    return null;
                };
        Underscore.setInterval(incr, 10);
        await().atLeast(45, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertTrue(
                                    asList(0, 4).contains(counter[0]),
                                    "Counter is not in range [0, 4] " + counter[0]);
                            return true;
                        });
    }

    @Test
    void clearInterval() {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr =
                () -> {
                    counter[0]++;
                    return null;
                };
        java.util.concurrent.ScheduledFuture future = Underscore.setInterval(incr, 20);
        Underscore.clearInterval(future);
        Underscore.clearInterval(null);
        await().atLeast(40, TimeUnit.MILLISECONDS)
                .until(
                        () -> {
                            assertEquals(0, counter[0].intValue());
                            return true;
                        });
    }
}
