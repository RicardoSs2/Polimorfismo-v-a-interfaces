package tienda.modelo;
import java.util.List;
public interface Auditable {
 void registrarEvento(String evento);
 List<String> obtenerAuditoria();
}
