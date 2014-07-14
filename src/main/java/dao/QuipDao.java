package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.Quip;
import models.QuipDto;
import models.QuipsDto;
import models.User;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;

public class QuipDao {

    @Inject
    Provider<EntityManager> entitiyManagerProvider;
    
    @Transactional
    public QuipsDto getAllQuips() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        TypedQuery<Quip> query= entityManager.createQuery("SELECT x FROM Quip x", Quip.class);
        List<Quip> quips = query.getResultList();

        QuipsDto quipsDto = new QuipsDto();
        quipsDto.quips = quips;
        
        return quipsDto;
        
    }
    
    @Transactional
    public Quip getFirstQuipForFrontPage() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Quip x ORDER BY x.postedAt DESC");
        Quip quip = (Quip) q.setMaxResults(1).getSingleResult();
        
        return quip;
        
        
    }
    
    @Transactional
    public List<Quip> getOlderQuipsForFrontPage() {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Quip x ORDER BY x.postedAt DESC");
        List<Quip> quips = (List<Quip>) q.setFirstResult(1).setMaxResults(10).getResultList();
        
        return quips;
        
        
    }
    
    @Transactional
    public Quip getQuip(Long id) {

        try {
            EntityManager entityManager = entitiyManagerProvider.get();

            Query q = entityManager.createQuery("SELECT x FROM Quip x WHERE x.id = :idParam");
            Quip quip = (Quip) q.setParameter("idParam", id).getSingleResult();

            return quip;
        } catch (NoResultException nre) {
            return null;
        }
        
        
    }
    
    /**
     * Returns false if user cannot be found in database.
     */
    @Transactional
    public boolean postQuip(String username, QuipDto quipDto) {
        
        EntityManager entityManager = entitiyManagerProvider.get();
        
        Query query = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam");
        User user = (User) query.setParameter("usernameParam", username).getSingleResult();
        
        if (user == null) {
            return false;
        }
        
        Quip quip = new Quip(user, quipDto.sourceDate, quipDto.content);
        entityManager.persist(quip);
        
        return true;
        
    }

}
