import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * A Lexical Analyzer for a subset of YASL. Uses a (Mealy) state machine to
 * extract the next available token from the input each time next() is called.
 * Input comes from a Reader, which will generally be a BufferedReader wrapped
 * around a FileReader or InputStreamReader (though for testing it may also be
 * simply a StringReader).
 * 
 * @author bhoward
 * @author sthede
 */

public class Scanner extends Exception
{
    /**
     * Construct the Scanner ready to read tokens from the given Reader.
     * 
     * @param in
     */
    //public void ArrayList<Token> holder;
    
    SymbolTable table;
    String category;
    String name;
    TokenType symbolType;
    String value;
    ArrayList <Paramater> params;
    String paramid;
    TokenType paramtype;
    BufferedWriter writer;
    
    ExpressionParser parser;
    private int stackIndexTracker;
    
    public Scanner(Reader in) 
    {   
        try{
            source = new Source(in);
            table = new SymbolTable();
            stackIndexTracker = 0;
            this.category = "";
            this.name = "";
            this.value ="";
            params = new ArrayList <Paramater>();
            this.paramid = "";
            this.writer = new BufferedWriter (new FileWriter("out.pal"));
            this.parser = new ExpressionParser();
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
    // Takes a lexeme lex and sees if it matches a keyword. If it does,
    // returns the token type for that keyword. Otherwise, it returns
    // ID.
    public TokenType findKeyword( String lex )
    {
        if ( lex.equals( "program" ) )
            return TokenType.PROGRAM;
        else if ( lex.equals( "const" ) )
            return TokenType.CONST;
        else if ( lex.equals( "begin" ) )
            return TokenType.BEGIN;
        else if ( lex.equals( "end" ) )
            return TokenType.END;
        else if ( lex.equals( "print" ) )
            return TokenType.PRINT;
        else if ( lex.equals( "div" ) )
            return TokenType.DIV;
        else if ( lex.equals( "mod" ) )
            return TokenType.MOD;
        else if ( lex.equals("$"))
            return TokenType.DOLLAR;
        else if (lex.equals("var"))
            return TokenType.VAR;
        else if (lex.equals("int"))
            return TokenType.INT;
        else if (lex.equals("boolean"))
            return TokenType.BOOL;
        else if (lex.equals("if"))
            return TokenType.IF;
        else if (lex.equals("then"))
            return TokenType.THEN;
        else if (lex.equals("while"))
            return TokenType.WHILE;
        else if (lex.equals("do"))
            return TokenType.DO;
        else if (lex.equals("prompt"))
            return TokenType.PROMPT;
        else if (lex.equals("else"))
            return TokenType.ELSE;
        else if (lex.equals("=="))
            return TokenType.EQUAL;
        else if (lex.equals("<="))
            return TokenType.LEQ;
        else if (lex.equals(">="))
            return TokenType.GEQ;
        else if (lex.equals("<>"))
            return TokenType.NEQ;
        else if (lex.equals("<"))
            return TokenType.LT;
        else if (lex.equals(">"))
            return TokenType.GT;
        else if (lex.equals("proc"))
            return TokenType.PROC;
        else
            return TokenType.ID;
    }
    
    
    /**
     * Extract the next available token. When the input is exhausted, it will
     * return an EOF token on all future calls.
     * 
     * @return the next Token object
     */
    public Token next() 
    {
        Token token;
        int state = 0;
        char curr = source.current;
        String lexeme = "";
        int start_line, start_column;
                
        // start_line and start_column track where the token began.
        start_line = source.line;
        start_column = source.column;
        
        
       
        while ( !source.atEOF )
        {
            curr = source.current;
            curr = Character.toLowerCase( curr );
            
            if ( state == 0 )  // Start state
            {
                if ( curr == ';' )
                    state = 105;
                else if ( curr == '.' )
                    state = 106;
                else if ( curr == '+' )
                    state = 107;
                else if ( curr == '-' )
                    state = 108;
                else if ( curr == '*' )
                    state = 109;
                else if ( curr == '=' )
                    state = 110;
                else if (curr == ':')
                    state = 113;
                else if (curr == '(')
                    state = 114;
                else if (curr == ')')
                    state = 115;
                else if (curr== ',')
                    state = 116;
                else if (curr == '"')
                    state =117;
               
                else if ( curr == '/' )
                    state = 3;
                else if ( curr == '{' )
                    state = 2;
                else if ( curr == '0' )
                    state = 103;
                else if ( Character.isLetter( curr ) )
                    state = 101;
                else if ( Character.isDigit( curr ) )
                    state = 102;
                // Apparently hitting Enter in Java can return a new line \n and
                // a carriage return \r. Need to check for both, and consider both
                // as white space.
                else if ( curr == ' ' || curr == '\t' || curr == '\n' || curr == '\r' )
                {
                    // White space resets the starting point.
                    state = 0;
                    start_line = source.line;
                    start_column = source.column;
                }
                else
                    state = 92;
            }
            else if ( state == 2 )  // State for { } comments
            {
                if ( curr == '}' )
                    state = 111;
                else
                    state = 2;
            }
            else if ( state == 3 )  // State for // comments
            {
                if ( curr == '/' )
                    state = 4;
                else
                    state = 93;
            }
            else if ( state == 4 )  // State for // comments
            {
                if ( curr == '\n' || curr == '\r' )
                    state = 112;
                else
                    state = 4;
            }
            else if ( state == 92 )
            {
                System.err.println( "Error: unidentified symbol!" );
                state = 0;
            }
            else if ( state == 93 )
            {
                System.err.println( "Error: unidentified use of symbol /." );
                state = 0;
            }
            else if ( state == 101 )  // State for ID
            {
                if ( Character.isLetter( curr ) ||
                     Character.isDigit( curr ) )
                    state = 101;
                else
                {
                    token = new Token( start_line, start_column,findKeyword(lexeme), lexeme );
                   
                    return token;
                }
            }
            else if ( state == 102 )  // State for NUM.
            {
                if ( Character.isDigit( curr ) )
                    state = 102;
                else
                {
                    token = new Token( start_line, start_column, TokenType.NUM, lexeme );
                    return token;
                }
            }
            else if ( state == 103 )  // State for 0.
            {
                token = new Token( start_line, start_column, TokenType.NUM, lexeme );
                
                return token;
            }
            else if ( state == 105 )  
            {
                token = new Token( start_line, start_column, TokenType.SEMI, lexeme );
                return token;
            }
            else if ( state == 106 )
            {
                token = new Token( start_line, start_column, TokenType.PERIOD, lexeme );
                return token;
            }
            else if ( state == 107 )
            {
                token = new Token( start_line, start_column, TokenType.PLUS, lexeme );
                return token;
            }
            else if ( state == 108 )
            {
                token = new Token( start_line, start_column, TokenType.MINUS, lexeme );
                return token;
            }
            else if ( state == 109 )
            {
                token = new Token( start_line, start_column, TokenType.STAR, lexeme );
                return token;
            }
            else if ( state == 110 )
            {
                token = new Token( start_line, start_column, TokenType.ASSIGN, lexeme );
                return token;
            }
            else if ( state == 111 )  // State for { } comments
            {
                state = 0;
                lexeme = "";
            }
            else if ( state == 112 )  // State for // comments
            {
                state = 0;
                lexeme = "";
            }
            else if (state == 113)
            {
                token = new Token( start_line, start_column, TokenType.COLON, lexeme );
                return token;
            }
             else if (state == 114)
            {
                token = new Token( start_line, start_column, TokenType.LPAREN, lexeme );
                return token;
            }
             else if (state == 115)
            {
                token = new Token( start_line, start_column, TokenType.RPAREN, lexeme );
                return token;
            }
            else if (state == 116)
            {
                token = new Token( start_line, start_column, TokenType.COMMA, lexeme );
                return token;
            }
            else if (state == 117)
            {
                token = new Token( start_line, start_column, TokenType.QUOTE, lexeme );
                return token;
            }
            else
            {
                System.err.println( "Error: unknown state in state machine." );
                state = 0;
            }
            
            
            
            source.advance( );
            
            // If white space, reset starting point. Otherwise add character to lexeme.
            if ( curr == ' ' || curr == '\t' || curr == '\n' || curr == '\r' )
            {
                start_line = source.line;
                start_column = source.column;
            }
            else
                lexeme = lexeme + curr; 
        }
             
        
        
        // If loop is over, must have seen EOF, so return it.
        return new Token( start_line, start_column, TokenType.EOF, "" );
        
        
    }
    
   
    
   
    
    
    
    
    //==============================PARSER
    public Token program(Token token){
         try{
                    if (token.type ==TokenType.PROGRAM){
                        this.writer.write("$junk #1");
                        this.writer.newLine();
                        token = this.next();
                        if (token.type == TokenType.ID){
                            token =this.next();
                            if (token.type == TokenType.SEMI){
                                token = this.next();
                                token = this.block(token);
                                if (token.type == TokenType.PERIOD){
                                    System.out.println("Correctly Parsed YASL program file");
                                    token = new Token(0,0,TokenType.EOF,"EOF");
                                    this.writer.write("end");
                                    this.writer.close();
                                    return token;
                                }
                            }
                        }
                        else {
                            System.out.println("Error: <ID> expected on line " + token.line);
                        }
                }
            else {
                System.out.println("Error: <Program> expected on line hello" + token.line);
            }
        
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
 
    public Token block(Token token){
        try{
            this.writer.write("$main movw SP R0");
            this.writer.newLine();
            token = this.consts(token);
            token = this.vars(token);
            token = this.procs(token);
            
            
            if (token.type == TokenType.BEGIN){
                 table.addLevel();
                 System.out.println(table.symbolTable.elementAt(0).symbolHolder.get(0).getName() + " Here I am"); 
                 token = this.next();
                 token = this.statements(token);
                        
                 if (token.type== TokenType.END){
                     token = this.next();
                     return token;
                    }
          
                }
                
        }
        catch (Exception e){
            System.out.println(e);
        }
        
        return token;
        
    }
 
    public Token consts(Token token){
    
        if (token.type ==TokenType.CONST){
            return token = this.const1(token);
        }
        return token;
   }
 
    public Token const1(Token token){
       this.category  = "const";
       token = this.next();
       if (token.type == TokenType.ID){
            this.name = token.lexeme;
            token = this.next();
            if (token.type == TokenType.ASSIGN){
                token = this.next();
                if (token.type == TokenType.NUM){
                    this.value = token.lexeme;
                    symbolType = token.type;
                    token = this.next();
                    try{
                    if (token.type == TokenType.SEMI){
                                
                        table.addSymbol(this.category,this.name,this.value, symbolType, null);//adding symbol
                        this.writer.write("movw # (var) @SP R0");
                        this.writer.newLine();
                        this.writer.write("addw #4 SP");
                        this.writer.newLine();
                        token = this.next();
                        if (token.type == TokenType.CONST){
                            return this.consts(token);
                        }
                        else{
                            return token;
                        }
                    }
                    else if(token.type!=TokenType.SEMI){
                        token = this.next();
                        System.out.println("Error: <int> expected on line " + token.line);
                    }}
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if(token.type!=TokenType.NUM){
                    token = this.next();
                    System.out.println("Error: <int> expected on line " + token.line);
                }
            }
            else if(token.type!=TokenType.ASSIGN){
                token = this.next();
                System.out.println("Error: <equal> expected on line " + token.line);
            }
        }
            
        else if(token.type!=TokenType.ID){
            System.out.println("Error: <ID> expected on line " + token.line);
        }
            
          
        return null;
    }   
 
    public Token vars(Token token){
        this.category = "vars";
        
        if (token.type == TokenType.VAR){
            token = this.next();
            if(token.type == TokenType.ID){
                    this.name = token.lexeme;
                    token = this.next();
                    try{
                        if (token.type == TokenType.COLON){
                            token = this.next();
                            token = this.type(token);
                            this.value = "";
                            this.writer.write("add #4 SP");
                            this.writer.newLine();
                            table.addSymbol(this.category, this.name,this.value,token.type,params);
                            if (token.type == TokenType.SEMI){
                                token = this.next();
                                if (token.type == TokenType.VAR){
                                    token = this.vars(token);
                                    return token;
                                }
                                else{
                                    return token;
                                }
                            }
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                
            }
        }
        return token;
   }
 
    public Token type(Token token){
        
            try{
                System.out.println(token.lexeme + " hello");
                
                if (token.type == TokenType.INT){
                    paramtype = token.type;
                    params.add(new Paramater(this.paramid, paramtype));
                    token = this.next();
                    return token;
                }
                else if (token.type== TokenType.BOOL){
                    System.out.println ("did Bool");
                    paramtype = token.type;
                    params.add(new Paramater(this.paramid, paramtype));
                    token = this.next();
                    return token;
                }
            }
            catch(Exception e){
                System.out.print(e);
            }
    
        return null;
    
    }
    
    public Token procs(Token token){ //****!!! needs symbol table to rack proc!
       
        if(token.type == TokenType.PROC){
             table.addLevel();
             stackIndexTracker++;
            token = this.proc(token);
            System.out.println (token.lexeme);
            return token;
        }
        return token;
    }
 
    public Token proc(Token token){
        
        this.category = "proc";
        if (token.type == TokenType.PROC){
            token = this.next();
            if (token.type == TokenType.ID){
                this.name =token.lexeme;
                this.value = null;
                symbolType = token.type;
                token = this.next();
                token = this.paramList(token);
                table.addSymbol(this.category, this.name,this.value,token.type,params);
                if ( token.type == TokenType.SEMI){
                    token = this.next();
                    token = this.block(token);
                    token = this.next();
                    if (token.type == TokenType.SEMI){
                        token = this.next();
                        if (token.type == TokenType.PROC){
                            token = this.procs(token);
                            System.out.println ("Just did proc");
                            return token;
                        }
                        return token;
                    }
                }
            }
            
        }
        else{
            return token;
        }
        return null;
    }
    
    public Token paramList (Token token){
        
        if (token.type == TokenType.LPAREN){
             token = this.next();
             token = this.params(token);
             if (token.type == TokenType.RPAREN){
                table.addSymbol(this.category,this.name,this.value,symbolType,params);
                
                params.clear();
                System.out.println ("did paramlist");
                return token;
            }
            
        }
        
        return token;
    }
    
    public Token params (Token token){
        System.out.println(token.lexeme);
        token = this.param(token);
        
        return token;
    }
  
    public Token param(Token token){
        if (token.type == TokenType.ID)
        {   this.paramid = token.lexeme;
            token = this.next();
            if (token.type == TokenType.COLON){
                token = this.next();
                token = this.type(token);
                token = this.followParam(token);
                params.add(new Paramater(this.paramid, paramtype));
                
                return token;
            }
        }
        return token;
    }
    
    public Token followParam(Token token){
        if (token.type == TokenType.COMMA){
            token = this.next();
            token = this.params(token);
            token = this.next();
        }
        return token;
    }
    
    public Token statements(Token token){
        token = this.statement(token);
        token = this.statementTail(token);
        return token;
    }
    
    public Token statementTail(Token token){//error might occur see if it is returning good tokens
        if (token.type == TokenType.SEMI){
            token = this.next();
            token = this.statements(token);
        }
        else if (token.type ==TokenType.END){
            token =this.next();
        }
        return token;
    }
    
    public Token statement (Token token){
        ArrayList<Token> holder = new ArrayList();
        boolean checker;
        
        if (token.type == TokenType.IF){
            token = this.next();
            while (token.type!= TokenType.THEN){

                /**while (token.type!= TokenType.AND || token.type!=TokenType.OR || token.type!=TokenType.THEN){
                    holder.add(token);
                    token = this.next();
                }
                //checker = conditionParser (holder.get(0), holder.get(2), holder.get(1));
                holder.clear();
                if (checker == false){
                    while(token.type!= TokenType.ELSE){
                        token = this.next();
                    }
                    token =this.next();
                    token = this.followIf(token);
                    return token;
                }*/
                
            
            }
            
            if (token.type == TokenType.THEN){
                token = this.next();                    
                token = this.statements(token);
                return token;
                    
            }
            
        }
        else if (token.type == TokenType.WHILE){
            token = this.next();
            while (token.type!=TokenType.DO){
                this.statement(token);
            }
            return token;
        }
        else if (token.type == TokenType.ID){
            
             if(table.findSymbol(token.lexeme,stackIndexTracker)==false){
                  System.out.println(token.lexeme + " is not defined");
             }
            this.name = token.lexeme;
            token =this.next();
            token = this.followID(token);
            return token;
        }
        
        else if (token.type == TokenType.BEGIN){
            table.addLevel();
            stackIndexTracker++;
            token = this.next();
            token = this.followBegin(token);
            return token;
        }
        else if (token.type == TokenType.PROMPT){
            token = this.next();
            token = this.followPrompt(token);
            return token;
            
        }
        
        else if (token.type == TokenType.PRINT){
            token = this.next();
            token = this.followPrint(token);
            return token;
        }
      
        return token;
        
   
    }
    
    public Token followIf(Token token){
        if (token.type == TokenType.ELSE){
            token =this.next();
            if(token.type == TokenType.IF){
                token = this.statement(token);
                return token;
            }
        }
        return token;
    }
    
    public Token followBegin (Token token){
        token = this.consts(token);
        token = this.vars(token);
        token = this.procs(token);
        token = this.statement(token);
        token = this.statementTail(token);
        if (token.type == TokenType.END){
            stackIndexTracker--;
            token =this.next();
            System.out.println("Did follow Begin");
            return token;
        }
        else {
            return token;
        }
    }
    
    public Token followID(Token token){
        if (token.type == TokenType.ASSIGN){
            token = this.next();
            if (token.type== TokenType.NUM || token.type == TokenType.ID){
                    ArrayList <Token> holder = new ArrayList <Token>();
                    
                    while(token.type != TokenType.SEMI){
                        holder.add(token);
                        token = this.next();
                    }
                    this.parser.parse(holder);
                    return token; //
            }
        }
        
        else if (token.type ==TokenType.LPAREN){
            token =this.next();
            if (token.type == TokenType.ID || token.type== TokenType.NUM){
                token =this.next();
                token = this.followExp(token);
                if (token.type ==TokenType.RPAREN){
                    token =this.next();
                    System.out.println("followid done");
                    return token;
                }
            }
        }
       
        return token;
        
        
    }
    
    public Token followExp(Token token){
        if(token.type==TokenType.COMMA){
            token =this.next();
            if (token.type == TokenType.ID || token.type== TokenType.NUM){
                token=this.next();
                token =this.followExp(token);
            }
        }
      
        return token;
        
    }
    
    public Token followPrompt(Token token ){
        token = this.followPrint(token);
        if (token.type == TokenType.COMMA){
            token=this.next();
            if (token.type == TokenType.ID){
                System.out.println(token.lexeme);
                token =this.next();
                return token;
            }
        }
      
        return token;
        
    }
    
    public Token followPrint(Token token ){
        try{
        String temp ="";
        char c;
        if (token.type == TokenType.QUOTE){
            token = this.next();
            while(token.type!=TokenType.QUOTE){
                temp = temp + token.lexeme + " ";
                token = this.next();
            }
           
            
        }
        
        else if (token.type== TokenType.ID){
            temp = token.lexeme;
            
        }
        System.out.println(temp);
        for (int i =0; i < temp.length();i++){
            c = temp.charAt(i);
            this.writer.write("outb ^" + c);
            this.writer.newLine();
        }
        token = this.next();
    }
    catch(Exception e){
        System.out.println(e);
    }
        
        return token;
    }
    
    void printparams(){
        for (int i = 0 ; i < params.size(); i++){
            System.out.println (params.get(i).getId() + " " + params.get(i).getType());
        }
    }
    
    public void listprinter(ArrayList<Token> listing){
        for (int i = 0;  i < listing.size();i++){
            System.out.println(listing.get(i).lexeme);
        }
    }

    
    /**
     * Close the underlying Reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException 
    {
        source.close();
    }
    
    private Source source;
}
