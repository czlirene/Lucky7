package test.NestedFooTests;

// test.NestedFooTests.Foo1
// dec count : 1

// test.NestedFooTests.Foo
// ref count: 3

class Foo1 {
    Foo f; 

    public void test(){
        class Foo{} // local class, only use simple name Foo
    }

    public Foo hello(){
        return new Foo();
    }
}