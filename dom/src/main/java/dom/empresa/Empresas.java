package dom.empresa;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
import dom.huesped.Huesped;

@Named("Empresas")
public class Empresas extends AbstractFactoryAndRepository {
	
    @MemberOrder(sequence = "1")
    public Empresa NuevaEmpresa(
            @Named("Nombre") String nombre, 
            @Named("Cuit") String cuit){
        final String ownedBy = currentUserName();
        return nuevaEmpresa(nombre, cuit, ownedBy);
    }
    
    @Hidden
    public Empresa nuevaEmpresa(
            String nombre,
            String cuit,
            String userName) {
        final Empresa empresa = newTransientInstance(Empresa.class);
        empresa.setNombre(nombre);
        empresa.setCuit(cuit);
        empresa.setOwnedBy(userName);
        
        persist(empresa);
        return empresa;
    }
    
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Empresa> ListaEmpresas() {
        final String currentUser = currentUserName();
        final List<Empresa> items = allMatches(Empresa.class, Empresa.thoseOwnedBy(currentUser));
        return items;
    }    

    /*
     * Método para llenar el DropDownList de empresas, con la posibilidad de que te autocompleta las coincidencias al ir tipeando
     */
    @Hidden
    public List<Empresa> autoComplete(final String nombre) {
        return allMatches(Empresa.class, new Filter<Empresa>() {
            @Override
            public boolean accept(final Empresa e) {
                return ownedByCurrentUser(e) && e.getNombre().contains(nombre);
            }
        });
    }
    
   protected boolean ownedByCurrentUser(final Empresa e) {
        return Objects.equal(e.getOwnedBy(), currentUserName());
   }

    protected String currentUserName() {
        return getContainer().getUser().getName();
    }
}