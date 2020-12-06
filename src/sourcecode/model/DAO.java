package sourcecode.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import sourcecode.model.Person;

/**
 *
 * @author Fábio Augusto Rodrigues
 */
public class DAO {
    
    private static DAO instance;
    protected EntityManager entityManager;
    
    public static DAO getInstance(){
        if (instance == null){
            instance = new DAO();
        }
        
        return instance;
    }
    
    private DAO(){
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudJava");
        
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }

        return entityManager;
    }
    
    public void persist(Person person){
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        }catch(Exception ex){
            entityManager.getTransaction().rollback();
        }
    }
    
    public void merge(Person person) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(person);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }
 
    public void delete(Person person){
        try{
            entityManager.getTransaction().begin();
            entityManager.remove(person);
            entityManager.getTransaction().commit();
        }catch(Exception ex){
            entityManager.getTransaction().rollback();
        }
    }
    
    public Person findById(int id){
        Person person = null;
        try {
            person = entityManager.find(Person.class, id);
        }catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
        return person;
    }
    
    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        return entityManager.createQuery("FROM " + Person.class.getName()).getResultList();
    }
    
    public List<Person> findByName(String name){
        Query consulta = entityManager.createQuery("FROM " + Person.class.getName() + " WHERE name=:nm");
        consulta.setParameter("nm", name);
        
        return consulta.getResultList();
    }
    
    public List<Person> findByAddress(String address){
        Query consulta = entityManager.createQuery("FROM " + Person.class.getName() + " WHERE address=:ads");
        consulta.setParameter("ads", address);
        
        return consulta.getResultList();
    }

    public List<Person> findByEmail(String email){
        Query consulta = entityManager.createQuery("FROM " + Person.class.getName() + " WHERE email=:em");
        consulta.setParameter("em", email);
        
        return consulta.getResultList();
    }
    
    public List<Person> findByBirthday(String birthday){
        Query consulta = entityManager.createQuery("FROM " + Person.class.getName() + " WHERE birthday=:bt");
        consulta.setParameter("bt", birthday);
        
        return consulta.getResultList();
    }
    
    public List<Person> findByNumber(String number){
        Query consulta = entityManager.createQuery("FROM " + Person.class.getName() + " WHERE number=:nb");
        consulta.setParameter("nb", number);
        
        return consulta.getResultList();
    }
    
    public void closeEntityManager(){
        entityManager.close();
    }
}
