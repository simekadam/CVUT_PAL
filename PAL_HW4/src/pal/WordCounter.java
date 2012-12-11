package pal;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: simekadam
 * Date: 12/10/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordCounter {

    public int length;
    public int wordCount;
    public int depth;

    public WordCounter(int length) {
        this.wordCount = 0;
        this.length = length;
        this.depth = 0;
    }

    public void getWordCount(LinkedList<State> states, int depth) {
        if(states != null){


        for (State s : states) {
//            System.out.println("test");
            if (depth == length-1) {
                if(s.finish){
                    wordCount++;
                }
                return;

            } else {
                for (char key : s.transitions.keySet()) {
                       State next = s.transitions.get(key).getFirst();
                       getWordCount(next.transitions.get(key), depth + 1);
                }
            }
        }
        }


    }



}

