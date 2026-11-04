package tienda.servicio;
import tienda.modelo.*;
import java.math.BigDecimal;
/** Contratos como parámetros: el motor no conoce proveedores concretos. */
public final class MotorCheckout {
 public Resultado cobrar(Transaccional api,BigDecimal monto,String referencia){return api.cobrar(monto,referencia);}
 public Resultado cobrar(Transaccional api,String monto,String referencia){try{return cobrar(api,new BigDecimal(monto),referencia);}catch(NumberFormatException|NullPointerException e){return Resultado.error("Monto numérico inválido");}}
 public Resultado reintegrar(Transaccional api,BigDecimal monto,String referencia){return api.reintegrar(monto,referencia);}
 public Resultado emitirDocumento(Documentable api,String referencia){return api.generarDocumento(referencia);}
 public Resultado consultar(Consultable api,String referencia){return api.consultarEstado(referencia);}
 public Resultado consultar(Consultable api){return consultar(api,"ESTADO-GENERAL");}
}
