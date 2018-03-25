package test.NestedFooTests.Foobar;

import test.NestedFooTests.Foobar.Bar;

// test.NestedFooTests.Foobar.Foo 
// dec count = 1
// ref count = 0

// test.NestedFooTests.Foobar.Bar
// ref count = 2

// test.NestedFooTests.Foobar.Boo
// ref count = 1

// test.NestedFooTests.Foobar.Foo.FUBAR
// dec count = 1
// ref count = 1

class Foo{
    
    @interface FUBAR{
    }

    public Bar wowChicken(FUBAR x){
        return new Bar(new Boo(this));
    }
}