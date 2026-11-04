package tienda.vista;
import tienda.controlador.ControladorIntegraciones;
import tienda.modelo.*;
import tienda.servicio.OperacionSolicitada;
import java.util.Scanner;
import java.util.List;
/** Vista MVC: entrada de consola robusta, sin cerrar System.in. */
public final class VistaConsola {
 private final ControladorIntegraciones controlador;
 private final Scanner teclado;
 public VistaConsola(ControladorIntegraciones controlador,Scanner teclado){this.controlador=controlador;this.teclado=teclado;}
 private String leer(String mensaje){System.out.print(mensaje);return teclado.hasNextLine()?teclado.nextLine().trim():null;}
 private void listar(){System.out.println("ID        | Proveedor          | Categoría   | Estado        | Latencia");
  List<IntegracionApi> lista=controlador.listar();for(IntegracionApi api:lista)System.out.println(api);
  System.out.println("Total integraciones: "+lista.size());}
 private void buscar(){String id=leer("Identificador: ");IntegracionApi api=controlador.buscar(id);System.out.println(api==null?"No encontrada":api.configuracion());}
 private void ejecutar(OperacionSolicitada op){String id=leer("Identificador: ");if(id==null)return;
  String monto=op==OperacionSolicitada.COBRO||op==OperacionSolicitada.REINTEGRO?leer("Monto en quetzales: "):"0";
  String ref=leer("Referencia: ");System.out.println(controlador.ejecutar(id,op,monto,ref));}
 private void auditoria(){String id=leer("Identificador: ");IntegracionApi api=controlador.buscar(id);
  if(api==null){System.out.println("No encontrada");return;}
  List<String> eventos=controlador.auditoria(id);if(eventos.isEmpty())System.out.println("Auditoría vacía (0 eventos)");
  else for(String e:eventos)System.out.println("- "+e);System.out.println("Total eventos: "+eventos.size());}
 public void iniciar(){boolean activo=true;while(activo){
  System.out.println("\n=== GESTOR DE INTEGRACIONES API ===");
  System.out.println("1. Listar APIs y estados\n2. Buscar y ver configuración\n3. Ordenar por latencia\n4. Simular cobro\n5. Simular reintegro\n6. Generar documento\n7. Consultar estado o rastreo\n8. Ver auditoría\n0. Salir");
  String opcion=leer("Elija una opción: ");if(opcion==null){System.out.println("Fin de entrada.");break;}
  switch(opcion){case "1" -> listar();case "2" -> buscar();case "3" -> {controlador.ordenar();System.out.println("Ordenado por latencia ascendente.");listar();}
   case "4" -> ejecutar(OperacionSolicitada.COBRO);case "5" -> ejecutar(OperacionSolicitada.REINTEGRO);
   case "6" -> ejecutar(OperacionSolicitada.DOCUMENTO);case "7" -> ejecutar(OperacionSolicitada.CONSULTA);
   case "8" -> auditoria();case "0" -> {activo=false;System.out.println("Hasta luego.");}
   default -> System.out.println("Opción inválida: escriba un número del 0 al 8.");
  }
 }}
}
