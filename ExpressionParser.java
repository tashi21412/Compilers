
import java.util.*;
import java.lang.*;
import java.io.*;

public class ExpressionParser
{
   
 
    public void parse(ArrayList<Token> list)
    {
        Stack <Token> stack = new Stack<Token>();
        ArrayList<Token> holder = new ArrayList();
        //list.add(new Token (0,0, TokenType.DOLLAR, "$"));
        Token token;
        boolean checker = true;
        int i=0;
        
        System.out.println( list );
        
        while (i < list.size()){
            token = list.get(i);
            
           
            
            if (token.type == TokenType.NUM || token.type == TokenType.ID){
                
                System.out.println(token.lexeme);
                i++;
            }
            else if (stack.isEmpty()){
                stack.push(token);
                i++;
            }
            else if (token.type == TokenType.PLUS || token.type == TokenType.MINUS ||token.type == TokenType.DIV || token.type == TokenType.STAR || token.type == TokenType.MOD)
            {
                if (stack.isEmpty()){
                    stack.push(token);
                    i++;
                }
                else if (prec(stack.peek(),token) == TokenType.LT){
                    stack.push (token);
                    i++;
                }
                
                else 
                {  
                   
                        
                    while(!stack.isEmpty() && prec(stack.peek(),token)!=TokenType.LT){
                           if (stack.peek().type == TokenType.LPAREN || stack.peek().type ==TokenType.RPAREN){
                               stack.pop();
                            }
                            else{
                                System.out.println(stack.pop().lexeme);
                            }   
                       
                         
                    }
                    
                    
                    stack.push(token);
                    i++;
                }
            }
            
            else if (token.type == TokenType.LPAREN){
                stack.push(token);
                i++;
            }
            else if(token.type == TokenType.RPAREN){
                while (stack.peek().type!= TokenType.LPAREN){
                     System.out.println(stack.pop().lexeme);
                }
                
                stack.pop();
                i++;
            }
        }
        
        while (!stack.isEmpty()){
            System.out.println(stack.pop().lexeme);
        }
     }
     
     
     public TokenType prec(Token A, Token B){
        // PrecedenceTable TokenType.GT = Greater than TokenType.LT = less then
        if (A.type == TokenType.PLUS && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.PLUS && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.PLUS && B.type == TokenType.STAR){
            return TokenType.LT;
        }
        
        else  if (A.type == TokenType.PLUS && B.type == TokenType.DIV){
            return TokenType.LT;
        }
        
       else  if (A.type == TokenType.PLUS && B.type == TokenType.MOD){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.PLUS && B.type == TokenType.NUM){
            return TokenType.LT;
        }
        else  if (A.type == TokenType.PLUS && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        
       else if (A.type == TokenType.MINUS && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.MINUS && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.MINUS && B.type == TokenType.STAR){
            return TokenType.LT;
        }
        
        else  if (A.type == TokenType.MINUS && B.type == TokenType.DIV){
            return TokenType.LT;
        }
        
       else  if (A.type == TokenType.MINUS && B.type == TokenType.MOD){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.MINUS && B.type == TokenType.NUM){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.MINUS && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        
        else if (A.type == TokenType.STAR && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.STAR && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.STAR && B.type == TokenType.STAR){
            return TokenType.GT;
        }
        
        else  if (A.type == TokenType.STAR && B.type == TokenType.DIV){
            return TokenType.GT;
        }
        
       else  if (A.type == TokenType.STAR && B.type == TokenType.MOD){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.STAR && B.type == TokenType.NUM){
            return TokenType.LT;
        }
        else  if (A.type == TokenType.STAR && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        
        else if (A.type == TokenType.DIV && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.DIV && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.DIV && B.type == TokenType.STAR){
            return TokenType.GT;
        }
        
        else  if (A.type == TokenType.DIV && B.type == TokenType.DIV){
            return TokenType.GT;
        }
        
       else  if (A.type == TokenType.DIV && B.type == TokenType.MOD){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.DIV && B.type == TokenType.NUM){
            return TokenType.LT;
        }
        else  if (A.type == TokenType.DIV && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        
        else if (A.type == TokenType.MOD && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.MOD && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.MOD && B.type == TokenType.STAR){
            return TokenType.GT;
        }
        
        else  if (A.type == TokenType.MOD && B.type == TokenType.DIV){
            return TokenType.GT;
        }
        
       else  if (A.type == TokenType.MOD && B.type == TokenType.MOD){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.MOD && B.type == TokenType.NUM){
            return TokenType.LT;
        }
        else  if (A.type == TokenType.MOD && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        
        else if (A.type == TokenType.NUM && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.NUM && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.NUM && B.type == TokenType.STAR){
            return TokenType.GT;
        }
        
        else  if (A.type == TokenType.NUM && B.type == TokenType.DIV){
            return TokenType.GT;
        }
        
       else  if (A.type == TokenType.NUM && B.type == TokenType.MOD){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.NUM && B.type == TokenType.NUM){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.NUM && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
        else if (A.type == TokenType.DOLLAR && B.type == TokenType.PLUS){
            return TokenType.LT;
        }
        else  if (A.type == TokenType.DOLLAR && B.type == TokenType.MINUS){
            return TokenType.LT;
        }
       
        else if (A.type == TokenType.DOLLAR && B.type == TokenType.STAR){
            return TokenType.LT;
        }
        
        else  if (A.type == TokenType.DOLLAR && B.type == TokenType.DIV){
            return TokenType.LT;
        }
        
       else  if (A.type == TokenType.DOLLAR && B.type == TokenType.MOD){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.DOLLAR && B.type == TokenType.NUM){
            return TokenType.LT;
        }
       else  if (A.type == TokenType.DOLLAR && B.type == TokenType.DOLLAR){
            return TokenType.LT;
        }
         else if (A.type == TokenType.LPAREN && B.type == TokenType.PLUS){
            return TokenType.GT;
        }
        else  if (A.type == TokenType.LPAREN && B.type == TokenType.MINUS){
            return TokenType.GT;
        }
       
        else if (A.type == TokenType.LPAREN&& B.type == TokenType.STAR){
            return TokenType.GT;
        }
        
        else  if (A.type == TokenType.LPAREN && B.type == TokenType.DIV){
            return TokenType.GT;
        }
        
       else  if (A.type == TokenType.LPAREN && B.type == TokenType.MOD){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.LPAREN && B.type == TokenType.NUM){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.LPAREN && B.type == TokenType.DOLLAR){
            return TokenType.GT;
        }
       else  if (A.type == TokenType.LPAREN && B.type == TokenType.RPAREN){
            return TokenType.GT;
        }
        return TokenType.LT;
    }
     
}
