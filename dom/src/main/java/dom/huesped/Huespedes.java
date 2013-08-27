package dom.huesped;


import java.util.Collections;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;
import org.apache.isis.applib.annotation.Optional;

import com.google.common.base.Objects;

import dom.empresa.Empresa;
import dom.orden.*;

@Named("Huéspedessssssss")
public class Huespedes extends AbstractFactoryAndRepository {
	
    @MemberOrder(sequence = "1")
    @Named("Crear nuevo Huesped")
    public Huesped nuevoHuesped(
            @Named("Apellido") String apellido, 
            @Named("Nombre") String nombre,
            @Named("Direccion") String direccion,
            @Optional
            @Named("Empresa") Empresa empresa,
            @Optional
            @Named("Documento") String documento){
        final String ownedBy = currentUserName();
        return nuevoHuesped(apellido, nombre, direccion, empresa, documento,ownedBy);
    }
    
    @Hidden
    public Huesped nuevoHuesped(
            String apellido, 
            String nombre,
            String direccion,
            Empresa empresa,
            String documento, 
            String userName) {
        final Huesped huesped = newTransientInstance(Huesped.class);
        huesped.setApellido(apellido);
        huesped.setNombre(nombre);
        huesped.setDomicilio(direccion);
        huesped.setDocumento(documento);
        huesped.setOwnedBy(userName);
        
        /*
         * Se agrega el nuevo huesped a la colección Empresa 
         */
        if(empresa != null)
        {
            huesped.setEmpresa(empresa);
        	empresa.addToHuesped(huesped);
        }
        	
        persistIfNotAlready(huesped);
        return huesped;
    }
    
    @Hidden
    public List<Huesped> autoComplete(final String nombre) {
        return allMatches(Huesped.class, new Filter<Huesped>() {
            @Override
            public boolean accept(final Huesped h) {
                return ownedByCurrentUser(h) && h.getNombre().contains(nombre);
            }
        });
    }
    
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Huesped> ListaHuespedes() {
        final String currentUser = currentUserName();
        final List<Huesped> items = allMatches(Huesped.class, Huesped.thoseOwnedBy(currentUser));
        Collections.sort(items,new PorApellido());
        return items;
    }
    
    protected boolean ownedByCurrentUser(final Huesped h) {
        return Objects.equal(h.getOwnedBy(), currentUserName());
    }

    protected String currentUserName() {
        return getContainer().getUser().getName();
    }
}
