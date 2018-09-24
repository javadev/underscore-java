/*
 * The MIT License (MIT)
 *
 * Copyright 2015-2018 Valentyn Kolesnikov
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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static java.util.Arrays.asList;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Underscore library unit test.
 *
 * @author Valentyn Kolesnikov
 */
public class FunctionsTest {

/*
var func = function(greeting){ return greeting + ': ' + this.name };
func = _.bind(func, {name: 'moe'}, 'hi');
func();
=> 'hi: moe'
*/
    @Test
    public void bind() {
        class GreetingFunction implements Function<String, String> {
            private final String name;
            public GreetingFunction(final String name) {
                this.name = name;
            }
            public String apply(final String greeting) {
                return greeting + ": " + this.name;
            }
        }
        assertEquals("hi: moe", U.bind(new GreetingFunction("moe")).apply("hi"));
    }

/*
var subtract = function(a, b) { return b - a; };
sub5 = _.partial(subtract, 5);
sub5(20);
=> 15
*/
    @Test
    public void partial() {
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
    public void memoize() {
        class FibonacciFuncion1 extends MemoizeFunction<Integer, Integer> {
            public Integer calc(final Integer n) {
                return n < 2 ? n : apply(n - 1) + apply(n - 2);
            }
        }
        assertEquals(55, new FibonacciFuncion1().apply(10).intValue());
        Function<Integer, Integer> memoizeFunction = U.memoize(
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
    public void throttle() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            counter[0]++; return null; } };
        final Supplier<Void> throttleIncr = U.throttle(incr, 50);
        throttleIncr.get();
        throttleIncr.get();
        U.delay(throttleIncr, 16);
        U.delay(new Supplier<Void>() {
            public Void get() {
                assertEquals("incr was throttled", 1, counter[0].intValue());
                return null;
            }
        }, 60);
        await().atMost(180, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                throttleIncr.get();
                return true;
            }
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
    public void debounce() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            counter[0]++; return null; } };
        Supplier<Void> debouncedIncr = U.debounce(incr, 50);
        debouncedIncr.get();
        debouncedIncr.get();
        U.delay(debouncedIncr, 16);
        U.delay(new Supplier<Void>() {
            public Void get() {
                assertEquals("incr was debounced", 1, counter[0].intValue());
                return null;
            }
        }, 60);
        await().atMost(120, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return true;
            }
        });
    }

/*
_.defer(function(){ alert('deferred'); });
// Returns from the function before the alert runs.
*/
    @Test
    public void defer() throws Exception {
        final Integer[] counter = new Integer[] {0};
        U.defer(new Supplier<Void>() { public Void get() {
            try {
                TimeUnit.MILLISECONDS.sleep(16);
            } catch (Exception e) {
                e.getMessage();
            }
            counter[0]++; return null; } });
        assertEquals("incr was debounced", 0, counter[0].intValue());
        await().atLeast(60, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertEquals("incr was debounced", 1, counter[0].intValue());
                return true;
            }
        });
    }

/*
var initialize = _.once(createApplication);
initialize();
initialize();
// Application is only created once.
*/
    @Test
    public void once() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Integer> incr = new Supplier<Integer>() { public Integer get() {
            counter[0]++; return counter[0]; } };
        final Supplier<Integer> onceIncr = U.once(incr);
        onceIncr.get();
        onceIncr.get();
        await().atLeast(60, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertEquals("incr was called only once", 1, counter[0].intValue());
                assertEquals("stores a memo to the last value", 1, onceIncr.get().intValue());
                return true;
            }
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
    public void wrap() {
        Function<String, String> hello = new Function<String, String>() {
            public String apply(final String name) {
                return "hello: " + name;
            }
        };
        Function<Void, String> result = U.wrap(hello, new Function<Function<String, String>, String>() {
            public String apply(final Function<String, String> func) {
                return "before, " + func.apply("moe") + ", after";
            }
        });
        assertEquals("before, hello: moe, after", result.apply(null));
    }

/*
var isFalsy = _.negate(Boolean);
_.find([-2, -1, 0, 1, 2], isFalsy);
=> 0
*/
    @Test
    public void negate() {
        Predicate<Integer> isFalsy = U.negate(new Predicate<Integer>() {
            public boolean test(final Integer item) {
                return item != 0;
            }
        });
        Optional<Integer> result = U.find(asList(-2, -1, 0, 1, 2), isFalsy);
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
    public void compose() {
        Function<String, String> greet = new Function<String, String>() {
            public String apply(final String name) {
                return "hi: " + name;
            }
        };
        Function<String, String> exclaim = new Function<String, String>() {
            public String apply(final String statement) {
                return statement.toUpperCase() + "!";
            }
        };
        Function<String, String> welcome = U.compose(greet, exclaim);
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
    public void after() {
        final List<Integer> notes = asList(1, 2, 3);
        final Supplier<Integer> renderNotes = U.after(notes.size(),
            new Supplier<Integer>() { public Integer get() {
                return 4; } });
        final List<Integer> result = new ArrayList<Integer>();
        U.<Integer>each(notes, new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.get();
                if (afterResult != null) {
                    result.add(afterResult);
                }
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
    public void before() {
        final List<Integer> notes = asList(1, 2, 3);
        final Supplier<Integer> renderNotes = U.before(notes.size() - 1,
            new Supplier<Integer>() { public Integer get() {
                return 4; } });
        final List<Integer> result = new ArrayList<Integer>();
        U.<Integer>each(notes, new Consumer<Integer>() {
            public void accept(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.get();
                if (afterResult != null) {
                    result.add(afterResult);
                }
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
    public void iteratee() {
        List<Map<String, Object>> stooges = Arrays.<Map<String, Object>>asList(
            new LinkedHashMap<String, Object>() { { put("name", "curly"); put("age", 25); } },
            new LinkedHashMap<String, Object>() { { put("name", "moe"); put("age", 21); } },
            new LinkedHashMap<String, Object>() { { put("name", "larry"); put("age", 23); } }
        );
        final List<Object> result = U.map(stooges, U.iteratee("age"));
        assertEquals("[25, 21, 23]", result.toString());
    }

    @Test
    public void setTimeout() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            counter[0]++; return null; } };
        U.setTimeout(incr, 0);
        await().atLeast(40, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertEquals(1, counter[0].intValue());
                return true;
            }
        });
    }

    @Test
    public void clearTimeout() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            counter[0]++; return null; } };
        java.util.concurrent.ScheduledFuture future = U.setTimeout(incr, 20);
        U.clearTimeout(future);
        U.clearTimeout(null);
        await().atLeast(40, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertEquals(0, counter[0].intValue());
                return true;
            }
        });
    }

    @Test
    public void setInterval() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            if (counter[0] < 4) {
                counter[0]++;
            }
            return null; } };
        U.setInterval(incr, 10);
        await().atLeast(45, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertTrue("Counter is not in range [0, 4] " + counter[0],
                    asList(0, 4).contains(counter[0]));
                return true;
            }
        });
    }

    @Test
    public void clearInterval() throws Exception {
        final Integer[] counter = new Integer[] {0};
        Supplier<Void> incr = new Supplier<Void>() { public Void get() {
            counter[0]++; return null; } };
        java.util.concurrent.ScheduledFuture future = U.setInterval(incr, 20);
        U.clearInterval(future);
        U.clearInterval(null);
        await().atLeast(40, TimeUnit.MILLISECONDS).until(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                assertEquals(0, counter[0].intValue());
                return true;
            }
        });
    }
}
