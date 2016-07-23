package Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class MesasRESTService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getLastLlamado(
            @DefaultValue("ALL") @QueryParam("esp") String esp) throws IOException{
        return "Mesas del ultimo llamado | ESP: " + esp;
    }

    @GET
    @Path("{year}/")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllLlamadosOfYear(
            @PathParam("year") int year,
            @DefaultValue("ALL") @QueryParam("esp") String esp) throws IOException{
        return "Todas las mesas del año: " + year + " | ESP: " + esp;
    }

    @GET
    @Path("{year}/{llamado}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLlamadoOfYear(
            @PathParam("year") int year,
            @PathParam("llamado") int llamado,
            @DefaultValue("ALL") @QueryParam("esp") String esp) throws IOException{
        return "Todas las mesas del año: " + year + "| el llamado nro: " + llamado +" | ESP: " + esp;
    }

}