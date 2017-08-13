
import java.util.*;
import java.lang.*;
import java.io.*;

public class SymbolTable
{
    public Stack<SymbolLevel> symbolTable;
    public SymbolLevel inCurrentLevel;
    
    
    public SymbolTable(){
        inCurrentLevel = new SymbolLevel(0);
        symbolTable = new Stack <SymbolLevel>();
       
    }
    
    public void addLevel(){
      
       SymbolLevel newLevel = new SymbolLevel(inCurrentLevel.level);
       newLevel.symbolHolder = (LinkedList)inCurrentLevel.symbolHolder.clone();
       symbolTable.push(newLevel);
       inCurrentLevel.level+=1;
       inCurrentLevel.symbolHolder.clear();
       
    }
    
    public void addSymbol(String category, String name, String value, TokenType type, ArrayList<Paramater> params){
      inCurrentLevel.addSymbol(category, name, value, type, params);
    }
    
    public SymbolLevel getLevelAt(int levelNum){
        return this.symbolTable.elementAt(levelNum);
    }
    
    public void setValue(String toChange, String newValue, int indexTracker){
        for (int i = 0; i < symbolTable.elementAt(indexTracker).symbolHolder.size();i++){
            if ( symbolTable.elementAt(indexTracker).symbolHolder.get(i).getName() == toChange){
                inCurrentLevel.symbolHolder.get(i).setValue(newValue);
            }
        }
    }
    
    public boolean findSymbol(String name,int indexTracker){
        return symbolTable.elementAt(indexTracker).findSymbol(name);
    }
    
    public void removeLevel(){
        symbolTable.pop();
    }
   
    public void printSymbolTable(){
       Iterator <SymbolLevel> iter = symbolTable.iterator();
       SymbolLevel oldlevel = new SymbolLevel (0);
       while(iter.hasNext()){
           oldlevel = iter.next();
           for(int i = 0;i<oldlevel.symbolHolder.size();i++){
               System.out.println(oldlevel.symbolHolder.get(i).name);
            }
        }
    
    }
}
