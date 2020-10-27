# JavaFX-MySQL
Simple crud using JavaFX and JPA with Hibernate\
The JavaMail API was also used to send e-mails


## Samples
Reading data from database
![Crud screen](https://github.com/FabioAugustoRodrigues/JavaFX-MySQL/blob/master/screenshots/crud.png)

Registering a New Person\
![register screen](https://github.com/FabioAugustoRodrigues/JavaFX-MySQL/blob/master/screenshots/newPerson.png)

## Database 

Hibernate is being used, so the database settings are in the ``META-INF/persistence.xml``. For the program to work, create a database with the name ```crudjava``` or put the following credentials in the file the chosen name:

```
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/NAME/>
```



