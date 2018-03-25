package test.NestedProperTests.Foobar;


// test.NestedProperTests.Foobar.Foo
// Ref count = 3

// test.NestedProperTests.Foobar.Bar
// Dec count = 1
// ref count = 1

// test.NestedFooTests.Foobar.Boo
// ref count = 1

// test.NestedFooTests.Foobar.Foo.FUBAR
// ref count = 2


public class Bar implements Foo{

    @Override
    public double fries(Thread t){
        return (Double) 3.2;
    }
}