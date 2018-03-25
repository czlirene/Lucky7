package test.NestedProperTests;

import java.util.HashMap;

// test.NestedProperTests.Foo1
// dec count : 1

// java.lang.Object
// ref count: 2

// java.lang.String
// ref count : 1

// java.lang.String[]
// ref count : 1

// java.lang.Integer
// ref count : 1

// java.lang.Integer[]
// ref count : 1

// java.util.HashMap
// ref count : 2


public class Foo1 {
    public Object String(Object... args){ // method name should not count
        return new HashMap<String[], Integer[]>();
    }
}