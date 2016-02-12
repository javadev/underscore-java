  function equal(actual, expected, description) {
    it(description, function() {
      expect(actual).toBe(expected);
    });
  }
  function deepEqual(actual, expected, description) {
    it(description, function() {
      expect(actual).toEqual(expected);
    });
  }
  function strictEqual(actual, expected, description) {
    it(description, function() {
      expect(actual).toBe(expected);
    });
  }  
  function ok(actual, description) {
    expect(actual).toBe(true);
  }  
  describe('bind', function() {
    var context = {name : 'moe'};
    var func = function(arg) { return 'name: ' + (this.name || arg); };
    var bound = _.bind(func, context);
    equal(bound(), 'name: moe', 'can bind a function to a context');

    bound = _(func).bind(context);
    equal(bound(), 'name: moe', 'can do OO-style binding');

    bound = _.bind(func, null, 'curly');
    equal(bound(), 'name: curly', 'can bind without specifying a context');

    func = function(salutation, name) { return salutation + ': ' + name; };
    func = _.bind(func, this, 'hello');
    equal(func('moe'), 'hello: moe', 'the function was partially applied in advance');

    func = _.bind(func, this, 'curly');
    equal(func(), 'hello: curly', 'the function was completely applied in advance');

    func = function(salutation, firstname, lastname) { return salutation + ': ' + firstname + ' ' + lastname; };
    func = _.bind(func, this, 'hello', 'moe', 'curly');
    equal(func(), 'hello: moe curly', 'the function was partially applied in advance and can accept multiple arguments');

    func = function(context, message) { equal(this, context, message); };
//    _.bind(func, 0, 0, 'can bind a function to `0`')();
//    _.bind(func, '', '', 'can bind a function to an empty string')();
//    _.bind(func, false, false, 'can bind a function to `false`')();

    // These tests are only meaningful when using a browser without a native bind function
    // To test this with a modern browser, set underscore's nativeBind to undefined
    var F = function () { return this; };
    var Boundf = _.bind(F, {hello: 'moe curly'});
    var newBoundf = new Boundf();
    equal(newBoundf.hello, undefined, 'function should not be bound to the context, to comply with ECMAScript 5');
    equal(Boundf().hello, 'moe curly', "When called without the new operator, it's OK to be bound to the context");
    ok(newBoundf instanceof F, 'a bound instance is an instance of the original function');
  });

  describe('debounce', function() {
    var counter = 0;
    var incr = function(){ counter++; };
    var debouncedIncr = _.debounce(incr, 32);
    debouncedIncr(); debouncedIncr();
    _.delay(debouncedIncr, 16);
    _.delay(function(){ equal(counter, 1, 'incr was debounced'); }, 96);
  });

  describe('once', function() {
    var num = 0;
    var increment = _.once(function(){ return ++num; });
    increment();
    increment();
    equal(num, 1);

    equal(increment(), 1, 'stores a memo to the last value');
  });
