package agenda.persistencia;

import java.io.FileOutputStream;import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import agenda.modelo.Contacto;
import agenda.util.Contactos;

public class ContactoDaoMemSerial implements ContactoDao {
	
	//vamos a guardar los datos en un map
	
	private Map<Integer, Contacto> almacen;
	private int proximoId;
	
	
	private final String FICH_ALM = "almacen.dat";
	private final String FICH_ID = "id.dat";
		
	//hacemos el constructor
	public ContactoDaoMemSerial() {
		almacen = new HashMap<Integer, Contacto>();
		proximoId = 1; //para que se valla incrementando
		cargaInicial();
	}
	
	private void cargaInicial()
	{
		for(Contacto c :Contactos.generaContactos()) {
			insertar(c);
		}
		grabar();
	}
	
	private void grabar() {
		try(FileOutputStream fosAlm= new FileOutputStream(FICH_ALM);
			FileOutputStream fosId = new FileOutputStream(FICH_ID)){
			
			//creamos dos oupenSrin difernetes
			ObjectOutputStream oosAlm = new ObjectOutputStream(fosAlm);
			ObjectOutputStream oosId = new ObjectOutputStream(fosId);
			
			oosAlm.writeObject(almacen);
			oosId.writeInt(proximoId);
		
		}catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
		
		}
		
	}

	@Override
	public void insertar(Contacto c) {
		c.setIdContacto(proximoId++);
		almacen.put(c.getIdContacto(), c);
		
	}

	@Override
	public void actualizar(Contacto c) {
			almacen.replace(c.getIdContacto(), c);
		}
		

	@Override
	public boolean eliminar(int idContacto) {  //Es el método que recibe un número (el id del contacto que queremos eliminar) y devuelve true si lo elimina, o false si no lo encuentra.
		return almacen.remove(idContacto) != null; //se remueva directamente por clave 
	}

	@Override
	public boolean eliminar(Contacto c) {
		return eliminar(c.getIdContacto());
	}

	@Override
	public Contacto buscar(int idContacto) {
		return almacen.get(idContacto);
	}

	
	@Override
	public Set<Contacto> buscar(String cadena) {
		cadena = cadena.toLowerCase();
		Set<Contacto> resu = new HashSet<Contacto>();
		for (Contacto contacto : almacen.values()) {
			if(contacto.getApellidos().toLowerCase().contains(cadena)
					||contacto.getApodo().toLowerCase().contains(cadena)
					||contacto.getNombre().toLowerCase().contains(cadena)) {
				resu.add(contacto);
			}	
		}
		return resu;
	}

	
	@Override
	public Set<Contacto> buscarTodos() {
		return new HashSet<Contacto>(almacen.values());
	}

	
}
