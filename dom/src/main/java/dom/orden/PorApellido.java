package dom.orden;

import dom.huesped.Huesped;
import java.util.Comparator;

public class PorApellido implements Comparator<Huesped> {
	
	public int compare(Huesped h1, Huesped h2){
		return h1.getApellido().compareTo(h2.getApellido());
	}

	/*@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	
}
