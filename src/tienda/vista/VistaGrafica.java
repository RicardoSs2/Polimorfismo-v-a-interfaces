package tienda.vista;
import tienda.controlador.ControladorIntegraciones;
import tienda.modelo.*;
import tienda.servicio.OperacionSolicitada;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/** Vista Swing opcional: reutiliza el controlador MVC sin replicar lógica. */
public final class VistaGrafica extends JFrame {
 private final ControladorIntegraciones ctrl;
 private final DefaultTableModel tabla=new DefaultTableModel(new String[]{"ID","Proveedor","Categoría","Estado","Latencia (ms)"},0){@Override public boolean isCellEditable(int r,int c){return false;}};
 private final JTable grid=new JTable(tabla);
 private final JTextArea resultado=new JTextArea(7,55);
 public VistaGrafica(ControladorIntegraciones ctrl){super("Gestor de Integraciones API - MVC");this.ctrl=ctrl;
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);setSize(850,570);setLocationRelativeTo(null);
  resultado.setEditable(false);resultado.setLineWrap(true);resultado.setWrapStyleWord(true);
  JPanel botones=new JPanel(new FlowLayout(FlowLayout.LEFT));
  crearBoton(botones,"Actualizar",this::actualizar);crearBoton(botones,"Buscar",this::buscar);
  crearBoton(botones,"Ordenar latencia",()->{ctrl.ordenar();actualizar();});
  crearBoton(botones,"Cobrar",()->operar(OperacionSolicitada.COBRO));
  crearBoton(botones,"Reintegrar",()->operar(OperacionSolicitada.REINTEGRO));
  crearBoton(botones,"Documento",()->operar(OperacionSolicitada.DOCUMENTO));
  crearBoton(botones,"Consultar",()->operar(OperacionSolicitada.CONSULTA));
  crearBoton(botones,"Auditoría",this::auditoria);
  add(botones,BorderLayout.NORTH);add(new JScrollPane(grid),BorderLayout.CENTER);add(new JScrollPane(resultado),BorderLayout.SOUTH);
  actualizar();}
 private void crearBoton(JPanel panel,String nombre,Runnable accion){JButton b=new JButton(nombre);b.addActionListener(e->accion.run());panel.add(b);}
 private void actualizar(){tabla.setRowCount(0);for(IntegracionApi api:ctrl.listar())tabla.addRow(new Object[]{api.getId(),api.getProveedor(),api.getCategoria(),api.getEstado(),api.getLatenciaMs()});resultado.setText("Total integraciones: "+ctrl.listar().size());}
 private String id(){int r=grid.getSelectedRow();if(r>=0)return grid.getValueAt(r,0).toString();return JOptionPane.showInputDialog(this,"Identificador de API:");}
 private void buscar(){String id=id();if(id==null)return;IntegracionApi api=ctrl.buscar(id);resultado.setText(api==null?"API no encontrada":api.configuracion());}
 private void operar(OperacionSolicitada op){String id=id();if(id==null)return;
  String monto=(op==OperacionSolicitada.COBRO||op==OperacionSolicitada.REINTEGRO)?JOptionPane.showInputDialog(this,"Monto en Q:"):"0";
  if(monto==null)return;String ref=JOptionPane.showInputDialog(this,"Referencia:");if(ref==null)return;
  resultado.setText(ctrl.ejecutar(id,op,monto,ref).toString());}
 private void auditoria(){String id=id();if(id==null)return;IntegracionApi api=ctrl.buscar(id);
  resultado.setText(api==null?"API no encontrada":ctrl.auditoria(id).isEmpty()?"0 eventos de auditoría":String.join("\n",ctrl.auditoria(id)));}
}
