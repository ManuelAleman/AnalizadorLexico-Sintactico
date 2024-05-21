public class Simbolo {
    private Token tipoToken, tipoDato;

    public Simbolo(Token tipoToken, Token tipoDato) {
        this.tipoToken = tipoToken;
        this.tipoDato = tipoDato;
    }

    public Token getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(Token tipoToken) {
        this.tipoToken = tipoToken;
    }

    public Token getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Token tipoDato) {
        this.tipoDato = tipoDato;
    }
}
