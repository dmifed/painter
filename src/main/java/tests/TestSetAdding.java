package tests;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dmifed
 */
public class TestSetAdding {
    private static int id;

    public static void main(String[] args) {
        TestSetAdding testSetAdding = new TestSetAdding();
        testSetAdding.increase();
        testSetAdding.increase();
        testSetAdding.increase();
    }




    private void increase(){
        Set<Integer> set = new HashSet<>();
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        addInt(set);
        System.out.println(set);
    }

    private int addInt(Set<Integer> set){
        set.add(id%5);
        return ++id;
    }
}
