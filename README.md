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

## Modos intercambiables y formación gráfica (requisitos pendientes)
- Tres modos con cambio inmediato en cualquier momento: MANUAL (usuario ejecuta), ASISTIDO (propuestas explicadas con aprobación), AUTOMÁTICO (órdenes virtuales sujetas a controles de riesgo). Conservar saldo, posiciones e historial al cambiar; cancelar órdenes simuladas pendientes no ejecutadas y detener bucles automáticos al abandonar el modo automático.
- Gráficos de línea y velas japonesas OHLC, volumen, herramientas de zoom y desplazamiento, marcos 1m/5m/15m/30m/1h/1d/1w solo si la fuente ofrece datos autorizados y suficientes.
- Al tocar una vela: apertura, máximo, mínimo, cierre, cuerpo, mechas, periodo y explicación didáctica. Guías sobre tendencias, soporte/resistencia, volumen, medias móviles, RSI y MACD; indicadores no garantizan resultados.
- Identificar fuente, sello temporal, zona horaria, divisa y si los datos son en tiempo real o retrasados. No fabricar velas intradía a partir de precios de cierre ni de snapshots incompletos.
- Esta sección es especificación futura; no afirmar que esté implementada en la APK actual.

## Plan aprobado: diez mejoras y reinicio
1. Gestión de riesgos: stop-loss, take-profit, trailing stop, exposición máxima, pérdida máxima diaria.
2. Cartera avanzada: saldo disponible, posiciones, P&L, rentabilidad, diversificación geográfica y sectorial.
3. Alertas configurables de precio, tendencia, apertura y cierre de bolsas.
4. Profesor educativo con IA y explicaciones verificables; no prometer predicción fiable.
5. Backtesting con datos históricos autorizados, sin sesgo de anticipación y con costes simulados.
6. Noticias económicas y calendario de resultados, dividendos y eventos, con fuente y fecha.
7. Órdenes virtuales de mercado, límite, pendientes y ejecuciones parciales si hay datos de liquidez adecuados.
8. Escuela de trading: lecciones, ejercicios y cuestionarios.
9. Retos de inversión virtual con objetivos y métricas de riesgo.
10. Guardado persistente, exportación e importación de copias de seguridad.
11. Reiniciar simulación: botón con confirmación explícita; borrar cartera, órdenes, historial y progreso de simulación, mantener preferencias opcionalmente; permitir elegir nuevo saldo inicial en EUR. El botón básico de reset ya está en MainActivity, pero todavía usa 100 unidades y no tiene selector de capital.

Prioridad de desarrollo: fuentes gratuitas autorizadas de precios realmente en tiempo real, calendario de bolsas, divisas, motor de operaciones virtuales, saldo inicial elegible, modos intercambiables y controles de riesgo. Las funciones 1-10 son requisitos pendientes, no características implementadas.
