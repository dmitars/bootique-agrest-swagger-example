package org.example.agrest.api;

import io.agrest.Ag;
import io.agrest.DataResponse;
import io.agrest.SimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.example.agrest.persistent.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Tags(value = @Tag(name = "book", description = "API serving bookstore books"))
@Path("book")
@Produces(MediaType.APPLICATION_JSON)
public class BookApi {

    @Context
    private Configuration config;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation
    public SimpleResponse create(
            @RequestBody(description = "JSON object representing a Book") String data
    ) {
        return Ag.create(Book.class, config).sync(data);
    }

    @GET
    @Operation(description = "Gets books per supplied criteria. Supports all Agrest keys")
    public DataResponse<Book> getAllBooks(
            @Context UriInfo uriInfo
    ) {
        return Ag.select(Book.class, config).uri(uriInfo).get();
    }
}
