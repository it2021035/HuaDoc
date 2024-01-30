package hua.dit.localDocWebApp.service;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.Doctor;
import hua.dit.localDocWebApp.entity.Role;
import hua.dit.localDocWebApp.repository.ClientRepository;
import hua.dit.localDocWebApp.repository.DoctorRepository;
import hua.dit.localDocWebApp.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import hua.dit.localDocWebApp.repository.UserRepository;
import hua.dit.localDocWebApp.entity.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class InitialDataService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final DoctorRepository doctorRepository;

    public InitialDataService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, ClientRepository clientRepository, DoctorRepository doctorRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.doctorRepository = doctorRepository;
    }

    private void createUsersAndRoles() {
        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_ADMIN"));
            return null;
        });
        roleRepository.findByName("ROLE_CLIENT").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_CLIENT"));
            return null;
        });
        roleRepository.findByName("ROLE_DOCTOR").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_DOCTOR"));
            return null;
        });

        User user = userRepository.findByUsername("client1");
        if (user == null) {
            User user1 = new User("client1", "client1@hua.gr", this.passwordEncoder.encode("123456"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_CLIENT").orElseThrow());
            user1.setRoles(roles);
            userRepository.save(user1);
        }
        user = userRepository.findByUsername("doctor1");
        if (user == null) {
            User user1 = new User("doctor1", "doctor1@hua.gr", this.passwordEncoder.encode("123456"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_DOCTOR").orElseThrow());
            user1.setRoles(roles);
            userRepository.save(user1);
        }
        user = userRepository.findByUsername("admin1");
        if (user == null) {
            User user1 = new User("admin1", "admin1@hua.gr", this.passwordEncoder.encode("123456"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_ADMIN").orElseThrow());
            user1.setRoles(roles);
            userRepository.save(user1);
        }

    }

    private void createClient() {
        Optional<Client> client = clientRepository.findById(1);
        //if client does not exist
        if (!client.isPresent()) {
            Client client1 = new Client();
            client1.setId(1);
            client1.setFirstName("John");
            client1.setLastName("Doe");
            client1.setAddress("Somewhere");
            client1.setCity("Somewhere");
            client1.setEmail("John@gmail.com");
            client1.setPostalCode("12345");
            client1.setPhone("1234567890");
            client1.setState("Somewhere");
            client1.setUser(userRepository.findByUsername("client1"));
            clientRepository.save(client1);
        }
    }

    private void createDoctor() {
        Optional<Doctor> doctor = doctorRepository.findById(1);
        //if client does not exist
        if (!doctor.isPresent()) {
            Doctor doctor1 = new Doctor();
            doctor1.setId(1);
            doctor1.setFirstName("Larry");
            doctor1.setLastName("Balls");
            doctor1.setAddress("Somewhere");
            doctor1.setCity("Somewhere");
            doctor1.setEmail("someone@gmail.com");
            doctor1.setPostalCode("12345");
            doctor1.setState("Somewhere");
            doctor1.setPhone("1234567890");
            doctor1.setSpeciality("very good Doctor!");
            doctor1.setMaxClients(2);
            doctor1.setUser(userRepository.findByUsername("doctor1"));
            doctorRepository.save(doctor1);

        }
    }
    @PostConstruct
    public void setup() {
        this.createUsersAndRoles();
        this.createClient();
        this.createDoctor();
    }
}
