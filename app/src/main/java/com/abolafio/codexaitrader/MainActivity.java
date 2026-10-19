package com.abolafio.codexaitrader;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
  final String[] symbols={"aapl.us","msft.us","spy.us","qqq.us","voo.us"};
  final String[] names={"Apple","Microsoft","SPY ETF","QQQ ETF","VOO ETF"};
  double[] prices=new double[5]; String[] dates=new String[5];
  double cash=100,shares[]=new double[5];
  int mode=0; boolean halt=false; long lastOrder=0;
  LinearLayout layout; TextView summary,market,status; SharedPreferences prefs;
  @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("portfolio",0);load();draw();refresh();}
  TextView label(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setPadding(12,10,12,10);return t;}
  Button button(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setOnClickListener(v->r.run());return b;}
  void draw(){ScrollView sc=new ScrollView(this);layout=new LinearLayout(this);layout.setOrientation(1);layout.setPadding(20,22,20,24);sc.addView(layout);setContentView(sc);
    layout.addView(label("CODEX AI TRADER · PAPER",23));
    layout.addView(label("Dinero 100% virtual · Precios diarios de mercado, no tiempo real",13));
    summary=label("",20);layout.addView(summary);
    status=label("",14);layout.addView(status);
    layout.addView(button("Actualizar precios reales",()->refresh()));
    layout.addView(button("Modo: "+new String[]{"MANUAL","ASISTIDO","AUTOMÁTICO"}[mode]+" · cambiar",()->{String[] modes={"Manual","Asistido","Automático"};new AlertDialog.Builder(this).setTitle("Elegir modo").setSingleChoiceItems(modes,mode,(dialog,which)->{mode=which;save();draw();dialog.dismiss();}).setNegativeButton("Cancelar",null).show();}));
    layout.addView(button(halt?"REANUDAR OPERACIONES":"PARADA DE EMERGENCIA",()->{halt=!halt;save();draw();}));
    market=label("Cargando...",16);layout.addView(market);
    layout.addView(button("Analizar oportunidades / operar",()->analyze()));
    layout.addView(button("Estado de las bolsas",()->showHours()));
    layout.addView(button("Aprender: velas japonesas",()->showLesson()));
    layout.addView(button("Guía de riesgos",()->showRiskLesson()));
    layout.addView(button("Resumen de cartera",()->showPortfolio()));
    layout.addView(button("Configurar capital inicial / nueva partida",()->chooseCapital()));
    layout.addView(button("Reiniciar simulación",()->confirmReset()));
    layout.addView(button("Ver historial",()->new AlertDialog.Builder(this).setTitle("Operaciones virtuales").setMessage(prefs.getString("log","Sin operaciones")).setPositiveButton("Cerrar",null).show()));
    layout.addView(label("AVISO: órdenes simuladas bloqueadas hasta integrar cotizaciones en directo autorizadas y cambio EUR/USD.\nPrototipo: sin IA generativa ni ejecución en segundo plano. Estrategia demostrativa determinista. Fuente: Stooq, precios de cierre potencialmente retrasados, denominados en USD; conversión EUR/USD no incluida.",12));render();}
  void render(){double invested=0;StringBuilder sb=new StringBuilder("\nACTIVOS (precio cierre USD)\n");for(int i=0;i<5;i++){if(prices[i]>0)invested+=shares[i]*prices[i];sb.append(names[i]).append(" · ").append(prices[i]>0?String.format(Locale.US,"%.2f USD",prices[i]):"sin datos").append(" · ").append(dates[i]==null?"":dates[i]).append("\nTenencia: ").append(String.format(Locale.US,"%.4f",shares[i])).append("\n\n");}
    summary.setText(String.format(Locale.US,"Efectivo: %.2f unidades\nPosiciones: %.2f USD\nTotal indicativo: %.2f*",cash,invested,cash+invested));
    status.setText(new String[]{"MANUAL","ASISTIDO","AUTOMÁTICO"}[mode]+(halt?" · DETENIDO":" · HABILITADO")+"\n*Cotizaciones en USD. Capital elegido en EUR; sin FX integrado: operaciones bloqueadas.");market.setText(sb.toString());}
  void refresh(){status.setText("Consultando precios diarios...");new Thread(()->{double[] p=new double[5];String[] d=new String[5];for(int i=0;i<5;i++){try{URL url=new URL("https://stooq.com/q/d/l/?s="+symbols[i]+"&i=d&d1=20261001");HttpURLConnection c=(HttpURLConnection)url.openConnection();c.setConnectTimeout(8000);c.setReadTimeout(8000);c.setRequestProperty("User-Agent","Mozilla/5.0");try(BufferedReader br=new BufferedReader(new InputStreamReader(c.getInputStream()))){String line,latest=null;while((line=br.readLine())!=null){if(line.matches("\\d{4}-\\d{2}-\\d{2},.*"))latest=line;}if(latest!=null){String[] fields=latest.split(",");p[i]=Double.parseDouble(fields[4]);d[i]=fields[0];}}c.disconnect();}catch(Exception ignored){}}
    runOnUiThread(()->{int n=0;for(int i=0;i<5;i++){if(p[i]>0){prices[i]=p[i];dates[i]=d[i];n++;}}render();if(n==0)status.setText("No se pudieron cargar cotizaciones. Comprueba la conexión o el proveedor.");});}).start();}
  void analyze(){if(halt){toast("Operaciones detenidas");return;}int best=-1;double lowest=Double.MAX_VALUE;for(int i=0;i<5;i++)if(prices[i]>0&&shares[i]==0&&prices[i]<lowest){best=i;lowest=prices[i];}
    if(best<0){toast("No hay señales disponibles. Actualiza precios.");return;}
    final int ix=best;final double amount=Math.min(10,cash-30);
    if(amount<=0){toast("Reserva mínima alcanzada");return;}
    if(System.currentTimeMillis()-lastOrder<60000){toast("Espera entre operaciones");return;}
    if(mode==2){toast("Operaciones automáticas bloqueadas: cotizaciones diarias, no tiempo real");}else{new AlertDialog.Builder(this).setTitle("Propuesta virtual").setMessage("Comprar "+names[ix]+" por "+String.format(Locale.US,"%.2f unidades virtuales",amount)+" a precio de cierre "+prices[ix]+" USD. La orden no se envía a ningún bróker.").setNegativeButton("Rechazar",null).setPositiveButton("Aprobar",(d,w)->trade(ix,amount)).show();}}
  void trade(int i,double amount){toast("Orden no ejecutada: falta fuente de precios en tiempo real autorizada");if(!liveFeedAvailable())return; if(halt||prices[i]<=0||amount>10||cash-amount<30)return;double fee=0.05;double spend=amount-fee;if(spend<=0)return;shares[i]+=spend/prices[i];cash-=amount;lastOrder=System.currentTimeMillis();String old=prefs.getString("log","");prefs.edit().putString("log",new Date()+" COMPRA "+names[i]+" "+String.format(Locale.US,"%.2f",amount)+" USD (comisión simulada 0.05)\n"+old).apply();save();render();toast("Compra virtual registrada");}
  boolean liveFeedAvailable(){return false; /* No licensed real-time feed configured yet. */}
  void showHours(){new AlertDialog.Builder(this).setTitle("Horarios habituales (no estado en directo)").setMessage("Madrid: 09:00–17:30, hora de Madrid.\nNYSE / Nasdaq: 09:30–16:00, hora de Nueva York.\nLos festivos, cambios de hora y sesiones especiales pueden alterar estos horarios. Estado en directo pendiente de integrar.").setPositiveButton("Cerrar",null).show();}
  void showRiskLesson(){new AlertDialog.Builder(this).setTitle("Gestión de riesgos").setMessage("Diversifica: no concentres todo en una empresa.\n\nStop loss: nivel al que intentarías limitar una pérdida; no garantiza ejecución exacta.\n\nTake profit: objetivo de venta con ganancias; no asegura beneficios.\n\nSpread: diferencia entre compra y venta. Comisiones y divisas también afectan al resultado.\n\nLos resultados pasados no predicen los futuros.").setPositiveButton("Entendido",null).show();}
  void showPortfolio(){StringBuilder b=new StringBuilder();b.append(String.format(Locale.US,"Efectivo virtual: %.2f EUR\\n",cash));for(int i=0;i<shares.length;i++){if(shares[i]>0)b.append(names[i]).append(": ").append(String.format(Locale.US,"%.5f títulos",shares[i])).append("\\n");}b.append("\\nLa valoración de posiciones no es fiable sin cotizaciones en directo y conversión EUR/USD.");new AlertDialog.Builder(this).setTitle("Resumen de cartera").setMessage(b.toString()).setPositiveButton("Cerrar",null).show();}
  void showLesson(){new AlertDialog.Builder(this).setTitle("Aprender: velas japonesas").setMessage("Una vela representa apertura, máximo, mínimo y cierre (OHLC) de un intervalo. El cuerpo muestra la diferencia entre apertura y cierre; las mechas muestran los extremos. Una vela alcista no garantiza nuevas subidas. Los gráficos intradía requieren datos autorizados que aún no están integrados.").setPositiveButton("Entendido",null).show();}
  void chooseCapital(){EditText input=new EditText(this);input.setInputType(8194);input.setHint("Ejemplo: 100");new AlertDialog.Builder(this).setTitle("Nueva simulación · capital virtual").setMessage("Introduce el capital inicial en EUR. Se borrará la cartera y el historial anteriores. Las cotizaciones actuales están en USD y todavía no existe conversión FX, por lo que las operaciones permanecerán bloqueadas.").setView(input).setNegativeButton("Cancelar",null).setPositiveButton("Continuar",(d,w)->{try{double v=Double.parseDouble(input.getText().toString().replace(",","."));if(!Double.isFinite(v)||v<1||v>100000000){toast("Importe no válido");return;}confirmResetAmount(v);}catch(Exception e){toast("Introduce un importe válido");}}).show();}
  void confirmResetAmount(double amount){new AlertDialog.Builder(this).setTitle("Confirmar reinicio").setMessage("¿Borrar toda la cartera y comenzar con "+amount+" EUR virtuales? No se puede deshacer.").setNegativeButton("Cancelar",null).setPositiveButton("Reiniciar",(d,w)->{prefs.edit().clear().apply();cash=amount;Arrays.fill(shares,0);mode=0;halt=false;lastOrder=0;save();draw();toast("Nueva simulación creada");}).show();}
  void confirmReset(){chooseCapital();}
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
  void load(){cash=Double.longBitsToDouble(prefs.getLong("cash",Double.doubleToLongBits(100)));mode=prefs.getInt("mode",0);halt=prefs.getBoolean("halt",false);for(int i=0;i<5;i++)shares[i]=Double.longBitsToDouble(prefs.getLong("s"+i,Double.doubleToLongBits(0)));}
  void save(){SharedPreferences.Editor e=prefs.edit().putLong("cash",Double.doubleToLongBits(cash)).putInt("mode",mode).putBoolean("halt",halt);for(int i=0;i<5;i++)e.putLong("s"+i,Double.doubleToLongBits(shares[i]));e.apply();}
}
