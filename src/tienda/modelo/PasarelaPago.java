package tienda.modelo;
import java.math.BigDecimal;
public final class PasarelaPago extends IntegracionApi implements Transaccional, Consultable {
 public PasarelaPago(String id,String proveedor,String endpoint,double ms,EstadoConexion estado){super(id,proveedor,endpoint,ms,estado);}
 @Override public String getCategoria(){return "PAGO";}
 @Override public Resultado aceptar(OperacionIntegracion operacion){return operacion.ejecutar(this);}
 private Resultado validar(BigDecimal monto,String ref){
  if(!disponible())return Resultado.error("API de pago desconectada");
  if(monto==null||monto.signum()<=0||monto.scale()>2||ref==null||ref.isBlank())return Resultado.error("Monto positivo (máximo 2 decimales) y referencia obligatoria");
  return null;
 }
 @Override public Resultado cobrar(BigDecimal monto,String ref){Resultado error=validar(monto,ref);if(error!=null)return error;registrarEvento("COBRO "+monto+" ref="+ref);return Resultado.ok("Cobro autorizado por "+getProveedor()+" de Q"+monto.toPlainString());}
 @Override public Resultado reintegrar(BigDecimal monto,String ref){Resultado error=validar(monto,ref);if(error!=null)return error;registrarEvento("REINTEGRO "+monto+" ref="+ref);return Resultado.ok("Reintegro aceptado por "+getProveedor()+" de Q"+monto.toPlainString());}
 @Override public Resultado consultarEstado(String ref){if(!disponible())return Resultado.error("API de pago desconectada");if(ref==null||ref.isBlank())return Resultado.error("Referencia requerida");registrarEvento("CONSULTA "+ref);return Resultado.ok("Operación "+ref+": recibida para consulta en "+getProveedor());}
}
