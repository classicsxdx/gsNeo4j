package hello;

import java.io.File;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


@Configuration
@EnableNeo4jRepositories
public class Application extends Neo4jConfiguration implements CommandLineRunner  {
	
	
	@Bean
    EmbeddedGraphDatabase graphDatabaseService() {
        return new EmbeddedGraphDatabase("accessingdataneo4j.db");
    }
	
	@Autowired
    PersonRepository personRepository;

    @Autowired
    GraphDatabase graphDatabase;
    
    public void run(String... args) throws Exception {
        Person mark = new Person("Mark");
        Person courtney = new Person("Courtney");
        Person jagger = new Person("Jagger");

        System.out.println("Before linking up with Neo4j...");
        for (Person person : new Person[]{mark, courtney, jagger}) {
            System.out.println(person);
        }

        Transaction tx = graphDatabase.beginTx();
        try {
            personRepository.save(mark);
            personRepository.save(courtney);
            personRepository.save(jagger);

            mark = personRepository.findByName(mark.name);
            mark.worksWith(courtney);
            mark.worksWith(jagger);
            personRepository.save(mark);

            courtney = personRepository.findByName(courtney.name);
            courtney.worksWith(jagger);
            // We already know that courtney works with mark
            personRepository.save(courtney);

            // We already know jagger works with courtney and mark

            tx.success();
        } finally {
            tx.finish();
        }

        System.out.println("Lookup each person by name...");
        for (String name: new String[]{mark.name, courtney.name, jagger.name}) {
            System.out.println(personRepository.findByName(name));
        }

        System.out.println("Looking up who works with mark...");
        for (Person person : personRepository.findByTeammatesName("mark")) {
            System.out.println(person.name + " works with mark.");
        }
    }

    public static void main(String[] args) throws Exception {
        FileUtils.deleteRecursively(new File("accessingdataneo4j.db"));

        SpringApplication.run(Application.class, args);
    }

}