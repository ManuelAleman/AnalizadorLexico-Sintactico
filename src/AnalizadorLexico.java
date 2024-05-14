import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AnalizadorLexico {
    private static HashMap<String, Token> mapaTokens;

    public AnalizadorLexico() {
        this.mapaTokens = new HashMap<>();

        mapaTokens.put("class", Token.CLASS);
        mapaTokens.put("extends", Token.EXTENDS);
        mapaTokens.put("implements", Token.IMPLEMENTS);
        mapaTokens.put("final", Token.FINAL);

        // Modificadores de acceso
        mapaTokens.put("public", Token.PUBLIC);
        mapaTokens.put("private", Token.PRIVATE);
        mapaTokens.put("protected", Token.PROTECTED);

        // Bucles
        mapaTokens.put("for", Token.FOR);
        mapaTokens.put("while", Token.WHILE);
        mapaTokens.put("do", Token.DO);

        // Control de flujo
        mapaTokens.put("if", Token.IF);
        mapaTokens.put("else", Token.ELSE);
        mapaTokens.put("switch", Token.SWITCH);
        mapaTokens.put("case", Token.CASE);
        mapaTokens.put("default", Token.DEFAULT);
        mapaTokens.put("break", Token.BREAK);

        // Caracteres especiales
        mapaTokens.put("(", Token.LEFT_PARENTHESIS);
        mapaTokens.put(")", Token.RIGHT_PARENTHESIS);
        mapaTokens.put("{", Token.LEFT_CURLY_BRACE);
        mapaTokens.put("}", Token.RIGHT_CURLY_BRACE);
        mapaTokens.put(",", Token.COMMA);
        mapaTokens.put(";", Token.SEMICOLON);
        mapaTokens.put(":", Token.COLON);
        mapaTokens.put("\"", Token.QUOTATION_MARKS);

        // Operadores aritmeticos
        mapaTokens.put("+", Token.PLUS);
        mapaTokens.put("-", Token.MINUS);
        mapaTokens.put("*", Token.TIMES);
        mapaTokens.put("/", Token.DIVIDE);
        mapaTokens.put("%", Token.MODULO);

        // Operadores logicos
        mapaTokens.put("&&", Token.AND);
        mapaTokens.put("||", Token.OR);
        mapaTokens.put("!", Token.NEGATION);

        // Operadores relacionales
        mapaTokens.put("<", Token.LESS);
        mapaTokens.put("<=", Token.LESS_EQUALS);
        mapaTokens.put(">", Token.GREATER);
        mapaTokens.put(">=", Token.GREATER_EQUALS);
        mapaTokens.put("==", Token.EQUALS);
        mapaTokens.put("!=", Token.NOT_EQUALS);

        // Operadores de incremento
        mapaTokens.put("++", Token.INCREMENT);
        mapaTokens.put("--", Token.DECREMENT);

        // Operadores de asignacion
        mapaTokens.put("=", Token.ASSIGNMENT);
        mapaTokens.put("+=", Token.ADDITION_ASSIGNMENT);
        mapaTokens.put("-=", Token.SUBTRACTION_ASSIGNMENT);
        mapaTokens.put("*=", Token.MULTIPLICATION_ASSIGNMENT);
        mapaTokens.put("/=", Token.DIVISION_ASSIGNMENT);
        mapaTokens.put("%=", Token.MODULO_ASSIGNMENT);

        // Tipos de datos
        mapaTokens.put("int", Token.INT);
        mapaTokens.put("float", Token.FLOAT);
        mapaTokens.put("double", Token.DOUBLE);
        mapaTokens.put("char", Token.CHAR);
        mapaTokens.put("string", Token.STRING);
        mapaTokens.put("boolean",Token.BOOLEAN);
        mapaTokens.put("long", Token.LONG);
        mapaTokens.put("short", Token.SHORT);
        mapaTokens.put("byte", Token.BYTE);
        mapaTokens.put("void", Token.VOID);

        mapaTokens.put("true", Token.TRUE);
        mapaTokens.put("false", Token.FALSE);

        mapaTokens.put("null", Token.NULL);
    }

    public HashMap<String, String> generarTablaDeSimbolos(String codigo) {
        HashMap<String,String> tablaSimbolos = new HashMap<>();
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("$");
        tokens.addAll(Arrays.asList(codigo.split(" ")));
        tokens.add("$");
        for(int i = 1; i <= tokens.size()-2; i++) {
            if(esIdentificador(tokens.get(i)) && !mapaTokens.containsKey(tokens.get(i))) {
                if(tokens.get(i-1).equals("class")){
                    tablaSimbolos.put(tokens.get(i), Token.CLASS.toString());
                }else if(tokens.get(i+1).equals("(")) {
                    tablaSimbolos.put(tokens.get(i), Token.METHOD.toString());
                }else {
                    tablaSimbolos.put(tokens.get(i), tokens.get(i-1));
                }
            }
        }
        return tablaSimbolos;
    }

    public ArrayList<Token> analizar(String codigo) {
        codigo = codigo.trim();
        String[] tokens = codigo.split("[ \n]+");
        for (String token : tokens)
            System.out.println(token);

        ArrayList<Token> mensajes = new ArrayList<>();
        for (String token : tokens) {
            if (mapaTokens.containsKey(token)) {
                mensajes.add(mapaTokens.get(token));
                continue;
            }
            if (esNumero(token)) {
                mensajes.add(Token.NUMBER);
                continue;
            }
            if (esIdentificador(token)) {
                mensajes.add(Token.IDENTIFIER);
                continue;
            }
            mensajes.add(Token.ERROR);
            return mensajes;
        }
        return mensajes;
    }

    public boolean esNumero(String token) {
        return token.matches("0|[1-9][0-9]{0,2}|1000");
    }

    public boolean esIdentificador(String token) {
        return token.matches("[a-zA-Z][a-zA-Z0-9]*");
    }
}
