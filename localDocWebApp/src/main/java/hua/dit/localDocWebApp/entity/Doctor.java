package hua.dit.localDocWebApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String postalCode;

    @Column
    private String speciality;

    @Column
    private Integer maxClients;

    @Column
    private Integer currentClients;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Client> client;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<PendingAproval> pendingAproval;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;


    public void setClient(List<Client> client) {
        this.client = client;
    }

    public List<Client> getClient() {
        return client;
    }


    public Doctor() {
        this.currentClients = 0;
    }




    public Doctor(String firstName, String lastName, String email, String phone, String address, String city, String state, String postalCode, String speciality, Integer maxClients, Integer currentClients) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.speciality = speciality;
        this.maxClients = maxClients;
        this.currentClients = 0;
    }


    public void setPendingAproval(List<PendingAproval> pendingAproval) {
        this.pendingAproval = pendingAproval;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PendingAproval> getPendingAproval() {
        return pendingAproval;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCurrentClients() {
        return currentClients;
    }

    public Integer getMaxClients() {
        return maxClients;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCurrentClients(Integer currentClients) {
        this.currentClients = currentClients;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMaxClients(Integer maxClients) {
        this.maxClients = maxClients;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
