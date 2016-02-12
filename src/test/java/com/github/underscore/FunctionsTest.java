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
        class GreetingFunction implements Function1<String, String> {
            private final String name;
            public GreetingFunction(final String name) {
                this.name = name;
            }
            public String apply(final String greeting) {
                return greeting + ": " + this.name;
            }
        }
        assertEquals("hi: moe", $.bind(new GreetingFunction("moe")).apply("hi"));
    }

/*
var subtract = function(a, b) { return b - a; };
sub5 = _.partial(subtract, 5);
sub5(20);
=> 15
*/
    @Test
    public void partial() {
        class SubtractFunction implements Function1<Integer, Integer> {
            private final Integer arg1;
            public SubtractFunction(final Integer arg1) {
                this.arg1 = arg1;
            }
            public Integer apply(final Integer arg2) {
                return arg2 - arg1;
            }
        }
        Function1<Integer, Integer> sub5 = new SubtractFunction(5);
        assertEquals(15, sub5.apply(20).intValue());
    }

/*
var fibonacci = _.memoize(function(n) {
  return n < 2 ? n: fibonacci(n - 1) + fibonacci(n - 2);
});
*/
    @Test
    public void memoize() {
        class FibonacciFuncion1 extends MemoizeFunction1<Integer> {
            public Integer calc(final Integer n) {
                return n < 2 ? n : apply(n - 1) + apply(n - 2);
            }
        }
        assertEquals(55, new FibonacciFuncion1().apply(10).intValue());
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
        Function<Void> incr = new Function<Void>() { public Void apply() {
            counter[0]++; return null; } };
        Function<Void> debouncedIncr = $.debounce(incr, 50);
        debouncedIncr.apply();
        debouncedIncr.apply();
        $.delay(debouncedIncr, 16);
        $.delay(new Function<Void>() {
            public Void apply() {
                assertEquals("incr was debounced", 1, counter[0].intValue());
                return null;
            }
        }, 60);
        Thread.sleep(120);
    }

/*
_.defer(function(){ alert('deferred'); });
// Returns from the function before the alert runs.
*/
    @Test
    public void defer() throws Exception {
        final Integer[] counter = new Integer[] {0};
        $.defer(new Function<Void>() { public Void apply() {
            try {
                Thread.sleep(16);
            } catch (Exception e) {
                e.getMessage();
            }
            counter[0]++; return null; } });
        assertEquals("incr was debounced", 0, counter[0].intValue());
        Thread.sleep(60);
        assertEquals("incr was debounced", 1, counter[0].intValue());
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
        Function<Integer> incr = new Function<Integer>() { public Integer apply() {
            counter[0]++; return counter[0]; } };
        Function<Integer> onceIncr = $.once(incr);
        onceIncr.apply();
        onceIncr.apply();
        Thread.sleep(60);
        assertEquals("incr was called only once", 1, counter[0].intValue());
        assertEquals("stores a memo to the last value", 1, onceIncr.apply().intValue());
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
        Function1<String, String> hello = new Function1<String, String>() {
            public String apply(final String name) {
                return "hello: " + name;
            }
        };
        Function1<Void, String> result = $.wrap(hello, new Function1<Function1<String, String>, String>() {
            public String apply(final Function1<String, String> func) {
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
        Predicate<Integer> isFalsy = $.negate(new Predicate<Integer>() {
            public Boolean apply(final Integer item) {
                return item != 0;
            }
        });
        Optional<Integer> result = $.find(asList(-2, -1, 0, 1, 2), isFalsy);
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
        Function1<String, String> greet = new Function1<String, String>() {
            public String apply(final String name) {
                return "hi: " + name;
            }
        };
        Function1<String, String> exclaim = new Function1<String, String>() {
            public String apply(final String statement) {
                return statement.toUpperCase() + "!";
            }
        };
        Function1<String, String> welcome = $.compose(greet, exclaim);
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
        final Function<Integer> renderNotes = $.after(notes.size(),
            new Function<Integer>() { public Integer apply() {
                return 4; } });
        final List<Integer> result = new ArrayList<Integer>();
        $.<Integer>each(notes, new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.apply();
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
        final Function<Integer> renderNotes = $.before(notes.size() - 1,
            new Function<Integer>() { public Integer apply() {
                return 4; } });
        final List<Integer> result = new ArrayList<Integer>();
        $.<Integer>each(notes, new Block<Integer>() {
            public void apply(Integer item) {
                result.add(item);
                Integer afterResult = renderNotes.apply();
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
        final List<Object> result = $.map(stooges, $.iteratee("age"));
        assertEquals("[25, 21, 23]", result.toString());
    }
}
