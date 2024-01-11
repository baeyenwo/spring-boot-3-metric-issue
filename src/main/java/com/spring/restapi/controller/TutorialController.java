package com.spring.restapi.controller;


import io.micrometer.core.annotation.Timed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Path("/api")
public class TutorialController {

  @GET
  @Path("/tutorials0")
  public ResponseEntity<String> getAllTutorials0(@RequestParam(required = false) String title) {
    return internalGetAllTutorials();
  }

  @Timed
  @GET
  @Path("/tutorials1")
  public ResponseEntity<String> getAllTutorials1(@RequestParam(required = false) String title) {
    return internalGetAllTutorials();
  }

  @Timed(value="tutorials2")
  @GET
  @Path("/tutorials2")
  public ResponseEntity<String> getAllTutorials2(@RequestParam(required = false) String title) {
    return internalGetAllTutorials();
  }


  private ResponseEntity<String> internalGetAllTutorials() {
    try {
      return new ResponseEntity<>("tutorials", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
