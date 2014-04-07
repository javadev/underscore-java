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
});
