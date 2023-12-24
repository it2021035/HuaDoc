package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;



    @Transactional
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Integer clientId){
        clientRepository.deleteById(clientId);
    }

    @Transactional
    public Client getClient(Integer clientId){
        return clientRepository.findById(clientId).get();
    }

    @Transactional
    public Iterable<Client> getClients(){
        return clientRepository.findAll();
    }

    @Transactional
    public void removeClientDoctor(Integer clientId){
        Client client = clientRepository.findById(clientId).get();
        client.setDoctor(null);
        clientRepository.save(client);
    }






}
