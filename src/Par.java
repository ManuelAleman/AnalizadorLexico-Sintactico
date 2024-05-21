public class Par {
    private Token token; 
    private Posicion posicion;
    public Par(Token token, Posicion posicion) {
        this.token = token;
        this.posicion = posicion;
    }

    public Posicion getDireccion() {
        return posicion;
    }

    public Token getToken() {
        return token;
    }

    public void setDireccion(Posicion posicion) {
        this.posicion = posicion;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
