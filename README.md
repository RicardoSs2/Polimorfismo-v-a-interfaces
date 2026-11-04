# Ejercicio 6 — Polimorfismo mediante interfaces

Proyecto en Java que simula la administración de integraciones externas de una tienda en línea. Incluye pasarelas de pago, proveedores de logística y certificadores FEL. Permite consultar integraciones y ejecutar operaciones según los contratos de cada servicio.

## Requisitos

- Java JDK 17 o superior.
- Una terminal para ejecutar la versión de consola.
- Para la interfaz gráfica, un equipo con entorno de escritorio.

No requiere bibliotecas adicionales ni conexión a internet.

## Ejecución

Descomprimir el proyecto y abrir una terminal en la carpeta `Ejercicio6_Interfaces`.

### Windows (PowerShell)

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$archivos = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $archivos
java -cp out tienda.Main
```

También se puede utilizar `ejecutar_windows.bat`.

### Linux o macOS

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out tienda.Main
```

### Interfaz gráfica

Una vez compilado el proyecto:

```bash
java -cp out tienda.Main --gui
```

## Funciones del programa

El menú de consola permite:

1. Listar las integraciones y su estado de conexión.
2. Buscar una integración por identificador y consultar su configuración.
3. Ordenar las integraciones por latencia promedio.
4. Simular un cobro con una pasarela de pago.
5. Simular un reintegro con una pasarela de pago.
6. Generar una guía de envío o un documento FEL simulado.
7. Consultar el estado de una operación o un envío.
8. Revisar los eventos de auditoría.
0. Salir.

El catálogo inicia con 10 integraciones: cuatro de pagos (`PAG-01` a `PAG-04`), cuatro logísticas (`LOG-01` a `LOG-04`) y dos FEL (`FEL-01` y `FEL-02`). `LOG-03` inicia desconectada para comprobar el manejo de errores.

Cuando se solicita una operación incompatible con el tipo de integración, el programa devuelve un resultado informativo sin detener la ejecución.

## Organización del código

- `src/tienda/modelo`: clases del dominio e interfaces `Transaccional`, `Documentable`, `Consultable` y `Auditable`.
- `src/tienda/servicio`: catálogo de integraciones, despacho de operaciones y motor de checkout.
- `src/tienda/controlador`: coordinación de las acciones solicitadas por el usuario.
- `src/tienda/vista`: interfaz de consola e interfaz gráfica Swing.
- `src/tienda/pruebas`: comprobaciones automáticas.
- `docs`: análisis, diagramas UML y resultados de las pruebas.

El catálogo utiliza una sola lista `List<IntegracionApi>` para almacenar los diferentes tipos de API. Las operaciones se aplican mediante interfaces, herencia y polimorfismo.

## Pruebas

Después de compilar, ejecutar ambos comandos:

```bash
java -cp out tienda.pruebas.Pruebas
java -cp out tienda.pruebas.PruebasAdicionales
```

Los archivos `docs/Resultados_pruebas.txt` y `docs/Resultados_pruebas_adicionales.txt` contienen los resultados de las comprobaciones. Se incluyen casos de búsqueda, ordenamiento, cobros, reintegros, generación de documentos, entradas inválidas y auditoría.

## Documentación

El análisis y el diseño UML están en `docs/Analisis_y_Diseno_Ejercicio6_SIN_PIE_DE_PAGINA.pdf`. Los diagramas también se incluyen por separado en la carpeta `docs`.

## Alcance

Es un prototipo local: las respuestas de los proveedores, los cobros, los reintegros y los documentos FEL son simulados. No se conecta a servicios externos ni emite facturas válidas ante la SAT.
