package pal;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: simekadam
 * Date: 12/9/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateMachine {


    public Stack<Character> operatorStack;
    public Stack<LinkedList<State>> operandStack;
    public LinkedList<State> NFA;
    public LinkedList<State> DFA;
    public String alphabet;
    public int stateId = 0;
    public int dfaStateId = 0;
    public ArrayList<Character> operators;
    public LinkedList<State> intersectfirststate;
    public LinkedList<State> inteesectEnds;
    public int epsilons = 0;
    public int NFAfinish;
    public int NFAstart;


    public StateMachine(String alphabet)
    {
        this.alphabet = alphabet;
        this.operandStack = new Stack<LinkedList<State>>();
        this.operatorStack = new Stack<Character>();
        this.operators = new ArrayList<Character>();
        operators.add('*');
        operators.add('(');
        operators.add(')');
        operators.add('|');
        operators.add((char)7);

    }


    public void push(char input)
    {
        State s1 = new State(stateId++);
        State s2 = new State(stateId++);
        s1.addTransition(input, s2);
        LinkedList<State> nfa = new LinkedList<State>();
        nfa.add(s1);
        nfa.add(s2);
        this.operandStack.push(nfa);
    }

    public LinkedList<State> pop()
    {
//        System.out.println("pop "+this.operandStack.lastElement());
        return this.operandStack.isEmpty() ? null : this.operandStack.pop();
    }

    public boolean concatenate()
    {
        LinkedList<State> nfa1, nfa2;
        if(this.operandStack.size() < 2){
            return false;
        }
        nfa2 = this.pop();
        nfa1 = this.pop();
        nfa1.getLast().addTransition('\u0000', nfa2.getFirst());
        epsilons++;
        nfa1.addAll(nfa2);
        this.operandStack.push(nfa1);
        return true;
    }

    public boolean star()
    {
        if(this.operandStack.size() < 1){
            return false;
        }
        LinkedList<State> nfa = this.pop();
        State s1 = new State(stateId++);
        State s2 = new State(stateId++);

        s1.addTransition('\u0000', s2);
        epsilons++;
        epsilons++;
        epsilons++;
        epsilons++;
        s1.addTransition('\u0000', nfa.getFirst());
        nfa.getLast().addTransition('\u0000', s2);
        s2.addTransition('\u0000', s1);

        nfa.addFirst(s1);
        nfa.addLast(s2);

        this.operandStack.push(nfa);
        return true;
    }

    public boolean union()
    {
        if(this.operandStack.size() < 2){
            return false;
        }
        LinkedList<State> nfa1, nfa2;
        nfa2 = this.pop();
        nfa1 = this.pop();

        State s1 = new State(stateId++);
        State s2 = new State(stateId++);

        s1.addTransition('\u0000', nfa1.getFirst());
        s1.addTransition('\u0000', nfa2.getFirst());
        nfa1.getLast().addTransition('\u0000', s2);
        nfa2.getLast().addTransition('\u0000', s2);
        epsilons++;
        epsilons++;
        epsilons++;
        epsilons++;
        nfa1.addFirst(s1);
        nfa2.addLast(s2);

        nfa1.addAll(nfa2);
        this.operandStack.push(nfa1);


        return true;
    }




    private boolean isOperator(char input)
    {
       return operators.contains(input);
    }

    private boolean operatorPresedence(char left, char right)
    {
        if(left == right) return true;
        if(left == '*') return false;
        if(right == '*') return true;
        if(left == 7) return false;
        if(right == 7) return true;
        if(left == '|') return false;
        return true;
    }

    private boolean applyOperator()
    {
       if(this.operatorStack.size() == 0) return false;
       char operator = this.operatorStack.pop();

       switch (operator)
       {
           case '*':
               return this.star();
           case '|':
               return this.union();
           case 7:
               return this.concatenate();
           default:
               System.out.println("A kurva mam bordel v operatorech");
               return false;
       }
    }

    public LinkedList<State> createTheNondeterministicFiniteAutomat(Regex regularExpression)
    {

       for (char c : regularExpression.regex.toCharArray())
       {
           if(!isOperator(c))
           {
               this.push(c);
//               System.out.println("pushing " + c);
           }
           else if(this.operatorStack.isEmpty() || c == '(')
           {
               this.operatorStack.push(c);
           }
           else if(c == ')')
           {
               while (this.operatorStack.lastElement() != '(')
               {
                    if(!applyOperator())
                    {
                        return null;
                    }
               }
               this.operatorStack.pop();
           }
           else
           {
               while (!this.operatorStack.isEmpty() && operatorPresedence(c, this.operatorStack.lastElement()))
               {
                   if(!applyOperator()){
                       return null;
                   }
               }
               this.operatorStack.push(c);
           }
       }
       while (!this.operatorStack.isEmpty())
       {
           if(this.operatorStack.lastElement() == '(') {
               this.operatorStack.pop();
               continue;
           }
           if(!applyOperator()){
               return null;
           }
       }
       LinkedList<State> nfa = this.pop();
       if(nfa == null){
           return null;
       }
       NFAfinish = nfa.getLast().id;
        NFAstart = nfa.getFirst().id;
        this.NFA = nfa;
       return nfa;
    }


    public LinkedList<State> convertNFAtoDFA(LinkedList<State> nfa)
    {
        LinkedList<State> dfa = new LinkedList<State>();
        Stack<State> unmarked = new Stack<State>();
//        dfa.add(nfa.getFirst());
        LinkedList<State> nfaFirst = new LinkedList<State>();
        nfaFirst.add(nfa.getFirst());

        State s = new State(dfaStateId++, new HashSet<State>(epsilonClosure(nfaFirst)));
        s.finish = false;
        s.start = false;
        for(State ss : s.states)
        {
            if(ss.id == NFAfinish) s.finish = true;
            if(ss.id == NFAstart) {
                s.start = true;
            }
        }
        dfa.add(s);
        unmarked.push(s);
        while (!unmarked.isEmpty())
        {

            State state = unmarked.pop();
            for(char c : alphabet.toCharArray())
            {
                boolean addNew = true;
                LinkedList<State> U = epsilonClosure(move(c, state.states));
                for(State sub : dfa)
                {
                    if(sub.states.equals(new HashSet<State>(U)))
                    {
                       state.addTransitionWithoutSettingFinish(c, sub);
                       addNew = false;
                       break;
                    }
                }
                if(addNew)
                {
                    State newState = new State(dfaStateId++, new HashSet<State>(U));
//                    boolean finish = state.finish;
                    newState.finish = false;
                    newState.start = false;
                    for(State ss : newState.states)
                    {
                        if(ss.id == NFAfinish) {
                            newState.finish = true;
                        }
                        if(ss.id == NFAstart) {
                            newState.start = true;
                        }
                    }

                    state.addTransitionWithoutSettingFinish(c, newState);

                    dfa.add(newState);
                    unmarked.push(newState);
                }
            }
        }
        this.DFA = dfa;
        for(State state: dfa)
        {
          if(state.id == NFAfinish) state.finish = true;
            if(state.id == NFAstart) {
                state.start = true;
            }
        }
        return dfa;
    }

    public LinkedList<State> epsilonClosure(LinkedList<State> states)
    {
        LinkedList<State> result = new LinkedList<State>();
        Stack<State> statesStack = new Stack<State>();
        if(states.size() == 0) return result;
        statesStack.addAll(states);
        result.addAll(states);
//        System.out.println(statesStack.size());
        State state;
        while (!statesStack.isEmpty())
        {
            state = statesStack.pop();

            if(state.transitions.containsKey('\u0000'))
            {
                for (State subState : state.transitions.get('\u0000'))
                {
                    if(!result.contains(subState))
                    {
                        result.add(subState);
                        statesStack.push(subState);
                    }
                }
            }

        }

        return result;
    }

    private LinkedList<State> move(char c, Set<State> states)
    {
        LinkedList<State> result = new LinkedList<State>();
        for(State s : states)
        {


//            if(s.transitions == null) continue;
            if(s.transitions.containsKey(c))
            {
                LinkedList<State> tmp = s.transitions.get(c);
                for(State resultState : tmp)
                {
                    result.add(resultState);
                }
            }
        }
        return result;
    }

    public LinkedList<State> getIntersection(StateMachine machine2)
    {
        intersectfirststate = new LinkedList<State>();
        int test = 0;
        HashMap<String, State> tmpResults = new HashMap<String, State>();
        LinkedList<State> result = new LinkedList<State>();
        inteesectEnds = new LinkedList<State>();
        for(State s1 : this.DFA)
        {
            for(State s2 : machine2.DFA)
            {
                State intersectX;

                String id =  (""+s1.id+"-"+s2.id);
                if(tmpResults.containsKey(id))
                {
                    intersectX = tmpResults.get(id);
                }
                else
                {
                    intersectX= new State(id);
                    intersectX.finish = false;
                    intersectX.start = false;
                    tmpResults.put(intersectX.longid, intersectX);
                }


                if(s1.start && s2.start)
                {
                    System.out.println("prdel");
                   intersectfirststate.add(intersectX);
                }
                if (s1.finish && s2.finish) {
                    intersectX.finish = true;
                    this.inteesectEnds.add(intersectX);
                }
//                if(intersectX.start)
//                {
//                    intersectfirststate = intersectX;
//                }

                Set<Character> keys1 = s1.transitions.keySet();
                Set<Character> keys2 = s2.transitions.keySet();

                for(char key1 : keys1)
                {
                     for (char key2 : keys2)
                     {
                         if(key1 == key2)
                         {
                             String to = (""+s1.transitions.get(key1).getFirst().id + "-" +s2.transitions.get(key2).getFirst().id );
                             State toState;
                             if(tmpResults.containsKey(to))
                             {
                                 toState = tmpResults.get(to);
                             }
                             else
                             {
                                 toState = new State(to);
                                 toState.finish = false;
                                 toState.start = false;

                                 tmpResults.put(to, toState);
                             }
//                             boolean finish = intersectX.finish;
                             intersectX.addTransitionWithoutSettingFinish(key1, toState);
//                             intersectX.finish = finish;
                             test++;
                         }
                     }
                }
            }
        }
        result = new LinkedList<State>(tmpResults.values());
        return result;
    }


//    public LinkedList<State> simplifyDFA(LinkedList<State> dfa)
//    {
//        Iterator<State> stateIterator = dfa.iterator();
//        LinkedList<State> toDelete = new LinkedList<State>();
//        while (stateIterator.hasNext())
//        {
//            State s = stateIterator.next();
//            if(isRedundant(s))
//            {
//                toDelete.add(s);
//            }
//        }
////        System.out.println("to delete "+toDelete.size());
//        dfa.removeAll(toDelete);
//        return dfa;
//    }

    private boolean isRedundant(State s)
    {
        if(s.finish) return false;
        if(s.transitions == null || s.transitions.size() == 0) return true;
        for(char key : s.transitions.keySet())
        {
            if(s.transitions.get(key).getFirst().id != s.id)
            {
                return false;
            }
        }
        return true;
    }



    public int getWordCount(int length) {

        int result = 0;




        return result;
    }


}
