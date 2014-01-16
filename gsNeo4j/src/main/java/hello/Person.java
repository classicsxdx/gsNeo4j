/* There is a lot to explain in this tutorial
 * so instead of commenting the fuck out of everything
 * I recomend you go to the link on the next line
http://spring.io/guides/gs/accessing-data-neo4j/

	Jan 15, 2014*/

package hello;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Person {

	@GraphId Long id;
	public String name;
	
	public Person() {}
	public Person(String name) { this.name = name; }
	
	@RelatedTo(type="TEAMMATE", direction=Direction.BOTH)
	public @Fetch Set<Person> teammates;
	
	public void worksWith(Person person) {
		if (teammates == null) {
			teammates = new HashSet<Person>();
		}
		teammates.add(person);
	}
	
	public String toString() {
		String results = name + "'s teammates include\n"; 
		if (teammates != null) {
			for (Person person : teammates) {
                results += "\t- " + person.name + "\n";
            }
		}
		return results;
	}
}