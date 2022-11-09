package servidor;

import servidorControlador.ControladorServidor;
import servidorModelo.ModeloServidor;

public class LanzadorServidor extends Thread
{
	@Override
	public void run()
	{
		System.out.println("Se lanz� el Servidor");
		VistaServidor vista = new VistaServidor();
		ControladorServidor controlador = new ControladorServidor(vista);
	}
}
