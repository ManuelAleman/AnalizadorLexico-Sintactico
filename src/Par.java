public class Par {
    private Token token; 
    private Direccion direccion;
    public Par(Token token, Direccion direccion) {
        this.token = token;
        this.direccion = direccion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Token getToken() {
        return token;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
