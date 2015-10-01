package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name="Users")
public class User {
    
    @Id
    @GenericGenerator(name="incrementId",strategy="increment") 
	@GeneratedValue(generator="incrementId")
    public Long id;
    public String username;
    public String password;
    public String fullname;
    public boolean isAdmin;
    
    public User() {}
    
    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }
 
}
