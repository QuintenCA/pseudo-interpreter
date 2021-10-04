package com.company;
//  Quinten Allen
//  CS4308: Concepts of Programming Languages

import java.util.*;
import java.io.*;
import java.util.logging.LoggingPermission;

public class Analyzer {

    //GLOBAL VARIABLE DECLARATIONS:
    static int  charClass;
    static String lexeme;
    static String token;
    static String code;
    static int codeLength;
    static Vector<String> stack = new Vector<String>();
    static int index;
    static ArrayList<String> varList = new ArrayList<String>();


    static int i;
    static int j;
    static int k;

    static Scanner s = new Scanner(System.in);



    // Looks up the character's class
    static void updateClass(char ch) {
        // The character is assumed to be the "other" class until found to be a digit or letter.
        charClass = 0;

        // If the character is a digit between 0-9 it is the digit class
        if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9')
            charClass = 1;

        // If the character is any letter it is the letter class
        else if (Character.isLetter(ch)){
            charClass = 2;
        }
    }


    static int search(String s) {

        for (int i = 0; i < varList.size(); i++) {
            if (varList.get(i).equals(s))
                return i;
        }

        return -1;
    }



    static void skipParen() {
        lex();

        while (!lexeme.equals(")")) {

            if (lexeme.equals("(")) {
                tParen();
            }

            else {

                if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                    //stack.add(index, lexeme);
                    //index++;

                    lex();
                    if (lexeme.equals("(")) {
                        tParen();
                    }

                    else {
                        //index--;
                        //stack.add(index, lexeme);
                        //index++;
                        //index++;
                    }
                }

                else {
                    //index--;
                    //stack.add(index, lexeme);
                    //index++;
                }

                lex();
            }
        }

