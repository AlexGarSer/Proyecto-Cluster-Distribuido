package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import servidorControlador.ControladorServidorUDP;

public class LanzadorServidorUDP 
{
	
	public static void main(String args[])
	{
		LanzadorServidorUDP LS = new LanzadorServidorUDP();
		LS.lanzar();
	}
	
	public void lanzar()
	{
		System.out.println("Arranque manual"); //, espernado al menos un cliente
		Difucion d = new Difucion();
		d.start();
		System.out.println("Comienza el montaje...");
		
		//ModServReceiveUDP receive = new ModServReceiveUDP();
		//ModServSendUDP send = new ModServSendUDP();
		ControladorServidorUDP controlador = new ControladorServidorUDP();	//receive, send
	}
	
	
	public void run() throws IOException 
	{
		System.out.print("Arranque automatico");
		
		int clientes = 0;
		ServerSocket sc;
		int puerto = 50000;
		int tiempolimite = 20000; //Tiempo maximo 20 segundos para responder
		
		while(clientes < 6) {

			System.out.println("Yo soy el nuevo servidor");
			
			/*Atributos del socket (int port, int backlog, InetAddress bindAddr) */
			try {
				sc = new ServerSocket(puerto,tiempolimite);
				Socket skcliente = sc.accept(); // Acepta cualquier conexion hacia el
				clientes++;
				//ModServReceiveUDP receive = new ModServReceiveUDP();
				//ModServSendUDP send = new ModServSendUDP();
				ControladorServidorUDP controlador = new ControladorServidorUDP(); //receive, send
				skcliente.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
