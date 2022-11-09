package clienteControlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import beans.InfoPC;
import cliente.LanzadorCliente;
import cliente.VistaCliente;
import clienteModelo.ModeloCliente;
import servidor.Difucion;
import servidor.LanzadorServidor;
import servidor.LanzadorServidorUDP;

public class ControladorCliente implements ActionListener
{
	private boolean conectado;
	public String ipServer;
	public String miIP;
	
	private Socket socket;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	
	public static String iptop;
	
	private ModeloCliente M;
	private VistaCliente V;
	public ControladorCliente(ModeloCliente M, VistaCliente V, String ipServer) //throws IOException
	{
		this.ipServer = ipServer;
		this.M = M;
		this.V = V;
		ejecutar();
	}
	
	private void ejecutar() //throws IOException
	{
		System.out.println("Controlador cliente: "+ipServer);
		conectado = true;
		this.V.lanzarVista();
		while(!this.V.vistaCreada)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.V.btnCerrar.addActionListener(this);
		this.V.btnConvertir.addActionListener(this);
		conexion();
		comunicacion();
	}
	
	private void conexion()
	{
		try 
		{
			System.out.println("Realizando conexion --> "+this.ipServer);
			this.socket = new Socket(this.ipServer, 5432);//5432
			System.out.println("socket");
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("oos");
			this.ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("ois");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void comunicacion() //throws IOException
	{
		try 
		{
			// Obtiene la información de la PC y se la envía automaticamente al servidor
			System.out.println("...Obteniendo info");
			InfoPC inf = this.M.obtenerInfo(true, ipServer, "");
			this.oos.writeObject(inf);
			//this.oos.writeObject(this.M.obtenerInfo(true, this.ipServer, "")); 
			
			this.miIP = inf.getIp();
			System.out.println("...Se ha enviado la info");
			String fromServer = (String) this.ois.readObject(); // Recibe mensaje de bienvenida del servidor
			System.out.println("Mensaje desde servidor: "+fromServer); // Imprime el mensaje en consola
				
			while(conectado)
			{
				InfoPC info = this.M.obtenerInfo(true, ipServer, "");
				this.oos.writeObject(info); 
				System.out.println("Tu puntaje es: "+info.puntaje);
				
				// Mensaje de quien es el Top 1 en la tabla 
				iptop = (String) this.ois.readObject();
			}
		}
					
		 catch (IOException | ClassNotFoundException e) {
			//e.printStackTrace();
			if(conectado) // Si el cliente no ha querido cerrar conexión hará lo siguiente
			{
				System.out.println("Se perdio la conexion con el servidor...");
				InetAddress addr;
		        	
				/*		Arranca este código cuando:
				 * 		- Se pierde la conexión con el servidor
				 * 		- Baja el % de cpu libre del servidor a 10%
				 * 
				 */
				
				try {
					System.out.println("---> IpTOP: "+iptop);
					addr = InetAddress.getLocalHost(); // Obtiene los datos de tu adaptador wifi
					String ip_local = addr.getHostAddress(); // Se genera tu direccion ip de maquina
					if(ip_local.equals(iptop)) // Compara tu IP con la IP que se encontraba en el Top 1 del servidor
					{
						System.out.println("Tu eres candidato para servidor");
						this.V.dispose(); // Oculta la vista
						
						Difucion d = new Difucion();
						d.start();
						
						LanzadorServidor ls = new LanzadorServidor();
						ls.run(); // lanzamos el servidor TCP
						
					}
					else {
						System.out.println("No eres el top 1 de la tabla... Reiniciando cliente.");
						this.V.dispose();
						Thread.sleep(3000);
						//Crea una nueva ventana cli
						LanzadorCliente cli = new LanzadorCliente();
						cli.bidireccionamiento(iptop);
					}
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	 	}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == this.V.btnConvertir) convertirVideo();
		if(e.getSource() == this.V.btnCerrar) cerrarConexion();
	}
	
	private void cerrarConexion()
	{
		try {
			this.oos.writeObject(this.M.obtenerInfo(false, ipServer, "")); 
			conectado = false;
			this.V.dispose();
			System.out.println("Se ha cerrado la conexión.");
			if(oos!=null) oos.close();
			if(ois!= null) ois.close();
			if(socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void convertirVideo()
	{
		String nombre = this.V.txtIntegrante.getText();
		try {
			// Envío del nombre ingresado por el cliente
			this.oos.writeObject(this.M.obtenerInfo(true, ipServer, nombre)); 
			System.out.println("Se ha enviado la petición.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
