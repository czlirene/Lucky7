package test.NestedProperTests;

import java.util.List;
import java.util.ArrayList;

// test.NestedProperTests.Foo
//  dec count: 1

// java.lang.String
// ref count: 4

// java.util.List
// ref count: 2

// java.util.ArrayList
// ref count: 4

// int
// ref count : 1

public class Foo {
    int primType;
    List<ArrayList<String>> nestedParameters;

    public Foo(){
        nestedParameters = new ArrayList<ArrayList<String>>();
    }

    public String foo(){
        return new String("what");
    }
}