import java.util.ArrayList;

public class AnalizadorSintactico {
    private ArrayList<Token> tokens;
    private int siguienteIndice;

    public boolean analizar(ArrayList<Token> tokens) {
        this.tokens = tokens;
        siguienteIndice = 0;

        return declaracionClases(0);
    }

    private boolean declaracionClases(int i) {
        if (i == tokens.size()) {
            return true;
        }
        if (!clase(i)) {
            return false;
        }
        return declaracionClases(siguienteIndice);
    }

    private boolean clase(int i) {
        if (i == tokens.size()) {
            return false;
        }

        if (modificadorAcceso(i)) {
            i++;
        }

        if (evaluar(i, Token.FINAL)) {
            i++;
        }

        if (!evaluar(i, Token.CLASS) ||
                !evaluar(++i, Token.IDENTIFIER)) {
            return false;
        }

        if (evaluar(++i, Token.EXTENDS)) {
            if (!evaluar(++i, Token.IDENTIFIER)) {
                return false;
            }
            i++;
        }

        if (evaluar(i, Token.IMPLEMENTS)) {
            if (!implementaciones(++i)) {
                return false;
            }
            i = siguienteIndice;
        }

        if (!evaluar(i, Token.LEFT_CURLY_BRACE) || !cuerpoClase(++i)) {
            return false;
        }
        i = siguienteIndice;

        if (!evaluar(i, Token.RIGHT_CURLY_BRACE)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    private boolean implementaciones(int i) {
        if (!evaluar(i, Token.IDENTIFIER)) {
            return false;
        }
        if (!evaluar(++i, Token.COMMA)) {
            siguienteIndice = i;
            return true;
        }
        return implementaciones(++i);
    }

    private boolean cuerpoClase(int i) {
        if (declaracionAtributo(i) || declaracionMetodo(i)) {
            cuerpoClase(siguienteIndice);
            return true;
        }
        if (evaluar(i, Token.RIGHT_CURLY_BRACE)) {
            siguienteIndice = i;
            return true;
        }
        return false;
    }

    private boolean declaracionAtributo(int i) {
        if (modificadorAcceso(i)) {
            i++;
        }
        if (evaluar(i, Token.STATIC)) {
            i++;
        }
        return declaracionVariable(i);
    }

    private boolean declaracionMetodo(int i) {
        if (modificadorAcceso(i)) {
            i++;
        }
        if (evaluar(i, Token.STATIC)) {
            i++;
        }
        if (evaluar(i, Token.FINAL)) {
            i++;
        }

        if (!tipoRetorno(i) || !evaluar(++i, Token.IDENTIFIER) || !evaluar(++i, Token.LEFT_PARENTHESIS)) {
            return false;
        }
        if (listaParametros(++i)) {
            i = siguienteIndice;
        }
        if (!evaluar(i, Token.RIGHT_PARENTHESIS) || !evaluar(++i, Token.LEFT_CURLY_BRACE) || !cuerpoMetodo(++i)) {
            return false;
        }
        return evaluar(siguienteIndice++, Token.RIGHT_CURLY_BRACE);
    }

    private boolean modificadorAcceso(int i) {
        return evaluar(i, Token.PUBLIC) || evaluar(i, Token.PRIVATE) || evaluar(i, Token.PROTECTED);
    }

    private boolean cuerpoMetodo(int i) {
        if (evaluar(i, Token.RIGHT_CURLY_BRACE)) {
            siguienteIndice = i;
            return true;
        }
        return listaInstrucciones(i);
    }

    private boolean listaInstrucciones(int i) {
        if (!declaracionVariable(i) && !asignacionValorVariable(i) && !estructuraCondicional(i) &&
                !estructuraRepetitiva(i) && !salto(i)) {
            return false;
        }
        if (asignacionValorVariable(i)) {
            i = siguienteIndice;
            if (!evaluar(i, Token.SEMICOLON)) {
                return false;
            }
            siguienteIndice++;
        }
        i = siguienteIndice;
        if (!listaInstrucciones(siguienteIndice)) {
            siguienteIndice = i;
        }
        return true;
    }

    private boolean salto(int i) {
        siguienteIndice = i + 1;
        return evaluar(i, Token.BREAK) || evaluar(i, Token.CONTINUE) || retorno(i);
    }

    private boolean retorno(int i) {
        if (!evaluar(i, Token.RETURN)) {
            return false;
        }
        if (evaluar(++i, Token.SEMICOLON)) {
            siguienteIndice = i + 1;
            return true;
        }
        if (literal(++i)) {
            if (!evaluar(++i, Token.SEMICOLON)) {
                return false;
            }
            siguienteIndice = i + 1;
            return true;
        }
        return false;
    }

    private boolean listaParametros(int i) {
        if (!parametro(i)) {
            return false;
        }
        i = siguienteIndice;
        if (evaluar(i, Token.COMMA)) {
            return listaParametros(++i);
        }
        return true;
    }

    private boolean parametro(int i) {
        siguienteIndice = i + 2;
        return tipoDato(i) && evaluar(++i, Token.IDENTIFIER);
    }

    public boolean declaracionVariable(int i){
        if(evaluar(i, Token.FINAL)){
            i++;
        }

        if(!tipoDato(i)){
            return false;
        }

        if (!evaluar(++i, Token.IDENTIFIER)){
            return false;
        }
        
        if (asignacionValorVariable(i)) {
            i = siguienteIndice;
        } else {
            i++;
        }

        siguienteIndice = i + 1;
        return evaluar(i, Token.SEMICOLON);
    }

    public boolean asignacionValorVariable(int i){ // TODO: Asignar valor a varias variables, es posible.
        if (!evaluar(i, Token.IDENTIFIER)){
            return false;
        }
        if(!operadorAsignacion(++i)){
            return false;
        }
        return operacionAritmetica(++i);
    }

    private boolean estructuraCondicional(int i) {
        return declaracionIf(i);
    }

    private boolean declaracionIf(int i){
        if(!evaluar(i, Token.IF)){
            return false;
        }

        if(!evaluar(++i, Token.LEFT_PARENTHESIS)){
            return false;
        }

        if(!listaCondiciones(++i)){
            return false;
        }

        i = siguienteIndice;
        if(!evaluar(i, Token.RIGHT_PARENTHESIS)){
            return false;
        }

        if(!evaluar(++i, Token.LEFT_CURLY_BRACE)){
            return false;
        }

        if(!listaInstrucciones(++i)){
            return false;
        }
        i = siguienteIndice;
        System.out.println(tokens.get(i));
        if(!evaluar(i, Token.RIGHT_CURLY_BRACE)){
            return false;
        }
        if(!evaluar(++i, Token.ELSE)){
            siguienteIndice = i;
            return true;
        }
        siguienteIndice = i;
        return false; // TODO: meter [else if] y [else]
    }

    private boolean estructuraRepetitiva(int i) {
        return declaracionFor(i) || declaracionWhile(i) || declaracionDoWhile(i);
    }

    private boolean declaracionFor(int i) {
        if (!evaluar(i, Token.FOR) || !evaluar(++i, Token.LEFT_PARENTHESIS) || !primeraParteFor(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!listaCondiciones(i)) {
            return false;
        }
        i = siguienteIndice;
        if (!asignacionValorVariable(i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_PARENTHESIS) || evaluar(++i, Token.LEFT_CURLY_BRACE) || !listaInstrucciones(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_CURLY_BRACE)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    private boolean primeraParteFor(int i) {
        if (declaracionVariable(i)) {
            return true;
        }
        if (evaluar(i, Token.SEMICOLON)) {
            siguienteIndice = i + 1;
            return true;
        }
        return false;
    }

    private boolean declaracionWhile(int i) {
        if (!parteWhile(i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.LEFT_CURLY_BRACE) || !listaInstrucciones(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_CURLY_BRACE)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    private boolean declaracionDoWhile(int i){
        if (!evaluar(i, Token.DO) || !evaluar(++i, Token.LEFT_CURLY_BRACE) || !listaInstrucciones(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_CURLY_BRACE) || !parteWhile(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.SEMICOLON)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    private boolean parteWhile(int i) {
        if (!evaluar(i, Token.WHILE) || !evaluar(++i, Token.LEFT_PARENTHESIS) || !listaCondiciones(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_PARENTHESIS)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    // TODO : Reestructurar las condiciones, desde la gramática
    private boolean listaCondiciones(int i) {
        if (!condicion(i)) {
            return false;
        }
        i = siguienteIndice;
        if (!operadorLogico(i)) {
            return true;
        }
        return listaCondiciones(++i);
    }

    private boolean condicion(int i) {
        if (valorCondicion(i)) {
            if (!operadorRelacional(++i)) {
                siguienteIndice = i;
                return true;
            }
            return valorCondicion(++i);
        }
        if (evaluar(i, Token.NOT)) {
            i++;
        }
        if (!evaluar(i, Token.LEFT_PARENTHESIS)) {
            return false;
        }
        if (!condicion(++i)) {
            return false;
        }
        i = siguienteIndice;
        if (!evaluar(i, Token.RIGHT_PARENTHESIS)) {
            return false;
        }
        siguienteIndice = i + 1;
        return true;
    }

    private boolean valorCondicion(int i) {
        if (literal(i)) {
            siguienteIndice = i + 1;
            return true;
        }
        return operacionAritmetica(i);
    }

    private boolean operacionAritmetica(int i) {
        if (evaluar(i, Token.LEFT_PARENTHESIS)) {
            if (!operacionAritmetica(++i)) {
                return false;
            }
            i = siguienteIndice;
            if (!evaluar(i, Token.RIGHT_PARENTHESIS)) {
                return false;
            }
            if (!operadorAritmetico(++i)) {
                siguienteIndice = i;
                return true;
            }
            return operacionAritmetica(++i);
        }
        if (!valorOperacion(i)) {
            return false;
        }
        if (!operadorAritmetico(++i)) {
            siguienteIndice = i;
            return true;
        }
        return operacionAritmetica(++i);
    }

    private boolean valorOperacion(int i) {
        return numero(i) || evaluar(i, Token.IDENTIFIER);
    }

    public boolean literal(int i){
        return valorBooleano(i) || numero(i) || constante(i) || stringValue(i);
    }

    public boolean stringValue(int i){
        return evaluar(i, Token.STRING_VALUE);
    }

    public boolean constante(int i){
        return evaluar(i, Token.NULL);
    }

    public boolean numero(int i){
        return evaluar(i, Token.NUMBER); // TODO: AGREGAR EL REAL Y CAMBIAR NUMBER POR INTEGER
    }

    private boolean tipoDato(int i){
        return evaluar(i, Token.INT) || evaluar(i, Token.FLOAT) || evaluar(i, Token.DOUBLE)
                || evaluar(i, Token.CHAR) || evaluar(i, Token.STRING) || evaluar(i, Token.BOOLEAN)
                || evaluar(i, Token.LONG) || evaluar(i, Token.SHORT) || evaluar(i, Token.BYTE);
    }

    private boolean tipoRetorno(int i){
        return evaluar(i, Token.VOID) || tipoDato(i);
    }

    public boolean operadorAritmetico(int i){
        return evaluar(i, Token.PLUS) || evaluar(i, Token.MINUS) || evaluar(i, Token.TIMES) || evaluar(i, Token.DIVIDE)  || evaluar(i, Token.MODULO);
    }

    public boolean operadorLogico(int i){
        return evaluar(i, Token.AND) || evaluar(i, Token.OR) || evaluar(i, Token.NOT);
    }

    public boolean operadorRelacional(int i){
        return evaluar(i, Token.LESS) || evaluar(i, Token.LESS_EQUALS) || evaluar(i, Token.GREATER) || evaluar(i, Token.GREATER_EQUALS)
                || evaluar(i, Token.EQUALS) || evaluar(i, Token.NOT_EQUALS);
    }

    public boolean operadorAsignacion(int i){
        return evaluar(i, Token.ASSIGNMENT) || evaluar(i, Token.ADDITION_ASSIGNMENT) || evaluar(i, Token.SUBTRACTION_ASSIGNMENT)
                || evaluar(i, Token.MULTIPLICATION_ASSIGNMENT) || evaluar(i, Token.DIVISION_ASSIGNMENT) || evaluar(i, Token.MODULO_ASSIGNMENT);
    }

    public boolean operadorIncremento(int i){
        return evaluar(i, Token.INCREMENT) || evaluar(i, Token.DECREMENT);
    }


    private boolean valorBooleano(int i) {
        return evaluar(i, Token.TRUE) || evaluar(i, Token.FALSE);
    }

    private boolean evaluar(int i, Token token) {
        return i < tokens.size() && tokens.get(i).equals(token);
    }
}