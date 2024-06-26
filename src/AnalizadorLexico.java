import java.util.*;

public class AnalizadorLexico {
    private final HashMap<String, Token> mapaTokens;
    private final HashMap<String, Simbolo> tablaSimbolos;
    private final HashMap<String, ArrayList<String>> jerarquiaSimbolos;
    private ArrayList<Token> tokens;
    private ArrayList<Par> pares, errores;
    private ArrayList<String> clases, cadenas;

    public AnalizadorLexico() {
        mapaTokens = new HashMap<>();
        tablaSimbolos = new HashMap<>();
        jerarquiaSimbolos = new HashMap<>();

        mapaTokens.put("class", Token.CLASS);
        mapaTokens.put("extends", Token.EXTENDS);
        mapaTokens.put("implements", Token.IMPLEMENTS);
        mapaTokens.put("final", Token.FINAL);

        // Modificadores de acceso
        mapaTokens.put("public", Token.PUBLIC);
        mapaTokens.put("private", Token.PRIVATE);
        mapaTokens.put("protected", Token.PROTECTED);
        mapaTokens.put("static", Token.STATIC);

        // Bucles
        mapaTokens.put("for", Token.FOR);
        mapaTokens.put("while", Token.WHILE);
        mapaTokens.put("do", Token.DO);
        mapaTokens.put("continue",Token.CONTINUE);
        mapaTokens.put("return",Token.RETURN);

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
        mapaTokens.put(".", Token.DOT);

        // Operadores aritmeticos
        mapaTokens.put("+", Token.PLUS);
        mapaTokens.put("-", Token.MINUS);
        mapaTokens.put("*", Token.TIMES);
        mapaTokens.put("/", Token.DIVIDE);
        mapaTokens.put("%", Token.MODULO);

        // Operadores logicos
        mapaTokens.put("&&", Token.AND);
        mapaTokens.put("||", Token.OR);
        mapaTokens.put("!", Token.NOT);

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

    public void analizar(String codigo) {
        int filaActual = 1, columnaActual = 0;
        codigo += " ";
        tokens = new ArrayList<>();
        pares = new ArrayList<>();
        clases = new ArrayList<>();
        cadenas = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean comillasAbiertas = false;

        for (int i = 0; i < codigo.length(); i++) {
            columnaActual += codigo.charAt(i) == '\t' ? 4 : 1;
            int tipoCaracterDeSeparacion = esCaracterDeSeparacion(codigo.charAt(i));

            if (codigo.charAt(i) == '"') {
                comillasAbiertas = !comillasAbiertas;
            }

            if (tipoCaracterDeSeparacion == 0 && codigo.charAt(i) == '\n') {
                filaActual++;
                columnaActual = 0;
            }

            if (tipoCaracterDeSeparacion == -1 || (comillasAbiertas && i < codigo.length() - 1)) {
                sb.append(codigo.charAt(i));
                continue;
            }

            if (sb.isEmpty()) {
                if (tipoCaracterDeSeparacion >= 1) {
                    String tokenO = "" + codigo.charAt(i);
                    boolean agregado = esOperadorCompuesto(codigo, i, tipoCaracterDeSeparacion);
                    if (agregado) {
                        tokenO += codigo.charAt(i + 1);
                        i++;
                    }
                    guardarToken(mapaTokens.get(tokenO), filaActual, columnaActual - tokenO.length());
                    cadenas.add(tokenO);
                }
                continue;
            }

            String token = sb.toString();
            boolean tokenValido = false;
            if (mapaTokens.containsKey(token)) {
                guardarToken(mapaTokens.get(token), filaActual, columnaActual - token.length());
                tokenValido = true;
            }
            else if (esEntero(token)) {
                guardarToken(Token.INTEGER, filaActual, columnaActual - token.length());
                tokenValido = true;
            }
            else if (esReal(token)) {
                guardarToken(Token.REAL, filaActual, columnaActual - token.length());
                tokenValido = true;
            }
            else if (esIdentificador(token)) {
                guardarToken(Token.IDENTIFIER, filaActual, columnaActual - token.length());
                tokenValido = true;
            }
            else if(esCadena(token)) {
                guardarToken(Token.STRING_VALUE, filaActual, columnaActual - token.length());
                tokenValido = true;
            }

            cadenas.add(token);

            if (!tokenValido) {
                guardarToken(Token.ERROR, filaActual, columnaActual - token.length());
                // cadenas.add(sb.toString());
                sb = new StringBuilder();
            }

            if (tipoCaracterDeSeparacion >= 1) {
                String tokenO = "" + codigo.charAt(i);
                boolean agregado = esOperadorCompuesto(codigo, i, tipoCaracterDeSeparacion);
                if (agregado) {
                    tokenO += codigo.charAt(i + 1);
                    i++;
                }
                guardarToken(mapaTokens.get(tokenO), filaActual, columnaActual - tokenO.length());
                cadenas.add(tokenO);
            }

            if (tokenValido) {
                sb = new StringBuilder();
                continue;
            }
            // comillasAbiertas = false;
        }

        generarTablaDeSimbolos(cadenas);
    }

    private void generarTablaDeSimbolos(ArrayList<String> cadenas) {
        tablaSimbolos.clear();
        jerarquiaSimbolos.clear();
        jerarquiaSimbolos.put("", new ArrayList<>());
        String claseActual = "", metodoActual = "", claseMetodo = "";
        clases = new ArrayList<>();
        boolean enMetodo = false, entreParentesis = false;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) == Token.RIGHT_CURLY_BRACE) {
                enMetodo = false;
            }
            if (enMetodo) {
                if (tokens.get(i) == Token.LEFT_PARENTHESIS) {
                    entreParentesis = true;
                } else if (tokens.get(i) == Token.RIGHT_PARENTHESIS) {
                    entreParentesis = false;
                }
            }
            if (esIdentificador(tokens.get(i).toString()) && !mapaTokens.containsKey(cadenas.get(i))) {
                if (i - 1 >= 0 && tokens.get(i - 1) == Token.CLASS) {
                    claseActual = cadenas.get(i);
                    clases.add(claseActual);
                    tablaSimbolos.put(claseActual, new Simbolo(Token.CLASSNAME, Token.NONE));
                    jerarquiaSimbolos.putIfAbsent(claseActual, new ArrayList<>());
                    continue;
                }
                if (i - 1 >= 0 && i + 1 < tokens.size() && esTipoRetorno(tokens.get(i - 1)) && tokens.get(i) == Token.IDENTIFIER && tokens.get(i + 1) == Token.LEFT_PARENTHESIS) {
                    metodoActual = cadenas.get(i);
                    claseMetodo = claseActual + "." + metodoActual;
                    tablaSimbolos.put(claseMetodo, new Simbolo(Token.METHOD, tokens.get(i - 1)));
                    enMetodo = true;
                    jerarquiaSimbolos.putIfAbsent(claseMetodo, new ArrayList<>());
                    jerarquiaSimbolos.get(claseActual).add(claseMetodo);
                    continue;
                }
                if (i - 1 >= 0 && esTipoDeDato(tokens.get(i - 1))) {
                    if (enMetodo) {
                        String claseMetodoVariable = claseMetodo + "." + cadenas.get(i);
                        tablaSimbolos.put(claseMetodoVariable, new Simbolo(entreParentesis ? Token.PARAMETER : Token.VARIABLE, tokens.get(i - 1)));
                        jerarquiaSimbolos.get(claseMetodo).add(claseMetodoVariable);
                    } else {
                        String claseVariable = claseActual + "." + cadenas.get(i);
                        tablaSimbolos.put(claseVariable, new Simbolo(Token.VARIABLE, tokens.get(i - 1)));
                        jerarquiaSimbolos.get(claseActual).add(claseVariable);
                    }
                }
            }
        }
    }

    public String obtenerErrores(String codigo) {
        String[] lineas = codigo.split("\n");
        StringBuilder sb = new StringBuilder();
        for (Par par : pares) {
            if (par.getToken() != Token.ERROR) {
                continue;
            }
            sb.append("Error en la linea ").append(par.getPosicion().getFila()).append(" y columna ").append(par.getPosicion().getColumna()).append("\n");
            sb.append(lineas[par.getPosicion().getFila() - 1]).append("\n");
            sb.append(" ".repeat(par.getPosicion().getColumna() - 1)).append("^").append("\n");
            sb.append(" ".repeat(par.getPosicion().getColumna() - 1)).append("Error").append("\n\n");
        }
        return sb.toString();
    }

    private String obtenerStringJerarquia(String cadena, int nivel) {
        StringBuilder sb = new StringBuilder();
        int idxPunto = cadena.lastIndexOf(".");
        String cadenaSinPrefijo = idxPunto != -1 ? cadena.substring(idxPunto + 1) : cadena;
        sb.append(" ".repeat(nivel * 8))
                .append(cadenaSinPrefijo).append(" ")
                .append(tablaSimbolos.get(cadena).getTipoToken()).append(" ")
                .append(tablaSimbolos.get(cadena).getTipoDato() != Token.NONE ? tablaSimbolos.get(cadena).getTipoDato() : "")
                .append("\n");
        if (!jerarquiaSimbolos.containsKey(cadena))
            return sb.toString();
        for (String s : jerarquiaSimbolos.get(cadena)) {
            sb.append(obtenerStringJerarquia(s, nivel + 1));
        }
        return sb.toString();
    }

    public boolean exito() {
        for(Token token : tokens) {
            if(token == Token.ERROR) {
                return false;
            }
        }
        return true;
    }

    private boolean esTipoDeDato(Token token) {
        return token == Token.BOOLEAN || token == Token.INT || token == Token.DOUBLE || token == Token.FLOAT
                || token == Token.CHAR || token == Token.STRING || token == Token.SHORT || token == Token.LONG
                || token == Token.BYTE;
    }

    private boolean esTipoRetorno(Token token) {
        return esTipoDeDato(token) || token == Token.VOID;
    }

    private int esCaracterDeSeparacion(char c) {
        if(" \t\n".indexOf(c) != -1) {
            return 0;
        }
        if("(){},;:.".indexOf(c) != -1) {
            return 1;
        }
        if ("+-*/%<>!=".indexOf(c) != -1)
            return 2;
        if ("&|".indexOf(c) != -1)
            return 3;
        return -1;
    }

    private boolean esCadena(String token) {
        boolean flag = true;
        if(token.charAt(0) == '"' && token.charAt(token.length()-1) == '"') {
            for(int i = 1; i < token.length()-1; i++) {
                if(token.charAt(i) == '"') {
                    flag = false;
                    break;
                }
            }
        }else {
            return false;
        }
        return flag;
    }

    private boolean esEntero(String token) {
        return token.matches("\\d+");
    }

    private boolean esReal(String token) {
        return token.matches("\\d*\\.\\d+");
    }

    private boolean esIdentificador(String token) {
        return token.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    private boolean esOperadorCompuesto(String codigo, int i, int tipoCaracterDeSeparacion) {
        if (tipoCaracterDeSeparacion == 3) {
            return codigo.charAt(i + 1) == codigo.charAt(i);
        }
        if (tipoCaracterDeSeparacion == 2) {
            return ("+-".indexOf(codigo.charAt(i)) != -1 && codigo.charAt(i + 1) == codigo.charAt(i))
                    || codigo.charAt(i + 1) == '=';
        }
        return false;
    }

    public String obtenerStringTokens() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pares.size(); i++) {
            sb.append(cadenas.get(i)).append(" ");
            sb.append(pares.get(i).getToken()).append(" ");
            sb.append(pares.get(i).getPosicion().getFila()).append(" ");
            sb.append(pares.get(i).getPosicion().getColumna()).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    private void guardarToken(Token token, int fila, int columna) {
        tokens.add(token);
        pares.add(new Par(token, new Posicion(fila, columna)));
    }

    public String obtenerStringTablaSimbolos() {
        StringBuilder sb = new StringBuilder();
        for (String clase : clases) {
            sb.append(obtenerStringJerarquia(clase, 0));
        }
        return sb.toString();
    }

    public HashMap<String, Token> getMapaTokens() {
        return mapaTokens;
    }

    public HashMap<String, Simbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public ArrayList<Par> getPares() {
        return pares;
    }
}
