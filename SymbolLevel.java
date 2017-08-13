import java.util.*;
import java.lang.*;
import java.io.*;


public class SymbolLevel extends LinkedList
{
    public int level;
    public LinkedList <Symbol> symbolHolder;
    
    
    public SymbolLevel(int level)
    {
        this.level = level;
        symbolHolder = new LinkedList <Symbol>();
    }

    
    boolean findSymbol (String name){
      
       
       for(int i = 0; i < symbolHolder.size();i++){
           if (symbolHolder.get(i).name.equals(name)){
               return true;
            }
        }
       
       return false;
    }
    
    void addSymbol(String category,String name,String value, TokenType type, ArrayList<Paramater>params){
        Symbol symbol = new Symbol (category,name, value, type, params);
        symbolHolder.addLast(symbol);
    }
    
    int getLevel(){
        return level;
    }
    
    void setValue(String newValue){
        
    }
    
   
}
