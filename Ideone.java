import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
	public static void main (String[] args) throws java.lang.Exception
	{
		SymbolTable table = new SymbolTable();
		table.addLevel();
		Token token = new Token (0,0,TokenType.ID, "x");
		table.addSymbol("Vars", "y" , "8" , TokenType.NUM,null);// category, nameorlexeme, value, tokentype,params 
		table.printSymbolTable();
		System.out.println(table.findSymbol(token.lexeme));
		
		BufferedWriter writer = new BufferedWriter (new FileWriter ("out.pal"));
		writer.write("This is a test");
		writer.close();
		  
                //if(this.next().type==TokenType.SEMI){
                    
                        
                        /**if (token.type == TokenType.NUM){
                            table.setValue(this.name,token.lexeme,stackIndexTracker);
                        }
                        else if (token.type== TokenType.ID){
                            String temp= "";
                            for (int i = 0 ; i <table.getLevelAt(stackIndexTracker).symbolHolder.size(); i++ ){
                                if (table.getLevelAt(stackIndexTracker).symbolHolder.get(i).name == token.lexeme){
                                    temp = table.getLevelAt(stackIndexTracker).symbolHolder.get(i).value;
                                }
                            }
                            table.setValue(this.name,temp, stackIndexTracker);
                        }
                        token = this.next();
                        return token;   //returns semi
                    }*/
              
                //}
	}
}