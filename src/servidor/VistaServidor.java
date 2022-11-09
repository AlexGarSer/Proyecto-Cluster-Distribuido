package servidor;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class VistaServidor extends JFrame
{
	private JPanel contentPane;
	public JTable table;

	public boolean vistaCreada = false;
	public void lanzarVista()
	{
		crearVistaServidor();
		setVisible(true);
		vistaCreada = true;
	}

	private void crearVistaServidor() 
	{
		setTitle("Informaci\u00F3n de los clientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 320, 1380, 320);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 1344, 260);
		contentPane.add(scrollPane);
		
		// Datos de la tabla
		table = new JTable();
		Object[][] datos= {};
		String[] encabezados = {"No.", "Direcci\u00F3n IP", "Nombre del host", "SO", "Version", "Procesador", "Memoria RAM", "%RAM Libre", "Disco Duro", "%DD Libre", "%CPU Libre", "Ancho de Banda", "Edo. conexi\u00F3n"};
		
		// Modelo
		DefaultTableModel modelo = new DefaultTableModel(datos, encabezados);
		table.setModel(modelo);
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		scrollPane.setViewportView(table);
		
		TableColumnModel columnModel = table.getColumnModel();
	    columnModel.getColumn(0).setPreferredWidth(5);
	    columnModel.getColumn(1).setPreferredWidth(40);
	    columnModel.getColumn(2).setPreferredWidth(85);
	    columnModel.getColumn(3).setPreferredWidth(30);
	    columnModel.getColumn(4).setPreferredWidth(20);
	    columnModel.getColumn(5).setPreferredWidth(265);
	    columnModel.getColumn(6).setPreferredWidth(50);
	    columnModel.getColumn(7).setPreferredWidth(40);
	    columnModel.getColumn(8).setPreferredWidth(40);
	    columnModel.getColumn(9).setPreferredWidth(30);
	    columnModel.getColumn(10).setPreferredWidth(30);
	    columnModel.getColumn(11).setPreferredWidth(60);
	    columnModel.getColumn(12).setPreferredWidth(40);
	}
}
