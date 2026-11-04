package tienda.modelo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
/** Modelo base de toda API. La única colección polimórfica está en CatalogoIntegraciones. */
public abstract class IntegracionApi implements Comparable<IntegracionApi>, Auditable {
 private final String id, proveedor, endpoint;
 private final double latenciaMs;
 private EstadoConexion estado;
 private final List<String> auditoria = new ArrayList<>();
 protected IntegracionApi(String id,String proveedor,String endpoint,double latenciaMs,EstadoConexion estado){
  if(id==null||id.isBlank()||proveedor==null||proveedor.isBlank()||endpoint==null||endpoint.isBlank())
   throw new IllegalArgumentException("Identificador, proveedor y endpoint son obligatorios");
  if(!Double.isFinite(latenciaMs)||latenciaMs<0)throw new IllegalArgumentException("Latencia inválida");
  this.id=id.trim();this.proveedor=proveedor.trim();this.endpoint=endpoint.trim();this.latenciaMs=latenciaMs;
  this.estado=Objects.requireNonNull(estado,"Estado obligatorio");
 }
 public final String getId(){return id;}
 public final String getProveedor(){return proveedor;}
 public final String getEndpoint(){return endpoint;}
 public final double getLatenciaMs(){return latenciaMs;}
 public final EstadoConexion getEstado(){return estado;}
 public final void setEstado(EstadoConexion nuevo){estado=Objects.requireNonNull(nuevo);registrarEvento("Estado="+nuevo);}
 public abstract String getCategoria();
 public abstract Resultado aceptar(OperacionIntegracion operacion);
 public final String configuracion(){return String.format(Locale.US,"ID: %s%nProveedor: %s%nCategoría: %s%nEndpoint: %s%nLatencia: %.2f ms%nEstado: %s",id,proveedor,getCategoria(),endpoint,latenciaMs,estado);}
 @Override public final int compareTo(IntegracionApi otra){int c=Double.compare(latenciaMs,otra.latenciaMs);return c!=0?c:id.compareToIgnoreCase(otra.id);}
 @Override public final boolean equals(Object obj){return this==obj||(obj instanceof IntegracionApi otra && id.equalsIgnoreCase(otra.id));}
 @Override public final int hashCode(){return id.toLowerCase(Locale.ROOT).hashCode();}
 @Override public String toString(){return String.format(Locale.US,"%-9s | %-18s | %-11s | %-13s | %8.2f ms",id,proveedor,getCategoria(),estado,latenciaMs);}
 @Override public final void registrarEvento(String evento){if(evento!=null&&!evento.isBlank())auditoria.add(evento);}
 @Override public final List<String> obtenerAuditoria(){return Collections.unmodifiableList(new ArrayList<>(auditoria));}
 protected final boolean disponible(){return estado!=EstadoConexion.DESCONECTADA;}
}
