package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Main
{
    public static String firstRegExString, secondRegExString, alphabet;
    public static Regex firstRegex, secondRegex;
    public static int length;
    static StateMachine machine1;
    static StateMachine machine2;


    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            alphabet =  reader.readLine();
            firstRegExString = reader.readLine();
            secondRegExString = reader.readLine();
            length = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        firstRegex = new Regex(firstRegExString, alphabet);
        secondRegex = new Regex(Regex.encapsulate(secondRegExString), alphabet);
//        System.out.println(firstRegex.regex);
//        System.out.println(secondRegex.regex);

        machine1 = new StateMachine(alphabet);
        machine2 = new StateMachine(alphabet);
        machine1.DFA = machine1.convertNFAtoDFA(machine1.createTheNondeterministicFiniteAutomat(firstRegex));
        machine2.DFA = machine2.convertNFAtoDFA(machine2.createTheNondeterministicFiniteAutomat(secondRegex));

//        System.out.println(machine1.getIntersection(machine2));

        LinkedList<State> intersection = machine1.getIntersection(machine2);
        WordCounter wc = new WordCounter(length);
        LinkedList<State> start = new LinkedList<State>();
//        start.add(machine1.intersectfirststate);
        System.out.println(machine1.inteesectEnds.size());

//        wc.getWordCount(start, 0);
//        System.out.println(wc.wordCount);
        getWordCount();


    }

    public static void getWordCount() {

        int result = 0;


        Queue<State> q = new LinkedList<State>();
        q.add(machine1.intersectfirststate.getFirst());
        for(State s : q)
        {
            s.oldTripCount = 1;
           s.newTripCount = 1;
        }

        int removed;
        int addedForN = 1;
        int added = 0;
        for (int i = 0; i < length-1; i++) {

            removed = 0;
            while (addedForN != removed) {

                State s = q.remove();

                removed++;
                for (char c : s.transitions.keySet()) {
                    State state = s.transitions.get(c).getFirst();
                    if (state.n != i + 1) {
                        state.n = i + 1;
                        q.add(state);
                        added++;

                        if(s.n == state.n){

                            state.oldTripCount = state.newTripCount;
                            state.newTripCount = s.oldTripCount;
                        }
                        else{

                            state.oldTripCount = state.newTripCount;
                            state.newTripCount = s.newTripCount;
                        }
                    } else {

                        if(s.n == state.n){
                            state.newTripCount += s.oldTripCount;
                        }
                        else{
                            state.newTripCount += s.newTripCount;
                        }
                    }
                }

            }

            addedForN = added;
            added = 0;
        }

        for (int i = 0; i < machine1.inteesectEnds.size(); i++) {
            if (machine1.inteesectEnds.get(i).n == length-1) {
                result += machine1.inteesectEnds.get(i).newTripCount;
            }
        }
        System.out.println(result);
    }




}