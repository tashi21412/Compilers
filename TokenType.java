/**
 * Enumeration of the different kinds of tokens in the YASL subset.
 * 
 * @author bhoward
 * @author sthede
 */

public enum TokenType 
{
    NUM,     // Any integer number
    SEMI,    // ;
    PLUS,    // +
    MINUS,   // -
    STAR,    // *
    ASSIGN,  // =
    PERIOD,  // .
    STRING,  // Any string constant
    ID,      // Any identifier
    PROGRAM, // Keyword program
    CONST,   // Keyword const
    BEGIN,   // Keyword begin
    PRINT,   // Keyword print
    END,     // Keyword end
    DIV,     // div operator
    MOD,     // mod operator
    DOLLAR,  // $
    
    //new tokentypes
    VAR,     // Variable
    INT,     // Int
    BOOL,    // Boolean
    PROC,    // Functions with possbile parabmeters intake
    LPAREN,  // LEFT  parenthesis  
    RPAREN,  // Right Parenthesis
    COLON,   // COLON
    COMMA,   // COMMA  
    IF,      // If
    THEN,    // Then
    WHILE,  // While
    DO,     // Do
    PROMPT, // PROMPT
    ELSE,   //ELSE
    EQUAL,  //== (Equality compare)
    LEQ,    //<= (Less than or equal to)
    GEQ,    //>= (Greater than or equal to)
    NEQ,    //!= (Not Equal to)
    LT,     //< (Less Than)
    GT,     // > (Greater than)
    OR,
    AND,
    QUOTE,  //  Quote
    EOF      // End-of-file
}
