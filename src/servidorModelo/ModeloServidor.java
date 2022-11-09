package servidorModelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import beans.InfoPC;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import servidorControlador.ControladorServidor;

public class ModeloServidor extends Thread
{
	DefaultTableModel modeloTable;
	public static InfoPC infoServer;
	public static boolean serverOnLine = true;
	
	public ModeloServidor() {}
	public ModeloServidor(DefaultTableModel modeloTable)
	{
		this.modeloTable = modeloTable;
	}
	
	// Método para obtener la información de la PC
	// La variable nombre sirve al momento de querer hacer la conversión de un video
	public void run() 
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
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream()));
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
        String procesador = processorIdentifier.getName(); //Identifier
        
        // Informacion de la Ram
        GlobalMemory globalMemory = hardware.getMemory();
        String ram = FormatUtil.formatBytes(globalMemory.getTotal());
			        
		while(serverOnLine)
		{
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

	        // % CPU LIBRE
	        command="wmic cpu get loadpercentage";
			line = null;
			String extraccionLinea = null;
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
			 if(!extraccionLinea.isEmpty())
			 {
				 String cpuutilizada = extraccionLinea.replace(" ","");
				 if(!cpuutilizada.isEmpty())
				 {
					 float cpuLibre =  100 - Float.parseFloat(cpuutilizada);
					 porCPULibre =  decimal.format(cpuLibre)+" %";
					 
			        if(cpuLibre <= 10 && ControladorServidor.infoPCs.size()>0)
			        {
			        	System.out.println("CPU <= 30: "+cpuLibre);
			        	serverOnLine = false; //Cortará la conexion del servidor
			        }
				 }
			 }
		    
	        // Guarda el bean InfoPC
	        infoServer = new InfoPC(ip, nombreDispositivo, SO, SOVersion, procesador, ram, porRAMLibre, discoDuroTot, porDDLibre, porCPULibre, " - ", "Conectado", 0, "");
	        actualizarTabla(infoServer);
		}
	}
	
	// Actualiza tabla completa
	public void actualizarTabla(InfoPC infoServ)
	{
		// Elimina todo lo que esté en la tabla
		for (int i = 0; i < modeloTable.getRowCount(); i++) 
		{
			modeloTable.removeRow(i);
			i-=1;
		}
		modeloTable.addRow(new Object[] {1, infoServ.getIp(), infoServ.getHostname(), infoServ.getNameSO(), infoServ.getVersionSO(), infoServ.getProcesador(), infoServ.getRam(), infoServ.getPorRAMLibre(), infoServ.getDiscoDuro(), infoServ.getPorDDLibre(), infoServ.getPorCPULibre(), infoServ.getAnchoBanda(), infoServ.getEstadoConexion()});
		// Lo vuelve agregar, actualizando el estado
		for(int i=0; i<ControladorServidor.infoPCs.size(); i++)
		{
			InfoPC pc = ControladorServidor.infoPCs.get(i);
			modeloTable.addRow(new Object[] {i+2, pc.getIp(), pc.getHostname(), pc.getNameSO(), pc.getVersionSO(), pc.getProcesador(), pc.getRam(), pc.getPorRAMLibre(), pc.getDiscoDuro(), pc.getPorDDLibre(), pc.getPorCPULibre(), pc.getAnchoBanda(), pc.getEstadoConexion()});
		}
	}
}
