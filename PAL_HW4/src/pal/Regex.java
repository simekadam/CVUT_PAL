package pal;

/**
 * Created with IntelliJ IDEA.
 * User: simekadam
 * Date: 12/7/12
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Regex {

    public String regex;
    public final char OR = 6;   //6
    public final char AND = 7;   //7
    public final char NULL = 0; //0

    public Regex(String regex, String alphabet)
    {
        this.regex = rewrite(regex, alphabet);
    }

    private String rewrite(String inputRegex, String alphabet)
    {

         String rewrittenAlphabet = rewriteAlphabet(alphabet);
         inputRegex = inputRegex.replaceAll("\\.", rewrittenAlphabet);
         inputRegex = "("+inputRegex+")";
         inputRegex = addAND(inputRegex, alphabet);
         inputRegex = addNULLS(inputRegex);





         return inputRegex;
    }

    public static String encapsulate(String input)
    {
        return ".*("+input+").*";
    }



    private String rewriteAlphabet(String alphabet)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(char c : alphabet.toCharArray())
        {
           sb.append(c);
           sb.append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        return sb.toString();
    }


    private String addAND(String input, String alphabet)
    {
       StringBuilder sb = new StringBuilder();
       for(int i = 0; i < input.length()-1; i++)
       {
           if(alphabet.indexOf(input.charAt(i)) != -1 && alphabet.indexOf(input.charAt(i+1)) != -1)
           {
               sb.append(input.charAt(i));
               sb.append(AND);

           }
           else if(input.charAt(i) == 41 && input.charAt(i+1) == 40)
           {
              sb.append(input.charAt(i));
               sb.append(AND);
           }
           else if( input.charAt(i) == 41 && alphabet.indexOf(input.charAt(i+1)) != -1)
           {
             sb.append(input.charAt(i));
               sb.append(AND);
           }
           else if(alphabet.indexOf(input.charAt(i)) != -1 && input.charAt(i+1) == 40)
           {
             sb.append(input.charAt(i));
               sb.append(AND);
           }
           else if(input.charAt(i) == 42 && alphabet.indexOf(input.charAt(i+1)) != -1)
           {
             sb.append(input.charAt(i));
               sb.append(AND);
           }
           else if(input.charAt(i) == 42 && input.charAt(i+1) == 40)
           {
             sb.append(input.charAt(i));
               sb.append(AND);
           }
           else
           {
             sb.append(input.charAt(i));
             if(i==input.length()-2)
             {
                 sb.append(input.charAt(i+1));
             }
           }

       }


       return  sb.toString();
    }

    private String addNULLS(String input)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < input.length()-1; i++)
        {
            if(input.charAt(i) == 40 && input.charAt(i+1) == 41)
            {
                sb.append(input.charAt(i));
                sb.append(NULL);
            }
            else if(input.charAt(i) == 124 && input.charAt(i+1) == 41)
            {
                sb.append(input.charAt(i));
                sb.append(NULL);
            }
            else if(input.charAt(i) == 40 && input.charAt(i+1) == 124)
            {
                sb.append(input.charAt(i));
                sb.append(NULL);
            }
            else if(input.charAt(i) == 124 && input.charAt(i+1) == 124)
            {
                sb.append(input.charAt(i));
                sb.append(NULL);
            }
            else if(input.charAt(i) == 40 && input.charAt(i+1) == 42)
            {
                sb.append(input.charAt(i));
                sb.append(NULL);
            }
            else
            {
                sb.append(input.charAt(i));
                if(i==input.length()-2)
                {
                    sb.append(input.charAt(i+1));
                }
            }
        }
        return sb.toString();
    }


}
