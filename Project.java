import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * Main class for Project -- Scanner for a Subset of YASL (Fall 2015). Scans
 * tokens from standard input and prints the token stream to standard output.
 * 
 * @author bhoward
 * @author sthede
 */

public class Project 
{
    public static void main(String[] args) throws IOException 
    {
        System.out.println("Please enter a file name location");
        BufferedReader reader = new BufferedReader(new FileReader("Compiler.txt"));
        Scanner scanner = new Scanner(reader);
        ArrayList<Token> holder = new ArrayList<Token>();
        boolean tokenChecker = false; //boolean to check to detect begin and end tokentypes.
        Token token = scanner.next();
        
        
        do 
        {    
            //token = scanner.next();
            System.out.println(token.lexeme);
            token = scanner.program(token);
            //System.out.println(token);
            /**if (token.type == TokenType.END){
                //listprinter(holder);
                //scanner.parser(holder);
                
                tokenChecker =false;
            }
            
            if (tokenChecker == true){
                holder.add(token);
            }
            
            if (token.type == TokenType.BEGIN){
                tokenChecker = true;
            }
            
            */System.out.println(token.type);
         } while (token.type != TokenType.EOF);
        
        scanner.close();
    }
    
    
    
}
