package clienteModelo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import beans.InfoPC;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;


public class ModeloCliente 
{
    // Método para obtener la información de la PC
	// La variable nombre sirve al momento de querer hacer la conversión de un video
	public InfoPC obtenerInfo(boolean conexion, String ipServer, String nombre) 
	{
		// Metodo para cortar decimales
		DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
		separadoresPersonalizados.setDecimalSeparator('.');
		DecimalFormat decimal = new DecimalFormat("#.00", separadoresPersonalizados);
		
		// Obtiene la ip
		String ip = "";
		InetAddress addr;
        try {
        	// Dirección IP
        	addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        
        // Obtiene el hostname
        String command="systeminfo";
		int i = 0;
		String line = null;
		String extraerNombreDisp = null;
		try {
            Process pro = Runtime.getRuntime().exec(command); 
            BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((line = buf.readLine()) != null)
            {
            	extraerNombreDisp = line;
            	if(i == 1) break;
            	i++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        int pos = extraerNombreDisp.indexOf(":");
        String nombreDisp = extraerNombreDisp.substring(pos+1);
        String nombreDispositivo = nombreDisp.replace(" ","");
	        
        
        // Nombre del SO
        String SO = System.getProperty("os.name").toLowerCase();
        // Versión del SO
        String SOVersion = System.getProperty("os.version").toLowerCase();
        
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

        // Informacion del CPU	    
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        String procesador = processorIdentifier.getName();
        
        // Informacion de la Ram
        GlobalMemory globalMemory = hardware.getMemory();
        String ram = FormatUtil.formatBytes(globalMemory.getTotal());
        
        // % DE RAM LIBRE
        String[] libreRAM = FormatUtil.formatBytes(globalMemory.getAvailable()).split(" ");
        String[] totalRAM = FormatUtil.formatBytes(globalMemory.getTotal()).split(" ");
	    float ramLibre = Float.parseFloat(libreRAM[0]);
		float ramTotal = Float.parseFloat(totalRAM[0]);
		float porcentajeRAMLibre;
		if(ramLibre > 20) 
		{
			porcentajeRAMLibre = (ramLibre * 100)/(ramTotal*1000);
		}
		else porcentajeRAMLibre = (ramLibre * 100)/(ramTotal);
		String porRAMLibre = decimal.format(porcentajeRAMLibre) + "%"; 
		
        // Uso del Disco Duro
	    FileSystem fileSystem = operatingSystem.getFileSystem();
	    List<OSFileStore> osFileStores = fileSystem.getFileStores();
	    String espacioLibre = "", discoDuroTot = "", usoDiscoDuro = "";
	    for(OSFileStore fileStore : osFileStores) {
	    	espacioLibre = FormatUtil.formatBytes(fileStore.getFreeSpace());
	    	discoDuroTot = FormatUtil.formatBytes(fileStore.getTotalSpace());
	    	break;
	    }

	    // % DE DISCO DURO
		String[] libreDD = espacioLibre.split(" ");
		float ddLibre = Float.parseFloat(libreDD[0]);
		String[] totalDD = discoDuroTot.split(" ");
		float ddTotal = Float.parseFloat(totalDD[0]);
		float porcentajeDDLibre = (ddLibre * 100)/ddTotal;
		String porDDLibre = decimal.format(porcentajeDDLibre) + "%";
		
		line = null;
		String extraccionLinea = null;
        try {
            Process pro = Runtime.getRuntime().exec("ping "+ipServer+" -l 1000");
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream()));
            while ((line = buf.readLine()) != null) extraccionLinea = line;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        pos = extraccionLinea.indexOf("Media");
        String mediaTime = extraccionLinea.substring(pos);
        String[] texto = mediaTime.split(" = ");
        pos = texto[1].indexOf("ms");
        double mediaTiempo = Double.parseDouble(texto[1].substring(0,pos));
        double bps = 800 / ((mediaTiempo/2)/1000);
        String anchoBanda = ""+decimal.format(bps);
        
        // % CPU LIBRE
        command="wmic cpu get loadpercentage";
		line = null;
		extraccionLinea = null;
		 try {
            Process pro = Runtime.getRuntime().exec(command);
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream()));
            while ((line = buf.readLine()) != null)
            {
            	if(!line.equals("")) if(!line.equals("LoadPercentage")) extraccionLinea = line;
            }
		 } catch (Exception ex) {
            System.out.println(ex.getMessage());
		 }
		 String porCPULibre = "";
		 float cpuLibre = 0;
		 if(!extraccionLinea.isEmpty())
		 {
			 String cpuutilizada = extraccionLinea.replace(" ","");
			 if(!cpuutilizada.isEmpty())
			 {
				 cpuLibre =  100 - Float.parseFloat(cpuutilizada);
				 porCPULibre =  decimal.format(cpuLibre)+" %";
			 }
		 }
		 
		 
        // Obtiene puntaje total
        double puntaje = (porcentajeDDLibre * 5) + (porcentajeRAMLibre * 3) + (cpuLibre * 10) + (bps / 10);
	    
	    String edoConexion = "";
	    if(conexion) edoConexion = "Conectado";
	    if(!conexion) 
	    {
	    	edoConexion = "Desconectado";
	    	porRAMLibre = " - ";
	    	porDDLibre = " - ";
	    	puntaje = 0.0;
	    }
        // Guarda el bean InfoPC y lo retorna
	    // Se agrega una variable nombre que se usará cuando se quiera convertir un video
        InfoPC infoPC = new InfoPC(ip, nombreDispositivo, SO, SOVersion, procesador, ram, porRAMLibre, discoDuroTot, porDDLibre, porCPULibre, anchoBanda, edoConexion, puntaje, nombre);
        return infoPC;
	}
}
