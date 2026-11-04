package tienda.modelo;
public final class ProveedorLogistico extends IntegracionApi implements Consultable,Documentable {
 public ProveedorLogistico(String id,String proveedor,String endpoint,double ms,EstadoConexion estado){super(id,proveedor,endpoint,ms,estado);}
 @Override public String getCategoria(){return "LOGÍSTICA";}
 @Override public Resultado aceptar(OperacionIntegracion operacion){return operacion.ejecutar(this);}
 @Override public Resultado consultarEstado(String ref){if(!disponible())return Resultado.error("API logística desconectada");if(ref==null||ref.isBlank())return Resultado.error("Referencia requerida");registrarEvento("RASTREO "+ref);return Resultado.ok("Rastreo consultado: "+ref+" (simulación local)");}
 @Override public Resultado generarDocumento(String ref){if(!disponible())return Resultado.error("API logística desconectada");if(ref==null||ref.isBlank())return Resultado.error("Referencia requerida");registrarEvento("GUÍA "+ref);return Resultado.ok("Guía de envío generada: GUIA-"+ref);}
}
