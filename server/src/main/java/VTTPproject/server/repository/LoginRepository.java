package VTTPproject.server.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import VTTPproject.server.model.Credentials;
import VTTPproject.server.model.User;
import VTTPproject.server.utils.sql;

@Repository
public class LoginRepository {
    
    @Autowired
    private JdbcTemplate template;

    //get all emails in user objects
    public List<String> getAllEmails(){
        List<User> userQuery = new LinkedList<>();
        userQuery = template.query(sql.sql_getAllUsers, BeanPropertyRowMapper.newInstance(User.class));
        List<String> emailList = userQuery.stream()
            .map(user -> user.getEmail())
            .toList();
        return emailList;
    }


    //get credentials object in credentials by email
    public Credentials getCredentialByEmail(String email){
        Credentials c = null;
        try{
            c= template.queryForObject(sql.sql_getCredentialByEmail, BeanPropertyRowMapper.newInstance(Credentials.class), email);
        }catch(DataAccessException ex){
            System.out.println("Credential object not found in credentials");
        }
        
        return c;
    }
    
    //save users
    public void insertUser(User user){
        template.update(sql.sql_insertIntoUser, user.getEmail(), user.getName());
    }

    //save credentials
    public void insertCrendentials(Credentials cred){
        template.update(sql.sql_insertIntoCredentials, cred.getUsers_email(), cred.getPassword());
    }



}
