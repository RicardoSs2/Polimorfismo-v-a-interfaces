package tienda.pruebas;

import tienda.modelo.*;
import tienda.servicio.*;
import tienda.controlador.*;
import java.math.BigDecimal;
import java.util.List;

/** Pruebas adicionales de límites, estados y contratos para revisión de rúbrica. */
public final class PruebasAdicionales {
 private static int n=0;
 private static void check(boolean ok,String nombre){n++;if(!ok)throw new AssertionError("FALLO " + n + ": " + nombre);System.out.println("OK extra "+n+" - "+nombre);}
 public static void main(String[] args){
  CatalogoIntegraciones cat=CatalogoIntegraciones.cargarEjemplo();
  ControladorIntegraciones c=new ControladorIntegraciones(cat);
  check(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"0.01","MIN").exitoso(),"Cobro mínimo permitido");
  check(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"999999.99","MAX").exitoso(),"Cobro grande con dos decimales");
  check(c.ejecutar("PAG-01",OperacionSolicitada.REINTEGRO,"0.01","RMIN").exitoso(),"Reintegro mínimo permitido");
  check(c.ejecutar("PAG-01",OperacionSolicitada.REINTEGRO,"1.234","R3").codigo().equals("ERROR"),"Reintegro con tres decimales inválido");
  check(c.ejecutar("PAG-01",OperacionSolicitada.REINTEGRO,"ABC","RABC").codigo().equals("ERROR"),"Reintegro no numérico inválido");
  check(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"  ","BLANCO").codigo().equals("ERROR"),"Monto en blanco inválido");
  check(c.ejecutar("LOG-03",OperacionSolicitada.DOCUMENTO,"0","DOC").codigo().equals("ERROR"),"No emite documento estando desconectado");
  check(c.ejecutar("LOG-01",OperacionSolicitada.DOCUMENTO,"0"," ").codigo().equals("ERROR"),"Documento sin referencia rechazado");
  check(c.ejecutar("FEL-02",OperacionSolicitada.DOCUMENTO,"0","FE-2").exitoso(),"Segundo certificador FEL operativo");
  check(c.ejecutar("LOG-02",OperacionSolicitada.COBRO,"12","X").codigo().equals("NO_SOPORTADO"),"Cobro logístico válido en formato, pero incompatible");
  check(c.ejecutar("FEL-02",OperacionSolicitada.REINTEGRO,"12","X").codigo().equals("NO_SOPORTADO"),"Reintegro FEL incompatible");
  check(c.ejecutar("PAG-01",null,"10","X").codigo().equals("ERROR"),"Operación nula rechazada");
  check(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"10",null).codigo().equals("ERROR"),"Referencia nula rechazada");
  IntegracionApi pago=c.buscar("PAG-02");pago.setEstado(EstadoConexion.DESCONECTADA);
  check(c.ejecutar("PAG-02",OperacionSolicitada.COBRO,"12","X").codigo().equals("ERROR"),"Cobro bloqueado tras desconexión");
  pago.setEstado(EstadoConexion.CONECTADA);
  check(c.ejecutar("PAG-02",OperacionSolicitada.COBRO,"12","X").exitoso(),"Cobro reanudado tras reconexión");
  boolean protectedAudit=false;try{pago.obtenerAuditoria().add("FALSO");}catch(UnsupportedOperationException ex){protectedAudit=true;}
  check(protectedAudit,"Auditoría no admite modificación externa");
  CatalogoIntegraciones tie=new CatalogoIntegraciones();
  tie.agregar(new PasarelaPago("B","b","https://b",10,EstadoConexion.CONECTADA));
  tie.agregar(new CertificadorFel("A","a","https://a",10,EstadoConexion.CONECTADA));
  tie.ordenarPorLatencia();
  check(tie.listar().get(0).getId().equals("A"),"Latencia empatada usa ID como desempate");
  check(c.buscar(" pag-01 ")!=null,"Búsqueda ignora espacios alrededor del ID");
  System.out.println("TOTAL ADICIONAL: "+n+" verificaciones correctas.");
 }
}
