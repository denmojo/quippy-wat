package dao;


import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Quip;
import models.User;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class SetupDao {
    
    @Inject
    Provider<EntityManager> entityManagerProvider;

    @Transactional
    public void setup() {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM User x");
        List<User> users = (List<User>) q.getResultList();        
        
        if (users.size() == 0) {

            // Create a new user and save it
            User bob = new User("bob@gmail.com", "secret", "Bob");
            entityManager.persist(bob);
            
            // Create a new post
            Quip bobPost3 = new Quip(bob, new Date(System.currentTimeMillis()), example2);
            entityManager.persist(bobPost3);

            // Create a new post
            Quip bobPost2 = new Quip(bob, new Date(System.currentTimeMillis()), example1);
            entityManager.persist(bobPost2);
            
            // Create a new post
            Quip bobPost1 = new Quip(bob, new Date(System.currentTimeMillis()), post1Content);
            entityManager.persist(bobPost1);
        }

    }
    
    String example1 = "16:06 < dm> nothing unifies #general like kristine breaking google";
    String example2 = "15:23 <@victor> sigh i hate rails";
    
    public String post1Content = "09:20 <@keenanp> Ugh, Mondays.<br />" +
            "09:20 <@keenanp> Ohwait, it's Tuesday.<br />" +
            "09:20 <@kristine> Lol";

}
