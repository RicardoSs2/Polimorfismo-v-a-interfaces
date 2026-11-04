package tienda.servicio;
import tienda.modelo.*;
import java.math.BigDecimal;
/** Visitor: operaciones incompatibles se omiten sin convertir tipos ni propagar excepciones. */
public final class DespachadorOperaciones implements OperacionIntegracion {
 private final MotorCheckout motor;
 private final OperacionSolicitada tipo;
 private final BigDecimal monto;
 private final String referencia;
 public DespachadorOperaciones(MotorCheckout motor,OperacionSolicitada tipo,BigDecimal monto,String referencia){this.motor=motor;this.tipo=tipo;this.monto=monto;this.referencia=referencia;}
 @Override public Resultado ejecutar(PasarelaPago api){return switch(tipo){
  case COBRO -> motor.cobrar(api,monto,referencia);
  case REINTEGRO -> motor.reintegrar(api,monto,referencia);
  case CONSULTA -> motor.consultar(api,referencia);
  case DOCUMENTO -> Resultado.omitido("Una pasarela de pago no genera documentos FEL o guías");
 };}
 @Override public Resultado ejecutar(ProveedorLogistico api){return switch(tipo){
  case COBRO,REINTEGRO -> Resultado.omitido("El proveedor logístico no ejecuta operaciones financieras");
  case DOCUMENTO -> motor.emitirDocumento(api,referencia);
  case CONSULTA -> motor.consultar(api,referencia);
 };}
 @Override public Resultado ejecutar(CertificadorFel api){return switch(tipo){
  case COBRO,REINTEGRO -> Resultado.omitido("El certificador FEL no ejecuta operaciones financieras");
  case DOCUMENTO -> motor.emitirDocumento(api,referencia);
  case CONSULTA -> motor.consultar(api,referencia);
 };}
}
