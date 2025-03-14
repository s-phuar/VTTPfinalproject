package VTTPproject.server.model;

public class Credentials {
    private String users_email;
    private String password;
    
    public String getUsers_email() {return users_email;}
    public void setUsers_email(String users_email) {this.users_email = users_email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "Credentials [users_email=" + users_email + ", password=" + password + "]";
    }


}
