package dom.empresa;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.spi.PersistenceCapable;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroups;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Resolve;
import org.apache.isis.applib.annotation.Resolve.Type;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.filter.Filters;
import org.apache.isis.applib.util.TitleBuffer;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.value.Blob;
import org.apache.isis.core.objectstore.jdo.applib.annotations.Auditable;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import dom.huesped.Huesped;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name="empresa_all", language="JDOQL",  
            value="SELECT FROM dom.empresa.Empresa WHERE ownedBy == :ownedBy")
 })
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("EMPRESA")
@Auditable
@AutoComplete(repository=Empresas.class, action="autoComplete")
@MemberGroups({"Datos"})
public class Empresa {
	
	public String title(){
		return this.getNombre();
	}
	
	private String nombre;
	
	@MemberOrder(name="Datos",sequence="1")
	public String getNombre(){
		return nombre;
	}
	
	public void setNombre(String nuevoNombre) {
		this.nombre = nuevoNombre;
	}
	
	private String cuit;
	
	@MemberOrder(name="Datos",sequence="2")
	public String getCuit(){
		return cuit;
	}
	
	public void setCuit(String nuevoCuit){
		this.cuit = nuevoCuit;
	}
		
	private String ownedBy;
	@Hidden
	public String getOwnedBy() {
	    return ownedBy;
	}

	public void setOwnedBy(final String ownedBy) {
	    this.ownedBy = ownedBy;
	}
	
    public static Filter<Empresa> thoseOwnedBy(final String currentUser) {
        return new Filter<Empresa>() {
            @Override
            public boolean accept(final Empresa empresa) {
                return Objects.equal(empresa.getOwnedBy(), currentUser);
            }

        };
    }
    
    /*
     * Mapeo de la colección a través de la clave foranea por el objeto Huesped y su objeto "empresa"
     */
    @Persistent(mappedBy="empresa")
    private List<Huesped> huespedes = new ArrayList<Huesped>();
    
    public List<Huesped> getHuespedes() { return huespedes; }
    
    /*
     * El método que incluye el nuevo huesped a la lista de empleados de esta empresa
     * No se muestra como acción en el viewer
     */    
    
    @Hidden
    public void addToHuesped(Huesped huesped) {
        if(huesped == null || huespedes.contains(huesped)) {
          return;
        }
        huesped.setEmpresa(this);
        huespedes.add(huesped);
    }
    
    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
	
}
