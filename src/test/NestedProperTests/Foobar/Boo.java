package test.NestedProperTests.Foobar;

import java.util.ArrayList;
import test.NestedProperTests.Foobar.Bar;

// test.NestedProperTests.Foobar.Boo
// dec count = 1
// ref count = 3

// java.lang.Thread
// dec count = 0
// ref count = 4

// test.NestedProperTests.Foobar.Bar
// dec count = 0
// ref count = 5

// java.util.ArrayList
// dec count = 0
// ref count = 2

// java.lang.System
// dec count = 0
// ref count = 1

// java.lang.Deprecated
// dec count = 0
// ref count = 1

// test.NestedProperTests.Foobar.Foo
// dec count = 0
// ref count = 1

// localMe
// dec count = 1
// ref count = 0

public class Boo {

    public void retrievingSomeStaticField(){
        Thread c = Bar.tField;
        Bar newBar = new Bar();

        for (Thread a : new ArrayList<Thread>()){
            System.out.println(newBar.chips(new Thread(c)));
        }
    }

    @Deprecated
    public test.NestedProperTests.Foobar.Foo returnInterface(Boo beebo){
        final class localMe {
            public void switcharoo(){
                switch("hello"){
                    case "hi":
                        break;
                    default:
                        Boo beedo = new Boo();
                }
            }
        }
        return new test.NestedProperTests.Foobar.Bar();
    }
}

