package com.devsuperior.projectone.resources;

import com.devsuperior.projectone.dto.ClientDTO;
import com.devsuperior.projectone.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource implements Serializable {
    @Autowired
    ClientService service;

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ClientDTO> findAllPaged = this.service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(findAllPaged);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> findById(
            @PathVariable
            final Long id){
        ClientDTO clientDto = this.service.findById(id);
        return ResponseEntity.ok().body(clientDto);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @RequestBody
            final ClientDTO newClient){
        ClientDTO newClientDto = this.service.createClient(newClient);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newClientDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newClientDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable
            final Long id,
            @RequestBody
            final ClientDTO clientUpdate){
        ClientDTO updatedClient = this.service.updateClient(id, clientUpdate);
        return ResponseEntity.ok().body(updatedClient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable
            final Long id){
        this.service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
