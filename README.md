# Codex AI Trader — Android

## Requisito de datos de mercado

Solo el saldo y las órdenes son virtuales. Las cotizaciones deben ser **reales y en tiempo real**, obtenidas de un proveedor con derechos de distribución y cobertura confirmada para cada bolsa. Cada cotización debe indicar fuente, mercado, timestamp, moneda, bid/ask y estado (en tiempo real, retrasada, cerrada, obsoleta).

**Estado:** la implementación actual es un prototipo con precios diarios de Stooq, NO cumple aún este requisito. No se deben presentar como cotizaciones en tiempo real ni usar para simular ejecuciones intradía. Antes de habilitar operaciones simuladas automáticas en tiempo real se requiere integrar un proveedor de datos en vivo, validar credenciales y cobertura, convertir EUR/USD con cotizaciones reales y comprobar antigüedad de datos, spread y comisiones.

No conecta con un bróker ni utiliza dinero real. La estrategia actual es demostrativa y no representa inteligencia artificial predictiva.

## Cobertura internacional prevista

Incluir acciones y ETF cotizados en España (BME), otros mercados europeos, Estados Unidos, Asia y otros mercados internacionales. Búsqueda por empresa, ticker, ISIN, MIC/bolsa y país. Distinguir empresas de instrumentos cotizados y resolver correctamente tickers duplicados. Mostrar zona horaria, horario, divisa y timestamp de cada cotización.

El modo de ejecución simulada solo podrá usar cotizaciones verdaderamente en tiempo real si el proveedor tiene cobertura y derechos de datos para esa bolsa; de lo contrario mostrar estado retrasado/no disponible y bloquear ejecución intradía. Conversión FX real, comisiones, spreads y liquidez simulados; 100 EUR de capital virtual. No prometer cobertura global hasta integrar y validar proveedores.

## Especificación educativa acordada (pendiente de implementación)
- Saldo inicial virtual elegible en EUR, persistente, reinicio explícito con confirmación.
- Solo acciones y ETF reales, España y otros mercados cuando exista fuente gratuita con permisos adecuados para uso de la app y datos verdaderamente en tiempo real.
- No confundir datos gratuitos, retrasados o de una sola plataforma (por ejemplo IEX) con datos consolidados de todas las bolsas.
- Catálogo filtrado por cobertura verificada y licencia; instrumentos no cubiertos se muestran como no disponibles, sin inventar precios.
- Estado de cada bolsa ABIERTO / CERRADO / PREAPERTURA / FESTIVO, próxima apertura y cierre, zona horaria IANA, calendario de festivos, horario de verano y medias sesiones.
- Mostrar timestamp, fuente, divisa, bid/ask, retraso y calidad de datos. Bloquear ejecución simulada automática si el precio es retrasado, obsoleto o el mercado está cerrado.
- Compras y ventas virtuales, modo manual asistido y automático, comisión, spread, conversión FX real, riesgo y explicación educativa.
- La APK actual aún NO cumple estas funciones; requiere implementación y proveedor de datos validado.
