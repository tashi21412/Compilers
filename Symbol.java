
import java.util.*;
import java.lang.*;
import java.io.*;


public class Symbol
{
    String name;
    String category;
    String value;
    TokenType type;
    ArrayList <Paramater> params;
    
  
    
     public Symbol(String category, String name, String value, TokenType type, ArrayList <Paramater> params)
    {
        this.name = name;
        this.category = category;
        this.type = type;
        this.value = value;
        this.params = params;
    }
    
    String getName(){
        return this.name;
    }
    
    String getCategory(){
        return this.category;
    }
    
    TokenType getType(){
        return this.type;
    }
    
    String getValue (){
        return this.value;
    }
    
    void setValue(String newValue){
        this.value = newValue;
    }
    
}
