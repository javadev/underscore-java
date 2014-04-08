/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Valentyn Kolesnikov
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
describe("underscore tests",function(){
/*
_.each([1, 2, 3], alert);
=> alerts each number in turn...
*/
    it("each",function(){
        var result = [];
        _.each([1, 2, 3], function(item) {result.push(item)});
        expect(result).toEqual([1, 2, 3]);
    });
/*
_.each({one: 1, two: 2, three: 3}, alert);
=> alerts each number value in turn...
*/
    it("eachMap",function(){
        var result = [];
        _.each({one: 1, two: 2, three: 3}, function(item) {result.push(item)});
        expect(result).toEqual([1, 2, 3]);
    });
/*
_.map([1, 2, 3], function(num){ return num * 3; });
=> [3, 6, 9]
*/
    it("map",function(){
        var result = _.map([1, 2, 3], function(num){ return num * 3; });
        expect(result).toEqual([3, 6, 9]);
    });
/*
_.map({one: 1, two: 2, three: 3}, function(num, key){ return num * 3; });
=> [3, 6, 9]
*/
    it("mapMap",function(){
        var result = _.map({one: 1, two: 2, three: 3}, function(num){ return num * 3; });
        expect(result).toEqual([3, 6, 9]);
    });
/*
var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
=> 6
*/
    it("reduce",function(){
        var sum = _.reduce([1, 2, 3], function(memo, num){ return memo + num; }, 0);
        expect(sum).toEqual(6);
    });
/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.reduceRight(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    it("reduceRight",function(){
        var list = [[0, 1], [2, 3], [4, 5]];
        var flat = _.reduceRight(list, function(a, b) { return a.concat(b); }, []);
        expect(flat).toEqual([4, 5, 2, 3, 0, 1]);
    });
/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldl(list, function(a, b) { return a.concat(b); }, []);
=> [0, 1, 2, 3, 4, 5]
*/
    it("foldl",function(){
        var list = [[0, 1], [2, 3], [4, 5]];
        var flat = _.foldl(list, function(a, b) { return a.concat(b); }, []);
        expect(flat).toEqual([0, 1, 2, 3, 4, 5]);
    });
/*
var list = [[0, 1], [2, 3], [4, 5]];
var flat = _.foldr(list, function(a, b) { return a.concat(b); }, []);
=> [4, 5, 2, 3, 0, 1]
*/
    it("foldr",function(){
        var list = [[0, 1], [2, 3], [4, 5]];
        var flat = _.foldr(list, function(a, b) { return a.concat(b); }, []);
        expect(flat).toEqual([4, 5, 2, 3, 0, 1]);
    });
/*
_.contains([1, 2, 3], 3);
=> true
*/
    it("contains",function(){
        expect(_.contains([1, 2, 3], 3)).toBe(true);
    });
/*
var even = _.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> 2
*/
    it("find",function(){
        expect(_.find([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; })).toBe(2);
    });
/*
var evens = _.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; });
=> [2, 4, 6]
*/
    it("filter",function(){
        expect(_.filter([1, 2, 3, 4, 5, 6], function(num){ return num % 2 == 0; })).toEqual([2, 4, 6]);
    });
/*
_.first([5, 4, 3, 2, 1]);
=> 5
*/
    it("first",function(){
        expect(_.first([5, 4, 3, 2, 1])).toBe(5);
    });
/*
_.initial([5, 4, 3, 2, 1]);
=> [5, 4, 3, 2]
*/
    it("initial",function(){
        expect(_.initial([5, 4, 3, 2, 1])).toEqual([5, 4, 3, 2]);
    });
/*
_.last([5, 4, 3, 2, 1]);
=> 1
*/
    it("last",function(){
        expect(_.last([5, 4, 3, 2, 1])).toBe(1);
    });
/*
_.flatten([1, [2], [3, [[4]]]]);
=> [1, 2, 3, 4];
*/
    it("flatten",function(){
        expect(_.flatten([1, [2], [3, [[4]]]])).toEqual([1, 2, 3, 4]);
    });
/*
_.compact([0, 1, false, 2, '', 3]);
=> [1, 2, 3]
*/
/*
_.without([1, 2, 1, 0, 3, 1, 4], 0, 1);
=> [2, 3, 4]
*/
/*
var numbers = [10, 5, 100, 2, 1000];
_.max(numbers);
=> 1000
*/
/*
var numbers = [10, 5, 100, 2, 1000];
_.min(numbers);
=> 2
*/
/*
_.shuffle([1, 2, 3, 4, 5, 6]);
=> [4, 1, 6, 3, 5, 2]
*/
/*
_.sample([1, 2, 3, 4, 5, 6]);
=> 4

_.sample([1, 2, 3, 4, 5, 6], 3);
=> [1, 6, 2]
*/
/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.pluck(stooges, 'name');
=> ["moe", "larry", "curly"]
*/
/*
_.sortBy([1, 2, 3, 4, 5, 6], function(num){ return Math.sin(num); });
=> [5, 4, 6, 3, 1, 2]
*/
/*
_.groupBy([1.3, 2.1, 2.4], function(num){ return Math.floor(num); });
=> {1: [1.3], 2: [2.1, 2.4]}
*/
/*
var stooges = [{name: 'moe', age: 40}, {name: 'larry', age: 50}, {name: 'curly', age: 60}];
_.indexBy(stooges, 'age');
=> {
  "40": {name: 'moe', age: 40},
  "50": {name: 'larry', age: 50},
  "60": {name: 'curly', age: 60}
}
*/
/*
(function(){ return _.toArray(arguments).slice(1); })(1, 2, 3, 4);
=> [2, 3, 4]
*/
/*
_.size({one: 1, two: 2, three: 3});
=> 3
*/
/*
_.union([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2, 3, 101, 10]
*/
/*
_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
=> [1, 2]
*/
/*
_.difference([1, 2, 3, 4, 5], [5, 2, 10]);
=> [1, 3, 4]
*/
/*
_.uniq([1, 2, 1, 3, 1, 4]);
=> [1, 2, 3, 4]
*/
/*
_.zip(['moe', 'larry', 'curly'], [30, 40, 50], [true, false, false]);
=> [["moe", 30, true], ["larry", 40, false], ["curly", 50, false]]
*/
/*
_.object(['moe', 'larry', 'curly'], [30, 40, 50]);
=> {moe: 30, larry: 40, curly: 50}
*/
/*
_.indexOf([1, 2, 3], 2);
=> 1
*/
/*
_.lastIndexOf([1, 2, 3, 1, 2, 3], 2);
=> 4
*/
/*
_.sortedIndex([10, 20, 30, 40, 50], 35);
=> 3
*/
/*
_.range(10);
=> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
_.range(1, 11);
=> [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
_.range(0, 30, 5);
=> [0, 5, 10, 15, 20, 25]
_.range(0, -10, -1);
=> [0, -1, -2, -3, -4, -5, -6, -7, -8, -9]
_.range(0);
=> []
*/
/*
var stooges = [{name: 'curly', age: 25}, {name: 'moe', age: 21}, {name: 'larry', age: 23}];
var youngest = _.chain(stooges)
  .sortBy(function(stooge){ return stooge.age; })
  .map(function(stooge){ return stooge.name + ' is ' + stooge.age; })
  .first()
  .value();
=> "moe is 21"
*/
});
