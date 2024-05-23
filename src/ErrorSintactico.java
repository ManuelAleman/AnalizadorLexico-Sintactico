import java.util.ArrayList;

public class ErrorSintactico {
    private String tokenEsperado;
    private int indiceTokenEsperado;

    public ErrorSintactico(String tokenEsperado, int indiceTokenEsperado) {
        this.tokenEsperado = tokenEsperado;
        this.indiceTokenEsperado = indiceTokenEsperado;
    }

    public String getMensajeError(ArrayList<Par> tokens) {
        String error;
        if (indiceTokenEsperado < tokens.size()) {
            error = "Error en la linea " + (tokens.get(indiceTokenEsperado).getPosicion().getFila());
            error += " y columna " + (tokens.get(indiceTokenEsperado).getPosicion().getColumna()) + "\n\t";
            error += "Se esperaba un " + tokenEsperado + " pero se encontro " + tokens.get(indiceTokenEsperado).getToken();
            return error;
        }
        error = "\tSe esperaba un " + tokenEsperado + " pero se encontro el fin del archivo";
        return error;
    }
}
