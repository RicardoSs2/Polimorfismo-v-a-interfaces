package tienda.modelo;
/** Visitor: doble despacho sin instanceof ni conversiones de tipo en la lógica central. */
public interface OperacionIntegracion {
 Resultado ejecutar(PasarelaPago api);
 Resultado ejecutar(ProveedorLogistico api);
 Resultado ejecutar(CertificadorFel api);
}
