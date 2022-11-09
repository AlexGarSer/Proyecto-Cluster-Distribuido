package servidor;

import servidorControlador.ControladorServidor;
import servidorModelo.ModeloServidor;

public class LanzadorServidor extends Thread
{
	@Override
	public void run()
	{
		System.out.println("Se lanzó el Servidor");
		VistaServidor vista = new VistaServidor();
		ControladorServidor controlador = new ControladorServidor(vista);
	}
}
