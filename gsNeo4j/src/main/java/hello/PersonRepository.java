/* There is a lot to explain in this tutorial
 * so instead of commenting the fuck out of everything
 * I recomend you go to the link on the next line
http://spring.io/guides/gs/accessing-data-neo4j/

	Jan 15, 2014*/

package hello;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person> {
	
	Person findByName(String name);

    Iterable<Person> findByTeammatesName(String name);

}
