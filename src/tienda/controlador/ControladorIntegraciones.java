package tienda.controlador;
import tienda.modelo.*;
import tienda.servicio.*;
import java.math.BigDecimal;
import java.util.List;
/** Controlador MVC: valida flujo y delega al modelo y a los servicios. */
public final class ControladorIntegraciones {
 private final CatalogoIntegraciones catalogo;
 private final MotorCheckout motor=new MotorCheckout();
 public ControladorIntegraciones(CatalogoIntegraciones catalogo){this.catalogo=catalogo;}
 public List<IntegracionApi> listar(){return catalogo.listar();}
 public IntegracionApi buscar(String id){return catalogo.buscar(id);}
 public void ordenar(){catalogo.ordenarPorLatencia();}
 public Resultado ejecutar(String id,OperacionSolicitada tipo,String monto,String referencia){
  IntegracionApi api=catalogo.buscar(id);
  if(api==null)return Resultado.error("No existe la integración: "+id);
  if(tipo==null)return Resultado.error("Operación obligatoria");
  if(referencia==null||referencia.isBlank())return Resultado.error("Referencia obligatoria");
  BigDecimal valor=BigDecimal.ZERO;
  if(tipo==OperacionSolicitada.COBRO||tipo==OperacionSolicitada.REINTEGRO){
   try{valor=new BigDecimal(monto);}catch(NumberFormatException|NullPointerException e){return Resultado.error("Monto numérico inválido");}
   if(valor.signum()<=0||valor.scale()>2)return Resultado.error("Monto positivo con máximo 2 decimales");
  }
  return api.aceptar(new DespachadorOperaciones(motor,tipo,valor,referencia.trim()));
 }
 public List<String> auditoria(String id){IntegracionApi api=buscar(id);return api==null?List.of():api.obtenerAuditoria();}
}
