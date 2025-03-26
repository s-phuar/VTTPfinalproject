package VTTPproject.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import VTTPproject.server.model.Credentials;
import VTTPproject.server.model.User;
import VTTPproject.server.repository.LoginRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    private final Counter loginCounter;

    @Autowired
    public LoginService(MeterRegistry meterRegistry) {
        // Metric 1: login attempts counter
        this.loginCounter = Counter.builder("app_login_count_metric1")
                .description("Total number of login attempts")
                .register(meterRegistry);
    }


    //get all emails
    public List<String> getAllEmails(){
        return loginRepository.getAllEmails();
    }

    //save user as transaction
    @Transactional
    public void createNewAccount(String name, String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        Credentials cred = new Credentials();
        cred.setUsers_email(email);
        cred.setPassword(password);

        loginRepository.insertUser(user);
        loginRepository.insertCrendentials(cred);   
    }


    public String getPasswordByEmail(String email){
        // Metric 1: ++ login counter when checking password
        loginCounter.increment();
        Credentials cred = loginRepository.getCredentialByEmail(email);
        return cred.getPassword();
    }


}
