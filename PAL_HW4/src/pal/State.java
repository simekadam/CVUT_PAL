package pal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: simekadam
 * Date: 12/9/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class State {

    public int id;
    public String longid;
    public boolean start, finish;
    public HashMap<Character, LinkedList<State>> transitions;
    public Set<State> states;
    public boolean marked;


    public int oldTripCount;
    public int newTripCount;
    public int n;



    public State(int id)
    {
       transitions = new HashMap<Character, LinkedList<State>>();
       this.id = id;
       this.start = this.finish = false;
       this.marked = false;
    }
    public State(String id)
    {
        transitions = new HashMap<Character, LinkedList<State>>();
        this.longid = id;
        this.start = this.finish = false;
        this.marked = false;
    }

    public State(int id, HashSet<State> states)
    {
        transitions = new HashMap<Character, LinkedList<State>>();
        this.id = id;
        this.states = states;
        this.start = this.finish = false;
        this.marked = false;
    }

    public boolean addTransition(char inputChar, State finalState)
    {
        if(this.transitions.containsKey(inputChar))
        {
            this.transitions.get(inputChar).add(finalState);
        }
        else
        {
            LinkedList<State> transitions = new LinkedList<State>();
            transitions.add(finalState);
            this.transitions.put(inputChar, transitions);
        }

        this.finish = false;
        finalState.start = false;
        return true;
    }

    public boolean addTransitionWithoutSettingFinish(char inputChar, State finalState)
    {
        if(this.transitions.containsKey(inputChar))
        {
            this.transitions.get(inputChar).add(finalState);
        }
        else
        {
            LinkedList<State> transitions = new LinkedList<State>();
            transitions.add(finalState);
            this.transitions.put(inputChar, transitions);
        }


        return true;
    }
}
