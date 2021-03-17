package tests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmifed
 */
public class IncreaseListSizeInLoopTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        int size = list.size();
        for(int i = 0; i < size; i++){
            list.add(i+2);
            ++size;
            if(size > 10){
                System.out.println(list);
                break;
            }
        }
    }
}
