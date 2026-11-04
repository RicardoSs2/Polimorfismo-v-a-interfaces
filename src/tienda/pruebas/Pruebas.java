package tienda.pruebas;
import tienda.controlador.ControladorIntegraciones;
import tienda.modelo.*;
import tienda.servicio.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import tienda.vista.VistaConsola;
/** Suite sin dependencias externas: ejecutar con java -ea tienda.pruebas.Pruebas. */
public final class Pruebas {
 private static int total=0;
 private static void verificar(boolean condicion,String descripcion){total++;if(!condicion)throw new AssertionError("FALLÓ #"+total+": "+descripcion);System.out.println("OK "+total+" - "+descripcion);}
 public static void main(String[] args){
  CatalogoIntegraciones cat=CatalogoIntegraciones.cargarEjemplo();ControladorIntegraciones c=new ControladorIntegraciones(cat);
  verificar(cat.cantidad()==10,"Carga de 10 APIs");
  verificar(cat.listar().stream().filter(a->a.getCategoria().equals("PAGO")).count()==4,"4 pasarelas");
  verificar(cat.listar().stream().filter(a->a.getCategoria().equals("LOGÍSTICA")).count()==4,"4 operadores logísticos");
  verificar(cat.listar().stream().filter(a->a.getCategoria().equals("FEL")).count()==2,"2 certificadores");
  verificar(c.buscar("pag-01")!=null,"Búsqueda insensible a mayúsculas");
  verificar(c.buscar("INEXISTENTE")==null,"ID inexistente");
  verificar(c.buscar("PAG-01").configuracion().contains("https://sim.local/visanet"),"Configuración incluye endpoint");
  verificar(c.buscar("PAG-01").configuracion().contains("CONectada".toUpperCase()),"Configuración incluye estado");
  verificar(cat.listar().stream().anyMatch(a->a.getEstado()==EstadoConexion.DESCONECTADA),"Estados desconectados visibles");
  c.ordenar();List<IntegracionApi> orden=cat.listar();boolean asc=true;for(int i=1;i<orden.size();i++)if(orden.get(i-1).getLatenciaMs()>orden.get(i).getLatenciaMs())asc=false;
  verificar(asc,"Comparable: latencia ascendente");
  verificar(orden.get(0).getId().equals("FEL-01"),"Menor latencia primero");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"125.50","C-1").exitoso(),"Cobro válido");
  verificar(c.ejecutar("PAG-02",OperacionSolicitada.REINTEGRO,"25.00","R-1").exitoso(),"Reintegro válido");
  verificar(c.ejecutar("LOG-01",OperacionSolicitada.REINTEGRO,"25.00","R-2").codigo().equals("NO_SOPORTADO"),"Logística ignora reintegros");
  verificar(c.ejecutar("FEL-01",OperacionSolicitada.COBRO,"25.00","R-3").codigo().equals("NO_SOPORTADO"),"FEL ignora cobros");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.DOCUMENTO,"0","DOC-1").codigo().equals("NO_SOPORTADO"),"Pago omite documentos");
  verificar(c.ejecutar("LOG-01",OperacionSolicitada.DOCUMENTO,"0","GUIA-1").exitoso(),"Guía de envío");
  verificar(c.ejecutar("FEL-01",OperacionSolicitada.DOCUMENTO,"0","FACT-1").exitoso(),"XML FEL");
  verificar(c.ejecutar("LOG-02",OperacionSolicitada.CONSULTA,"0","ENV-1").exitoso(),"Rastreo de paquete");
  verificar(c.ejecutar("PAG-03",OperacionSolicitada.CONSULTA,"0","TRANS-1").exitoso(),"Consulta pasarela degradada");
  verificar(c.ejecutar("LOG-03",OperacionSolicitada.CONSULTA,"0","ENV-2").codigo().equals("ERROR"),"API desconectada no consulta");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"-2","A").codigo().equals("ERROR"),"Monto negativo rechazado");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"0","A").codigo().equals("ERROR"),"Monto cero rechazado");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"2.345","A").codigo().equals("ERROR"),"Más de dos decimales rechazados");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"ABC","A").codigo().equals("ERROR"),"Monto no numérico rechazado");
  verificar(c.ejecutar("PAG-01",OperacionSolicitada.COBRO,"12","").codigo().equals("ERROR"),"Referencia vacía rechazada");
  verificar(c.ejecutar("NO-1",OperacionSolicitada.COBRO,"12","X").codigo().equals("ERROR"),"API inexistente manejada");
  verificar(!c.auditoria("PAG-01").isEmpty(),"Auditoría registra cobro");
  verificar(c.auditoria("LOG-04").size()==0,"Auditoría vacía se representa con cero");
  verificar(c.buscar("PAG-01").equals(new PasarelaPago("pag-01","Otro","https://demo",3,EstadoConexion.CONECTADA)),"equals por identificador");
  verificar(c.buscar("PAG-01").hashCode()==new PasarelaPago("pag-01","Otro","https://demo",3,EstadoConexion.CONECTADA).hashCode(),"hashCode consistente");
  boolean inmutable=false;try{cat.listar().clear();}catch(UnsupportedOperationException e){inmutable=true;}
  verificar(inmutable,"Catálogo sin exposición mutable");
  boolean dup=false;try{cat.agregar(new PasarelaPago("pag-01","Otro","https://demo",1,EstadoConexion.CONECTADA));}catch(IllegalArgumentException e){dup=true;}
  verificar(dup,"ID duplicado rechazado");
  cat.agregar(new ProveedorLogistico("LOG-05","Startup","https://sim.local/startup",23,EstadoConexion.CONECTADA));
  verificar(cat.cantidad()==11,"Nueva startup sin cambiar lógica central");
  MotorCheckout m=new MotorCheckout();Transaccional pago=new PasarelaPago("TEMP","Demo","https://demo",2,EstadoConexion.CONECTADA);
  verificar(m.cobrar(pago,"50.25","REF").exitoso(),"Overloading de cobro con String");
  verificar(m.cobrar(pago,new BigDecimal("50.25"),"REF").exitoso(),"Sobrecarga con BigDecimal");
  verificar(m.consultar((Consultable)pago).exitoso(),"Sobrecarga de consulta sin referencia");
  String menu="abc\n1\n2\nNOEXISTE\n3\n4\nPAG-01\n15\nCHK-1\n5\nLOG-01\n12\nRE-2\n6\nFEL-01\nFACT-2\n7\nLOG-01\nEN-1\n8\nLOG-04\n0\n";
  ByteArrayOutputStream buffer=new ByteArrayOutputStream();PrintStream original=System.out;
  try{System.setOut(new PrintStream(buffer));new VistaConsola(c,new Scanner(new ByteArrayInputStream(menu.getBytes(java.nio.charset.StandardCharsets.UTF_8)))).iniciar();}finally{System.setOut(original);}
  String salida=buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
  verificar(salida.contains("Opción inválida"),"Menú rechaza opción no válida");
  verificar(salida.contains("No encontrada"),"Menú informa búsqueda ausente");
  verificar(salida.contains("Ordenado por latencia"),"Menú ordena");
  verificar(salida.contains("Cobro autorizado"),"Menú cobra");
  verificar(salida.contains("NO_SOPORTADO"),"Menú no falla en reintegro incompatible");
  verificar(salida.contains("XML FEL simulado"),"Menú genera FEL");
  verificar(salida.contains("Rastreo consultado"),"Menú consulta envío");
  verificar(salida.contains("Total eventos: 0"),"Menú muestra cero en reporte");
  verificar(salida.contains("Hasta luego"),"Menú termina correctamente");
  verificar(salida.contains("Total integraciones: 11"),"Menú muestra recuento actualizado");
  System.out.println("\nTOTAL: "+total+" verificaciones correctas.");
 }
}
