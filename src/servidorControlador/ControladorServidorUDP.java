package servidorControlador;

import java.net.DatagramSocket;
import java.net.SocketException;
import servidor.LanzadorServidor;

public class ControladorServidorUDP 
{
    final int PUERTO = 50000;
    public static byte[] buffer = new byte[1024];
    public static DatagramSocket socketUDP;
    
	public ControladorServidorUDP()
	{
		ejecutar();
	}

	private void ejecutar()
	{
		System.out.println("Controlador-Servidor UDP");
		try {
			socketUDP = new DatagramSocket(PUERTO);

			LanzadorServidor LS = new LanzadorServidor();
			LS.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
