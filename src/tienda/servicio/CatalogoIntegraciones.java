package tienda.servicio;
import tienda.modelo.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/** Única lista polimórfica de instancias de APIs en todo el proyecto. */
public final class CatalogoIntegraciones {
 private final List<IntegracionApi> integraciones = new ArrayList<>();
 public void agregar(IntegracionApi api){
  if(api==null)throw new IllegalArgumentException("API no puede ser nula");
  if(buscar(api.getId())!=null)throw new IllegalArgumentException("ID duplicado: "+api.getId());
  integraciones.add(api);
 }
 public IntegracionApi buscar(String id){if(id==null)return null;for(IntegracionApi api:integraciones)if(api.getId().equalsIgnoreCase(id.trim()))return api;return null;}
 public List<IntegracionApi> listar(){return Collections.unmodifiableList(integraciones);}
 public void ordenarPorLatencia(){Collections.sort(integraciones);}
 public int cantidad(){return integraciones.size();}
 public static CatalogoIntegraciones cargarEjemplo(){
  CatalogoIntegraciones c=new CatalogoIntegraciones();
  c.agregar(new PasarelaPago("PAG-01","VisaNet","https://sim.local/visanet",110.7,EstadoConexion.CONECTADA));
  c.agregar(new PasarelaPago("PAG-02","Banco GT","https://sim.local/bancogt",82.5,EstadoConexion.CONECTADA));
  c.agregar(new PasarelaPago("PAG-03","Billetera X","https://sim.local/wallet",76.0,EstadoConexion.DEGRADADA));
  c.agregar(new PasarelaPago("PAG-04","TransferPay","https://sim.local/transfers",130.0,EstadoConexion.CONECTADA));
  c.agregar(new ProveedorLogistico("LOG-01","Cargo Express","https://sim.local/cargo",95.2,EstadoConexion.CONECTADA));
  c.agregar(new ProveedorLogistico("LOG-02","Ruta Rápida","https://sim.local/ruta",64.0,EstadoConexion.CONECTADA));
  c.agregar(new ProveedorLogistico("LOG-03","Última Milla","https://sim.local/milla",165.3,EstadoConexion.DESCONECTADA));
  c.agregar(new ProveedorLogistico("LOG-04","EcoDelivery","https://sim.local/eco",99.9,EstadoConexion.CONECTADA));
  c.agregar(new CertificadorFel("FEL-01","CertiFEL","https://sim.local/certifel",58.4,EstadoConexion.CONECTADA));
  c.agregar(new CertificadorFel("FEL-02","FacturaGT","https://sim.local/facturagt",72.8,EstadoConexion.CONECTADA));
  return c;
 }
}
