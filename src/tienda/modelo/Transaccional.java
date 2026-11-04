package tienda.modelo;
import java.math.BigDecimal;
public interface Transaccional {
 Resultado cobrar(BigDecimal monto, String referencia);
 Resultado reintegrar(BigDecimal monto, String referencia);
}
