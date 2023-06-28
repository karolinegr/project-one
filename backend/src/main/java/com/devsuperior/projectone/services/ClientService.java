package com.devsuperior.projectone.services;

import com.devsuperior.projectone.dto.ClientDTO;
import com.devsuperior.projectone.entities.Client;
import com.devsuperior.projectone.repositories.ClientRepository;
import com.devsuperior.projectone.services.exceptions.DatabaseException;
import com.devsuperior.projectone.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Optional;

@Service
public class ClientService implements Serializable {
    @Autowired
    ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> pagedList = this.repository.findAll(pageRequest);
        return pagedList.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(
            final Long id){
        Optional<Client> obj = this.repository.findById(id);
        Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO createClient(
            final ClientDTO newClient){
        Client newClientEntity = new Client();
        this.copyDtoToEntity(newClient, newClientEntity);
        newClientEntity = this.repository.save(newClientEntity);
        return new ClientDTO(newClientEntity);
    }

    @Transactional
    public ClientDTO updateClient(
            final Long id,
            final ClientDTO clientUpdate){
        try {
            Client clientEntity = this.repository.getReferenceById(id);
            this.copyDtoToEntity(clientUpdate, clientEntity);
            clientEntity = this.repository.save(clientEntity);
            return new ClientDTO(clientEntity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    public void deleteClient(
            Long id){
        try {
            this.repository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        } catch(DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
}
