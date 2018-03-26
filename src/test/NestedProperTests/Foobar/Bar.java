package test.NestedProperTests.Foobar;

import java.util.List;
import java.util.LinkedList;

// test.NestedProperTests.Foobar.Bar
// Dec count = 1
// ref count = 0

// test.NestedProperTests.Foobar.Foo
// Dec count = 0
// Ref count = 1

// java.lang.Override
// Dec = 0
// ref = 1

// double
// d = 0
// ref = 1

// java.lang.Thread
// d = 0
// ref = 2

// java.lang.Double
// d = 0
// ref = 1

// test.NestedProperTests.Foobar.Bar.Fish
// d = 1
// ref = 3

// boolean
// d = 0
// ref = 1

// java.util.LinkedList
// d = 0
// ref = 2

// java.util.List
// d = 0
// ref = 1

// java.lang.String[]
// d = 0
// ref = 1

// java.lang.String
// d = 0
// ref = 1

// test.NestedProperTests.Foobar.Bar.LambdaMe
// d = 1
// ref = 1

// float
// d = 0
// ref = 5

public class Bar implements Foo{
    public static Thread tField;

    @Override
    public double chips(Thread t){
        return (Double) 3.2;
    }

    protected class Fish {
        public test.NestedProperTests.Foobar.Bar.Fish food(){
            return new test.NestedProperTests.Foobar.Bar.Fish();
        }

        public boolean instanceOf(){
            LinkedList<Fish> c = new LinkedList<>();
            return (c instanceof List);
        }
    }

    public void main(String args[]){
        LambdaMe add = (float a, float b) -> a + b;
    }

    interface LambdaMe {
        float op(float a, float b);
    }
}