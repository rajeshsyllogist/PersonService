package com.cognizant.person.resources;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cognizant.person.domain.entity.Person;
import com.cognizant.person.domain.entity.PersonList;
import com.cognizant.person.service.PersonService;

@Controller
@Path("v1")
public class PersonResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);
	
	@Autowired
	private PersonService personService;
	
	@Produces(value = {MediaType.APPLICATION_XML ,  MediaType.APPLICATION_JSON})
    @GET
    @Path("/person/{id}")
    public Response getPerson(@PathParam("id") final String id) {
        Person person = personService.getPerson(id);
        ResponseBuilder response = Response.ok().entity(person);
        return  response.build();
    }
	
	
	@Produces(value = {MediaType.APPLICATION_XML ,  MediaType.APPLICATION_JSON})
    @GET
    @Path("/person")
    public Response getPersons() {
        PersonList list = personService.getAllPersons();
        ResponseBuilder response = Response.ok().entity(list);
        return  response.build();
    }
	
	
	@Produces (value = { MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	@Consumes (value = { MediaType.APPLICATION_XML ,  MediaType.APPLICATION_JSON})
	@POST
	@Path("/person")
	public Response storePerson(Person person) {
		
		LOGGER.info(" >> store person details " );
		LOGGER.info(" first name " + person.getFirstName());
		LOGGER.info(" last name " + person.getFirstName());
		personService.savePersonDetails(person);
		return Response.ok().build();
		
	}
	
}
