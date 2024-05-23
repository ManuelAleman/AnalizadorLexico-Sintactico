public class Par {
    private Token token; 
    private Posicion posicion;
    public Par(Token token, Posicion posicion) {
        this.token = token;
        this.posicion = posicion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public Token getToken() {
        return token;
    }
}
