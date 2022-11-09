package lanzador;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import cliente.LanzadorCliente;
import servidor.LanzadorServidorUDP;

public class Buscador {

	public static InetAddress ip_server;
	public static String ipserver;
	//public static LanzadorCliente cli = new LanzadorCliente();

	public static void main(String args[])
	{
		Buscador b = new Buscador();
		b.buscarServidor();
	}
	
	public void buscarServidor()
	{
		boolean serverOnLine = false;
		int puerto = 50000;
		int timeout = 600;
		InetAddress ip;
		SocketAddress direccionf = null;
		Socket s = null;
		System.out.println("Inicia las variables");
		
		int i=0;
		
		while(i<255) {
			
			s = new Socket();
			
			String j = "192.168.10."+i;
			
			try {
				ip = InetAddress.getByName(j);
				System.out.println("Arranca el socket: " + j +" = "+ip);
				direccionf = new InetSocketAddress(ip,puerto);
				System.out.println("Inicia el tiempo de conexion...");
				s.connect(direccionf,timeout);
				System.out.println("Conexion completada");
				serverOnLine = true;
				break;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					System.out.println("Tiempo Excedido...");
					s.close();
					i++;
					continue;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					i++;
					continue;
				}
			}				
		}
		
		if(serverOnLine) // Si hay un servidor en linea -> Lanza el cliente
		{
			System.out.println("Creando peticion");
			
			ip_server = s.getInetAddress();
			System.out.println(ip_server);
			ipserver = ip_server.toString();
			ipserver = ipserver.substring(1);
			/* Lanza la comunicacion TCP */
			
			System.out.println(ipserver);
			
			LanzadorCliente cli = new LanzadorCliente();
			cli.lanzarCliente(ipserver);
		}
		else // Si no, lanza el servidor
		{
			LanzadorServidorUDP lanzarServidor = new LanzadorServidorUDP();
			lanzarServidor.lanzar();
		}		
	}
}
