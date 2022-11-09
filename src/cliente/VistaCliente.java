package cliente;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JButton;

public class VistaCliente extends JFrame {
	public VistaCliente() {
	}

	private JPanel contentPane;
	public JTextField txtIntegrante;
	public JButton btnCerrar;
	public JButton btnConvertir;

	public boolean vistaCreada = false;
	public void lanzarVista()
	{
		crearVista();
		setVisible(true);
		vistaCreada = true;
	}

	private void crearVista() 
	{
		setTitle("Cliente");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 302, 250);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnCerrar = new JButton("Cerrar conexi\u00F3n");
		btnCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnCerrar.setBounds(66, 31, 150, 35); //66, 81, 130, 35
		contentPane.add(btnCerrar);
		
		JLabel lbTexto = new JLabel("Nombre:");
		lbTexto.setFont(new Font("Arial", Font.PLAIN, 13));
		lbTexto.setBounds(30, 98, 226, 14);
		contentPane.add(lbTexto);
		
		JLabel lbMP4 = new JLabel(".mp4");
		lbMP4.setFont(new Font("Arial", Font.PLAIN, 13));
		lbMP4.setBounds(195, 98, 100, 14);
		contentPane.add(lbMP4);
		
		txtIntegrante = new JTextField();
		txtIntegrante.setFont(new Font("Arial", Font.PLAIN, 13));
		txtIntegrante.setBounds(90, 92, 100, 28);
		contentPane.add(txtIntegrante);
		
		btnConvertir = new JButton("Convertir video (.mp4 --> .avi)");
		btnConvertir.setFont(new Font("Arial", Font.PLAIN, 13));
		btnConvertir.setBounds(30, 130, 220, 35);
		contentPane.add(btnConvertir);

	}
}
