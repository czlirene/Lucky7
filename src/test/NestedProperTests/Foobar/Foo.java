package test.NestedProperTests.Foobar;

// test.NestedProperTests.Foobar.Foo 
// dec count = 1
// ref count = 0

// test.NestedProperTests.Foobar.Foo.FUBAR
// dec count = 1
// ref count = 0

// double
// dec count = 0
// ref count = 1

// java.lang.Thread
// dec count = 0
// ref count = 1

public interface Foo{
    double chips(Thread t);

    enum FUBAR {SNAFU};
}