package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import servidorModelo.ModeloServidor;

public class Difucion extends Thread 
{
	int puerto = 50000;
	int tiempolimite = 20000; //Tiempo maximo 20 segundos para responder
	
	/*Atributos del socket (int port, int backlog, InetAddress bindAddr) */
	ServerSocket sc;
	
	public void run()
	{
		System.out.println("Yo soy el nuevo servidor");
		try {
			sc = new ServerSocket(puerto, tiempolimite);
			sc.accept(); // Acepta cualquier conexion hacia el
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
