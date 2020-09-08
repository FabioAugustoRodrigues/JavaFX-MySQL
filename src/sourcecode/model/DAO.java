package sourcecode.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    
    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        return entityManager.createQuery("FROM " + Person.class.getName()).getResultList();
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
    
    public void closeEntityManager(){
        entityManager.close();
    }
}
