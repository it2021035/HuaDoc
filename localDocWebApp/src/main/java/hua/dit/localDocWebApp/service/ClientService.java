package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.User;
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
    public Client saveClient(Client client){
        clientRepository.save(client);
        return client;
    } //saves client

    @Transactional
    public void deleteClient(Integer clientId){
        clientRepository.deleteById(clientId);
    } //deletes client

    @Transactional
    public Client getClient(Integer clientId){
        return clientRepository.findById(clientId).get();
    } //get client from id

    @Transactional
    public Iterable<Client> getClients(){
        return clientRepository.findAll();
    } //get all clients

    @Transactional
    public Client removeClientDoctor(Integer clientId){
        Client client = clientRepository.findById(clientId).get();
        client.setDoctor(null);
        clientRepository.save(client);
        return client;
    } //removes the doctor from the client

    @Transactional
    public List<Client> getClientsByUser(User user){
        return clientRepository.findByUser(user);
    } //get all clients of a user






}
