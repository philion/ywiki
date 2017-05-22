package com.acmerocket.ywiki;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acmerocket.ywiki.model.Pet;
import com.acmerocket.ywiki.model.PetData;

import java.util.UUID;

@Path("/pets")
public class PetsResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPet(final Pet newPet) {
        if (newPet.getName() == null || newPet.getBreed() == null) {
            return Response.status(400).entity(new Error("Invalid name or breed")).build();
        }

        Pet dbPet = newPet;
        dbPet.setId(UUID.randomUUID().toString());
        return Response.status(200).entity(dbPet).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Pet[] listPets(@QueryParam("limit") int limit) {
        if (limit < 1) {
            limit = 10;
        }

        Pet[] outputPets = new Pet[limit];

        for (int i = 0; i < limit; i++) {
            Pet newPet = new Pet();
            newPet.setId(UUID.randomUUID().toString());
            newPet.setName(PetData.getRandomName());
            newPet.setBreed(PetData.getRandomBreed());
            newPet.setDateOfBirth(PetData.getRandomDoB());
            outputPets[i] = newPet;
        }

        return outputPets;
    }

    @Path("/{petId}") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Pet getPetDetails() {
        Pet newPet = new Pet();
        newPet.setId(UUID.randomUUID().toString());
        newPet.setBreed(PetData.getRandomBreed());
        newPet.setDateOfBirth(PetData.getRandomDoB());
        newPet.setName(PetData.getRandomName());
        return newPet;
    }
}