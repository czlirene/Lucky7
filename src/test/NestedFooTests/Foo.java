package test.NestedFooTests;

import test.NestedFooTests.Foobar.Bar;

// test.NestedFooTests.Foo
//  dec count: 1
//  ref count: 1

// test.NestedFooTests.Foobar.Bar
// ref count: 2

class Foo {
    public Foo(){
        // default constructor
    }
    public Bar foo(){
        return null;
    }
}