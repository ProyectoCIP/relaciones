package dom.huesped;


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
import dom.empresa.Empresa;
import dom.empresa.Empresas;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name="todos_huesped", language="JDOQL",  
            value="SELECT FROM dom.huesped.Huesped WHERE ownedBy == :ownedBy")
})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("HUESPED")
@Auditable
@AutoComplete(repository=Huespedes.class, action="autoComplete")
@MemberGroups({"Datos", "Razon Social","Procedencia"})
public class Huesped {
	
	@Named("Huésped")
	public String title(){
		return this.getApellido()+" "+this.getNombre();
	}
	
	private String apellido;	
	
	@MemberOrder(name="Datos",sequence="1")
	public String getApellido(){
		return apellido;
	}
	public void setApellido(final String apellido){
		this.apellido = apellido;
	}
	private String nombre;
	@MemberOrder(name="Datos",sequence="2")
	public String getNombre(){
		return nombre;
	}
	public void setNombre(final String nombre){
		this.nombre = nombre;
	}
	
	private String domicilio;
	
	@MemberOrder(name="Datos",sequence="3")
	public String getDomicilio(){
		return domicilio;
	}
	public void setDomicilio(final String domicilio){
		this.domicilio = domicilio;
	}
	
	private String documento;

	@Optional
	@MemberOrder(name="Datos",sequence="4")
	public String getDocumento(){
		return documento;
	}
	public void setDocumento(final String documento){
		this.documento = documento;
	}
	
	
	private String ownedBy;
	@Hidden
	public String getOwnedBy() {
	    return ownedBy;
	}

	public void setOwnedBy(final String ownedBy) {
	    this.ownedBy = ownedBy;
	}
	

    public static Filter<Huesped> thoseOwnedBy(final String currentUser) {
        return new Filter<Huesped>() {
            @Override
            public boolean accept(final Huesped huesped) {
                return Objects.equal(huesped.getOwnedBy(), currentUser);
            }

        };
    }
    
    /*
     * Empresa opcional para los huespedes con convenio
     */
    private Empresa empresa;   
    
    @Optional
	@MemberOrder(name="Razon Social",sequence="1")    
    public Empresa getEmpresa() { return empresa; }
    
    public void setEmpresa(Empresa empresa) { 
    	this.empresa = empresa;
    }

    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    // }}



}
