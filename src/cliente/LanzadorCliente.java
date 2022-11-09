package cliente;

import java.io.IOException;

import clienteControlador.ControladorCliente;
import clienteModelo.ModeloCliente;

// Clase para lanzar al cliente
public class LanzadorCliente
{	
	public void lanzarCliente(String ipserver)
	{
		System.out.println("Se lanzó el cliente");
		ModeloCliente modelo = new ModeloCliente();
		VistaCliente vista = new VistaCliente();
		ControladorCliente controlador = new ControladorCliente(modelo, vista, ipserver);
		System.out.println("Ha cerrado el cliente....");
	}
	
	public void bidireccionamiento(String iptop) throws IOException {
		System.out.println("Se lanzó el cliente");
		ModeloCliente modelo = new ModeloCliente();
		VistaCliente vista = new VistaCliente();
		ControladorCliente controlador = new ControladorCliente(modelo, vista, iptop);
	}
}
