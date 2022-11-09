package servidorControlador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import beans.InfoPC;
import cliente.LanzadorCliente;
import servidor.Difucion;
import servidor.LanzadorServidorUDP;
import servidor.VistaServidor;
import servidorModelo.ModeloServidor;
import servidorModelo.RecibeClientes;

public class ControladorServidor extends Thread
{
	public static ArrayList<InfoPC> infoPCs = new ArrayList<InfoPC>();
	
	private static VistaServidor V;
	private static ServerSocket serverSocket;
	
	public ControladorServidor() {}
	public ControladorServidor(VistaServidor V)
	{
		ControladorServidor.V = V;
		ejecutar();
	}
	
	private void ejecutar()
	{
		V.lanzarVista(); // Lanza la vista del servidor
		while(!V.vistaCreada)
		{
			try 
			{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		DefaultTableModel modelo = (DefaultTableModel) V.table.getModel();
		ModeloServidor infoServidor = new ModeloServidor(modelo);
		infoServidor.start();
		
		server();
		ControladorServidor hilo = new ControladorServidor();
		hilo.start();
		escuchar();
	}
	
	private void server()
	{
		try {
			ControladorServidor.serverSocket = new ServerSocket(5432);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void escuchar()
	{
		// Mientras el servidor siga en linea, todo bien
		while(ModeloServidor.serverOnLine)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Cuano ya no esté en línea, cierra el socket y se convierte en cliente
		// Esto sucede cuando el servidor se encuentra con menos del 10% libre de cpu
		try {
			Thread.sleep(25000);
			ControladorServidor.serverSocket.close();
			ControladorServidor.V.dispose();
			LanzadorCliente LC = new LanzadorCliente();
			LC.lanzarCliente(RecibeClientes.top1);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		// Hilo que se pone a la escucha de los clientes que se conectan
		System.out.println("Arrancó hilo controlador servidor");
		while(ModeloServidor.serverOnLine)
		{
			try {
				System.out.println("...Esperando conexión");
				Socket cliente = ControladorServidor.serverSocket.accept();
				System.out.println("Se ha conectado un cliente "+cliente.getInetAddress().toString());
				DefaultTableModel modelo = (DefaultTableModel) ControladorServidor.V.table.getModel();
				new RecibeClientes(cliente, modelo).start(); // Lanza un hilo para recibir al cliente
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
