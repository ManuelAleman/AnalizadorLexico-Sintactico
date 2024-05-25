import java.util.ArrayList;

public class ErrorSintactico {
    private String tokenEsperado;
    private int indiceTokenEsperado;

    public ErrorSintactico(String tokenEsperado, int indiceTokenEsperado) {
        this.tokenEsperado = tokenEsperado;
        this.indiceTokenEsperado = indiceTokenEsperado;
    }

    public String getMensajeError(ArrayList<Par> tokens, String codigo) {
        String error;
        Posicion posicion = tokens.get(indiceTokenEsperado).getPosicion();
        if (indiceTokenEsperado < tokens.size()) {
            error = "Error en la linea " + posicion.getFila();
            error += " y columna " + posicion.getColumna() + "\n";
            error += codigo.split("\n")[posicion.getFila() - 1] + "\n";
            error += " ".repeat(posicion.getColumna() - 1) + "^\n";
            error += " ".repeat(posicion.getColumna() - 1) + "Error\n";
            error += "Se esperaba un " + tokenEsperado + " pero se encontro " + tokens.get(indiceTokenEsperado).getToken();
            return error;
        }
        error = "\tSe esperaba un " + tokenEsperado + " pero se encontro el fin del archivo";
        return error;
    }
}
