package servidorModelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;
import beans.InfoPC;
import servidorControlador.ControladorServidor;

public class RecibeClientes extends Thread 
{
	//public static ArrayList<InfoPC> infoPCs = new ArrayList<InfoPC>();
	public static String top1;
	private Socket socket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private DefaultTableModel modeloTable;
	private InfoPC infoPC;
	
	public RecibeClientes() { }
	public RecibeClientes(Socket socket, DefaultTableModel modeloTable)
	{
		System.out.println("...Recibiendo cliente");
		this.socket = socket;
		this.modeloTable = modeloTable;
		try 
		{
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		boolean conectado = true;
		leerMensaje(conectado);
	}
	
	private void leerMensaje(boolean conectado)
	{
		System.out.println("...Procesando mensaje recibido");
		try 
		{
			infoPC = (InfoPC) ois.readObject();
			
			if(!existePC()) // Si no existe el cliente, procede a guardar sus datos
			{
				ControladorServidor.infoPCs.add(infoPC);
				this.oos.writeObject("Se ha recibido correctamenta la información de su PC:)");
			}
			else // De lo contrario solo actualiza su estado a "conectado"
			{
				this.oos.writeObject("Bienvenido de nuevo!!!");
			}
			
			do
			{
				InfoPC infoPCliente = (InfoPC) ois.readObject();
				actualizarInfoPC(infoPCliente);
				reordenarTabla();
				
				/* Se enviara el top 1 de la tabla*/
				top1 = ControladorServidor.infoPCs.get(0).getIp();
				System.out.println("Actualizando datos de la tabla");
				System.out.println("TOP 1: "+top1);
				this.oos.writeObject(top1);
				
				// Preguntamos el nombre del integrante
				if(!infoPCliente.getNombre().equals(""))
				{
					switch (infoPCliente.getNombre()) 
					{
						case "andres":
							convertir(infoPCliente.getNombre());
							// Asigna el string vacío para evitar un error de código
							infoPCliente.setNombre("");
							break;
						case "fatima":
							convertir(infoPCliente.getNombre());
							infoPCliente.setNombre("");
							break;
						case "brenda":
							convertir(infoPCliente.getNombre());
							infoPCliente.setNombre("");
							break;
						case "alex":
							convertir(infoPCliente.getNombre());
							infoPCliente.setNombre("");
							break;
						case "joaquin":
							convertir(infoPCliente.getNombre());
							infoPCliente.setNombre("");
							break;
						default:
							// Asigna el string vacío para evitar un error de código
							infoPCliente.setNombre("");
							break;
					}
				}
				
				if(infoPCliente.getEstadoConexion().equals("Conectado")) conectado = true;
				if(infoPCliente.getEstadoConexion().equals("Desconectado")) conectado = false;
			} while (conectado && ModeloServidor.serverOnLine);
			System.out.println("Cliente desconectado - /"+infoPC.getIp());
			socket.close();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e);
		}
		
	}
	
	
	// Verifica que el cliente ingresado no exista para agregar nueva informacion en la tabla
	private boolean existePC()
	{
		boolean existe = false;
		
		for(int i=0; i<ControladorServidor.infoPCs.size(); i++)
		{
			InfoPC pc = ControladorServidor.infoPCs.get(i);
			if(pc.getIp().equals(infoPC.getIp()) && pc.getHostname().equals(infoPC.getHostname()) && pc.getNameSO().equals(infoPC.getNameSO()))
			{
				System.out.println("Ya existe la info de este cliente");
				existe = true;
			}
		}
		return existe;
	}
	
	private void actualizarInfoPC(InfoPC infoPCliente)
	{
		//System.out.println("Actualizando Info");
		for(int i=0; i<ControladorServidor.infoPCs.size(); i++)
		{
			InfoPC pc = ControladorServidor.infoPCs.get(i);
			if(pc.getIp().equals(infoPC.getIp()) && pc.getHostname().equals(infoPC.getHostname()) && pc.getNameSO().equals(infoPC.getNameSO()))
			{
				ControladorServidor.infoPCs.get(i).setPorRAMLibre(infoPCliente.getPorRAMLibre());
				ControladorServidor.infoPCs.get(i).setPorDDLibre(infoPCliente.getPorDDLibre());
				ControladorServidor.infoPCs.get(i).setEstadoConexion(infoPCliente.getEstadoConexion());
				ControladorServidor.infoPCs.get(i).setAnchoBanda(infoPCliente.getAnchoBanda());
				ControladorServidor.infoPCs.get(i).setPorCPULibre(infoPCliente.getPorCPULibre());
			}
		}
	}
	
	private void reordenarTabla()
	{
		Collections.sort(ControladorServidor.infoPCs, new Comparator<InfoPC>() 
		{
			@Override
			public int compare(InfoPC o1, InfoPC o2) {
				return new Double(o2.getPuntaje()).compareTo(new Double(o1.getPuntaje()));
			}
		});
	}
	
	// Método que convierte un video .mp4 a .avi según el nombre del integrante enviado usando la consola
	private void convertir(String nombre) {
		// System.out.println("Integrante " + nombre + " quiere convertir");
		// Comando para convertir el video de .mp4 a .avi y parámetros propios del programa junto con el nombre de salida
		String integrante = "&& ffmpeg -i "+nombre+".mp4 -b:a 128k -c:a aac -c:v libx264 -qscale 0 -framerate 29 -threads 2 "+nombre+".avi";
		// Uso de la consola de comandos para ejecutarlo
		String [] cmd = {"cmd","/c","start", "cmd.exe", "/K", "cd aplicacion ", integrante};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
