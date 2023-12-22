package hua.dit.localDocWebApp.entity;

import jakarta.persistence.*;

@Entity
public class PendingAproval {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    public PendingAproval(Client client, Doctor doctor) {
        this.client = client;
        this.doctor = doctor;
    }

    public PendingAproval() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Integer getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