        //index++;
    }



    static boolean skipIF() {
        //Skip adding "if" to stack
        index++;
        lex();

        //Skip adding "(" to stack
        lex();

        while (!lexeme.equals(")")) {

            if (lexeme.equals("(")) {
                skipParen();
            } else if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD") ||
                    token.equals("GREATER_THAN") || token.equals("LESS_THAN") || token.equals("EQUALS") || token.equals("LESS_OR_EQUAL") ||
                    token.equals("GRETER_OR_EQUAL") || token.equals("NOT_EQUAL")) {
                //stack.add(index, lexeme);
                //index++;

                lex();
                if (lexeme.equals("(")) {
                    skipParen();
                } else {
                    //index--;
                    //stack.add(index, lexeme);
                    //index++;
                    //index++;
                }
            } else {
                //index--;
                //stack.add(index, lexeme);
                //index++;
            }

            lex();

            if (token.equals("END"))
                break;

            if (token.equals("IDENTIFIER")) {
                //index++;
                break;
            }
        }

        //Skip the ")"
        lex();

        return false;
    }



    //Translates and executes next statement, but doesn't actually update variables
    static void skipStatement() {
        // Add first identifier to the intermediate stack
        if(!token.equals("IF")) {
            //stack.add(lexeme);
            //index++;

            // Add assignment operator to the intermediate stack
            lex();
            //stack.add(lexeme);
            //index++;

            lex();
            while (!token.equals("END")) {

                if (lexeme.equals("(")) {
                    skipParen();
                } else if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                    //stack.add(index, lexeme);
                    //index++;

                    lex();
                    if (lexeme.equals("(")) {
                        skipParen();
                    } else {
                        //index--;
                        //stack.add(index, lexeme);
                        //index++;
                        //index++;
                    }
                } else {
                    //index--;
                    //stack.add(index, lexeme);
                    //index++;
                }

                lex();

                if (token.equals("END"))
                    break;

                if (token.equals("IDENTIFIER")) {
                    //index++;
                    break;
                }

                if (token.equals("IF")) {
                    //index++;
                    break;
                }
            }

            if (!token.equals("END")) {
                translate();
            }
        }

        // IF STATEMENTS
        else {
            if(skipIF())
                translate();
            else
                skipStatement();
        }
    }



    // Finds the next lexeme in the string of code
    static void lex() {

        // Check if the index is already at the end of the string
        if (i < code.length() - 1) {

            while (code.charAt(i) == ' ')
                i++;

            // Starting at i, j finds the first white space and k finds the next.
            if (code.charAt(i) != ' ') {
                j = i;
                for (k = j; k < code.length(); k++) {
                    if (code.charAt(k) == ' ')
                        break;
                }

                // Substring is made using j and k as the endpoints
                lexeme = code.substring(j, k);

                // i is moved to k so that it iterates past the substring
                i = k;
            }
            // Find out the token of the lexeme
            analyze(lexeme);
            //System.out.println("\nLexeme is: " + lexeme + " | Token is: " + token);
        }

        else
            token = "END";
    }



    // Checks if the code given forms a proper statement in the grammar
    static void parse() {
        i = 0;
        j = 0;
        k = 0;
        varList.add("0");

        // Find the first lexeme
        lex();

        // Check if the tokens form a statement
        if(checkStatement()) {
            System.out.println("\nGRAMMAR IS CORRECT!");
            //System.out.println(grammar);
        }
        else
            System.out.println("\nERROR - UNEXPECTED LEXEME:  \"" + lexeme + "\"");

        i = 0;
        j = 0;
        k = 0;
        index = 0;
        lex();
        translate();
    }



    static void tParen() {
        lex();

        while (!lexeme.equals(")")) {

            if (lexeme.equals("(")) {
                tParen();
            }

            else {

                if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                    stack.add(index, lexeme);
                    index++;

                    lex();
                    if (lexeme.equals("(")) {
                        tParen();
                    }

                    else {
                        index--;
                        stack.add(index, lexeme);
                        index++;
                        index++;
                    }
                }

                else {
                    index--;
                    stack.add(index, lexeme);
                    index++;
                }

                lex();
            }
        }

        index++;
    }



    static void translate() {
        // Add first identifier to the intermediate stack
        if(!token.equals("IF")) {
            stack.add(lexeme);
            index++;

            // Add assignment operator to the intermediate stack
            lex();
            stack.add(lexeme);
            index++;

            lex();
            while (!token.equals("END")) {

                if (lexeme.equals("(")) {
                    tParen();
                } else if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                    stack.add(index, lexeme);
                    index++;

                    lex();
                    if (lexeme.equals("(")) {
                        tParen();
                    } else {
                        index--;
                        stack.add(index, lexeme);
                        index++;
                        index++;
                    }
                } else {
                    index--;
                    stack.add(index, lexeme);
                    index++;
                }

                lex();

                if (token.equals("END"))
                    break;

                if (token.equals("IDENTIFIER")) {
                    index++;
                    break;
                }

                if (token.equals("IF")) {
                    index++;
                    break;
                }
            }

            //  Beginning the execution and evaluation of the translated code.
            index = 0;

            while (!stack.get(index).equals("=") && !stack.get(index).equals("+") && !stack.get(index).equals("-") &&
                    !stack.get(index).equals("*") && !stack.get(index).equals("/") && !stack.get(index).equals("%")) {
                index ++;
            }

            int value = 0;
            int operand1 = 0;
            int operand2 = 0;
            String operator = stack.get(index);
            stack.remove(index);
            index--;

            if (operator.equals("=")) {

                try {
                    value = Integer.parseInt(stack.get(index));
                }
                catch (NumberFormatException e) {
                    value = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
                }

                stack.remove(index);
                index--;

                String id = stack.get(index);
                stack.remove(index);

                //System.out.println("\n" + id + " is equal to " + value);

                if (search(id) == -1) {
                    varList.add(id);
                    varList.add(Integer.toString(value));
                } else {
                    varList.set(search(id) + 1, Integer.toString(value));
                }
            }

            else {
                try {
                    operand2 = Integer.parseInt(stack.get(index));
                }
                catch (NumberFormatException e) {
                    operand2 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
                }
                stack.remove(index);
                index--;

                try {
                    operand1 = Integer.parseInt(stack.get(index));
                }
                catch (NumberFormatException e) {
                    operand1 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
                }
                stack.remove(index);

                execute(evaluate(operand1, operand2, operator));
            }

            if (!token.equals("END")) {
                translate();
            }
        }

        // IF STATEMENTS
        else {
            if(translateIF())
                translate();
            else
                skipStatement();
        }
    }


    static boolean translateIF() {
        //Skip adding "if" to stack
        index++;
        lex();

        //Skip adding "(" to stack
        lex();

        while (!lexeme.equals(")")) {

            if (lexeme.equals("(")) {
                tParen();
            } else if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD") ||
                    token.equals("GREATER_THAN") || token.equals("LESS_THAN") || token.equals("EQUALS") || token.equals("LESS_OR_EQUAL") ||
                    token.equals("GRETER_OR_EQUAL") || token.equals("NOT_EQUAL")) {
                stack.add(index, lexeme);
                index++;

                lex();
                if (lexeme.equals("(")) {
                    tParen();
                } else {
                    index--;
                    stack.add(index, lexeme);
                    index++;
                    index++;
                }
            } else {
                index--;
                stack.add(index, lexeme);
                index++;
            }

            lex();

            if (token.equals("END"))
                break;

            if (token.equals("IDENTIFIER")) {
                index++;
                break;
            }
        }

        //Skip the ")"
        lex();

        //  Beginning the execution and evaluation of the translated code.
        index = 0;

        while (!stack.get(index).equals("=") && !stack.get(index).equals("!=") && !stack.get(index).equals(">=") &&
                !stack.get(index).equals("<=") && !stack.get(index).equals(">") && !stack.get(index).equals("<")) {
            index ++;
        }

        int operand1 = 0;
        int operand2 = 0;
        String operator = stack.get(index);
        stack.remove(index);
        index--;

        if (operator.equals("=") || operator.equals("!=") || operator.equals(">=") ||
        operator.equals("<=") || operator.equals(">") || operator.equals("<")) {

            try {
                operand2 = Integer.parseInt(stack.get(index));
            }
            catch (NumberFormatException e) {
                operand2 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
            }

            stack.remove(index);
            index--;

            try {
                operand1 = Integer.parseInt(stack.get(index));
            }
            catch (NumberFormatException e) {
                operand1 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
            }

            stack.remove(index);

            if (evaluateCon(operand1, operand2, operator))
                return true;
            else
                return false;
        }

        else {
            try {
                operand2 = Integer.parseInt(stack.get(index));
            }
            catch (NumberFormatException e) {
                operand2 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
            }
            stack.remove(index);
            index--;

            try {
                operand1 = Integer.parseInt(stack.get(index));
            }
            catch (NumberFormatException e) {
                operand1 = Integer.parseInt(varList.get(search(stack.get(index)) + 1));
            }
            stack.remove(index);

            execute(evaluate(operand1, operand2, operator));
        }

        return false;
    }


    static int evaluate(int op1, int op2, String operator) {

        if (operator.equals("+")) {
            return op1 + op2;
        }

        else if (operator.equals("-")) {
            return op1 - op2;
        }

        else if (operator.equals("*")) {
            return op1 * op2;
        }

        else if (operator.equals("/")) {
            return op1 / op2;
        }

        else {
            return op1 % op2;
        }
    }


    static boolean evaluateCon(int op1, int op2, String operator) {

        if (operator.equals("=")) {
            if(op1 == op2)
                return true;
        }

        if (operator.equals("!=")) {
            if(op1 != op2)
                return true;
        }

        if (operator.equals(">=")) {
            if(op1 >= op2)
                return true;
        }

        if (operator.equals("<=")) {
            if(op1 <= op2)
                return true;
        }

        if (operator.equals(">")) {
            if(op1 > op2)
                return true;
        }

        if (operator.equals("<")) {
            if(op1 < op2)
                return true;
        }

        return false;
    }


    static void execute(int temp) {
        index = 0;

        while (!stack.get(index).equals("=") && !stack.get(index).equals("+") && !stack.get(index).equals("-") &&
                !stack.get(index).equals("*") && !stack.get(index).equals("/") && !stack.get(index).equals("%")) {
            index ++;
        }

        String operator = stack.get(index);
        stack.remove(index);
        index--;

        if (operator.equals("=")) {

            String value = Integer.toString(temp);

            String id = stack.get(index);
            stack.remove(index);

            if (search(id) == -1) {
                varList.add(id);
                varList.add(value);
            } else {
                varList.set(search(id) + 1, value);
            }
            //System.out.println("\n" + id + " is equal to " + value);
        }

        else {

            int operand = Integer.parseInt(stack.get(index));

            stack.remove(index);
            index--;

            temp = evaluate(operand, temp, operator);

            execute(temp);
        }
    }


    // Checks if tokens form proper statements in the grammar
    static boolean checkStatement() {

        // ID
        if (token.equals("IDENTIFIER")) {
            lex();

            // ID EQUALS
            if (token.equals("EQUALS")) {
                lex();

                // ID EQUALS term
                if (checkTerm()) {

                    if (token.equals("END")) {
                        return true;
                    }

                    // ID EQUALS term statement
                    else if (checkStatement()) {

                        if (token.equals("END")) {
                            return true;
                        }
                    }
                }
            }
        }

        // if
        if (token.equals("IF")) {
            lex();

            // if (
            if (token.equals("LEFT_PAREN")) {
                lex();

                // if ( expr
                if(checkExpression()) {

                    // if ( expr )
                    if(token.equals("RIGHT_PAREN")) {
                        lex();

                        // if ( expr ) statement
                        if (checkStatement() && token.equals("END")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }



    // Checks if tokens form an expression
    static boolean checkExpression() {
        String expression = "\nEXPRESSION:";

        // term
        if (checkTerm()) {

            // term eq_v
            if (token.equals("EQUALS") || token.equals("GREATER_THAN") || token.equals("LESS_THAN")
                    || token.equals("GREATER_OR_EQUAL") || token.equals("LESS_OR_EQUAL") || token.equals("NOT_EQUAL")) {
                expression = expression + " " + lexeme;
                lex();

                // term eq_v term
                if (checkTerm()) {
                    return true;
                }
            }
        }
        return false;
    }



    // Terms can contain terms, so checkTerm is recursive
    static boolean checkTerm() {
        // Checks if this is a complete term
        boolean complete = false;

        // simple_term (base case)
        if (token.equals("IDENTIFIER") || token.equals("SIGNED_INTEGER") || token.equals("UNSIGNED_INTEGER")) {
            complete = true;
            lex();

            // simple_term op
            if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                complete = false;
                lex();

                // simple_term op term
                         if(checkTerm()) {
                    complete = true;
                }
            }
        }

        // (
        else if (token.equals("LEFT_PAREN")) {
            lex();

            // ( term
            if(checkTerm()) {

                // ( term )
                if (token.equals("RIGHT_PAREN")) {
                    complete = true;
                    lex();

                    // ( term ) op
                    if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                        complete = false;
                        lex();

                        // ( term ) op term
                        if (checkTerm()) {
                            complete = true;
                        }
                    }
                }

                // ( term op
                else if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                    complete = false;
                    lex();

                    // ( term op term
                    if(checkTerm()) {

                        // ( term op term )
                        if (token.equals("RIGHT_PAREN")) {
                            complete = true;
                            lex();

                            // ( term op term ) op
                            if (token.equals("PLUS") || token.equals("MINUS") || token.equals("STAR") || token.equals("DIVOP") || token.equals("MOD")) {
                                complete = false;
                                lex();

                                // ( term op term ) op term
                                if (checkTerm()) {
                                    complete = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return complete;
    }



    // Analyzes the given lexeme to determine if it is a legal word
    static void analyze (String s) {
        // Assigns the given string to the global lexeme variable
        lexeme = s;

        //Ensures all lexemes are under 100 characters long
        if (lexeme.length() >= 100) {
            System.out.println("\nERROR: Lexeme too long - " + s);
            System.exit(0);
        }

        int length = lexeme.length();   // Length of the lexeme
        int i = 0;

        //Check the character class of the first character
        updateClass(lexeme.charAt(i));

        //First character is a digit
        if (charClass == 1){

            if (i == length - 1){
                token = "UNSIGNED_INTEGER";
                return;
            }
            i++;
            updateClass(lexeme.charAt(i));

            //Followed by any number of digits
            while (charClass == 1){

                if (i == length - 1){
                    token = "UNSIGNED_INTEGER";
                    return;
                }
                i++;
                updateClass(lexeme.charAt(i));
            }
            System.out.print("\nERROR - Illegal character: " + lexeme.charAt(i) + " | Lexeme: " + lexeme);
            System.exit(0);

        }

        // First character is a letter
        else if (charClass == 2){

            if (lexeme.equals("true")) {
                token = "TRUE";
                return;
            }

            if (lexeme.equals("false")) {
                token = "FALSE";
                return;
            }

            if (lexeme.equals("if")) {
                token = "IF";
                return;
            }

            if (i == length - 1){
                token = "IDENTIFIER";
                return;
            }
            i++;
            updateClass(lexeme.charAt(i));

            //Next characters are any number of letters and numbers
            while (charClass == 1 || charClass == 2 || lexeme.charAt(i) == '_'){

                if (i == length - 1){
                    token = "IDENTIFIER";
                    return;
                }
                i++;
                updateClass(lexeme.charAt(i));

            }
            System.out.print("\nERROR: Illegal character: " + lexeme.charAt(i) + " | Lexeme: " + lexeme);
            System.exit(0);

        }

        // First character is a digit
        else if (charClass == 1){

            if (i == length - 1){
                token = "UNSIGNED_INTEGER";
                return;
            }
            i++;
            updateClass(lexeme.charAt(i));

            //Followed by any number of digits
            while (charClass == 1){

                if (i == length - 1){
                    token = "UNSIGNED_INTEGER";
                    return;
                }
                i++;
                updateClass(lexeme.charAt(i));

            }
            System.out.print("\nERROR - Illegal character: " + lexeme.charAt(i) + " | Lexeme: " + lexeme);
            System.exit(0);

        }

        //First character is a sign
        else if (lexeme.charAt(i) == '+' || lexeme.charAt(i) == '-'){

            if (i == length - 1){
                if (lexeme.charAt(i) == '+'){
                    token = "PLUS";
                    return;
                }
                if (lexeme.charAt(i) == '-'){
                    token = "MINUS";
                    return;
                }
            }
            i++;
            updateClass(lexeme.charAt(i));

            //Followed by any number of digits
            while (charClass == 1){

                if (i == length - 1){
                    token = "SIGNED_INTEGER";
                    return;
                }
                i++;
                updateClass(lexeme.charAt(i));

            }
            System.out.print("\nERROR - Illegal character: " + lexeme.charAt(i) + " | Lexeme: " + lexeme);
            System.exit(0);

        }

        else {
            lookUp();
            if(token == "ILLEGAL") {
                System.out.print("\nERROR: Illegal token - " + lexeme);
                System.exit(0);
            }
        }
    }



    //Lookup table for keywords and symbols
    static void lookUp(){

        if (lexeme.equals("("))
            token = "LEFT_PAREN";
        else if (lexeme.equals(")"))
            token = "RIGHT_PAREN";
        else if (lexeme.equals("true"))
            token = "TRUE";
        else if (lexeme.equals("false"))
            token = "FALSE";
        else if (lexeme.equals("-"))
            token = "MINUS";
        else if (lexeme.equals("+"))
            token = "PLUS";
        else if (lexeme.equals("*"))
            token = "STAR";
        else if (lexeme.equals("/"))
            token = "DIVOP";
        else if (lexeme.equals("%"))
            token = "MOD";
        else if (lexeme.equals(">"))
            token = "GREATER_THAN";
        else if (lexeme .equals("<"))
            token = "LESS_THAN";
        else if (lexeme.equals("="))
            token = "EQUALS";
        else if (lexeme.equals("<="))
            token = "LESS_OR_EQUAL";
        else if (lexeme.equals(">="))
            token = "GREATER_OR_EQUAL";
        else if (lexeme.equals("!="))
            token = "NOT_EQUAL";
        else
            token = "ILLEGAL";
    }



    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        // Reads a line of code from the user
        System.out.println("Input Code:");

        code = s.nextLine();
        codeLength = code.length();

        parse();

        String id = "";
        String val = "";
        for (int i = 0; i < varList.size(); i++) {
            if (varList.get(i) != "0") {
                id = varList.get(i);
                i++;
                val = varList.get(i);
                System.out.println("\n" + id + " is " + val);
            }
        }

        System.out.println("\nAnalysis Complete");
    }
}
