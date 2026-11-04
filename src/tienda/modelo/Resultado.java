package tienda.modelo;
/** Resultado explícito: incluso una operación no soportada nunca causa una excepción. */
public record Resultado(boolean exitoso, String codigo, String mensaje) {
    public static Resultado ok(String mensaje){return new Resultado(true,"OK",mensaje);}
    public static Resultado omitido(String mensaje){return new Resultado(false,"NO_SOPORTADO",mensaje);}
    public static Resultado error(String mensaje){return new Resultado(false,"ERROR",mensaje);}
    @Override public String toString(){return "["+codigo+"] "+mensaje;}
}
