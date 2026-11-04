package tienda;
import tienda.controlador.ControladorIntegraciones;
import tienda.servicio.CatalogoIntegraciones;
import tienda.vista.*;
import java.awt.GraphicsEnvironment;
import java.util.Scanner;
import javax.swing.SwingUtilities;
/** Punto de entrada; --gui abre Swing cuando existe entorno gráfico. */
public final class Main {
 public static void main(String[] args){ControladorIntegraciones ctrl=new ControladorIntegraciones(CatalogoIntegraciones.cargarEjemplo());
  if(args.length>0&&"--gui".equalsIgnoreCase(args[0])){
   if(GraphicsEnvironment.isHeadless()){System.err.println("No hay entorno gráfico; ejecute sin --gui.");return;}
   SwingUtilities.invokeLater(()->new VistaGrafica(ctrl).setVisible(true));
  }else new VistaConsola(ctrl,new Scanner(System.in)).iniciar();
 }
}
