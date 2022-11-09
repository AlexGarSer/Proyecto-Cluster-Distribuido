package beans;

import java.io.Serializable;

public class InfoPC implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String ip;
	public String hostname;
	public String nameSO;
	public String versionSO;
	public String procesador;
	public String ram;
	public String porRAMLibre;
	public String discoDuro;
	public String porDDLibre;
	public String porCPULibre;
	public String anchoBanda;
	public String estadoConexion;
	public double puntaje;
	// Variable que se usará al momento de querer enviar un nombre para convertir un video
	public String nombre;
	public InfoPC(String ip, String hostname, String nameSO, String versionSO, String procesador, String ram, String porRAMLibre, 
			String discoDuro, String porDDLibre, String porCPULibre, String anchoBanda, String estadoConexion, double puntaje, String nombre) 
	{
		this.ip = ip;
		this.hostname = hostname;
		this.nameSO = nameSO;
		this.versionSO = versionSO;
		this.procesador = procesador;
		this.ram = ram;
		this.porRAMLibre = porRAMLibre;
		this.discoDuro = discoDuro;
		this.porDDLibre = porDDLibre;
		this.porCPULibre = porCPULibre;
		this.anchoBanda = anchoBanda;
		this.estadoConexion = estadoConexion;
		this.puntaje = puntaje;
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getNameSO() {
		return nameSO;
	}
	public void setNameSO(String nameSO) {
		this.nameSO = nameSO;
	}
	public String getVersionSO() {
		return versionSO;
	}
	public void setVersionSO(String versionSO) {
		this.versionSO = versionSO;
	}
	
	public String getProcesador() {
		return procesador;
	}
	public void setProcesador(String procesador) {
		this.procesador = procesador;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getPorRAMLibre() {
		return porRAMLibre;
	}
	public void setPorRAMLibre(String porRAMLibre) {
		this.porRAMLibre = porRAMLibre;
	}
	
	public String getDiscoDuro() {
		return discoDuro;
	}
	public void setDiscoDuro(String discoDuro) {
		this.discoDuro = discoDuro;
	}
	
	public String getPorDDLibre() {
		return porDDLibre;
	}
	public void setPorDDLibre(String porDDLibre) {
		this.porDDLibre = porDDLibre;
	}
	
	public String getPorCPULibre() {
		return porCPULibre;
	}
	public void setPorCPULibre(String porCPULibre) {
		this.porCPULibre = porCPULibre;
	}
	
	public String getAnchoBanda(){
		return anchoBanda;
	}
	public void setAnchoBanda(String anchoBanda) {
		this.anchoBanda = anchoBanda;
	}
	
	public String getEstadoConexion() {
		return estadoConexion;
	}
	public void setEstadoConexion(String estadoConexion) {
		this.estadoConexion = estadoConexion;
	}
	
	public double getPuntaje()
	{
		return puntaje;
	}
	public void setPuntaje(double puntaje)
	{
		this.puntaje = puntaje;
	}
	// Variables que se usarán cuando se quiera convertir un video
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
