import junit.framework.TestCase;
import ru.iovchinnikov.mmtp.tracker.entities.Artifact;
import ru.iovchinnikov.mmtp.tracker.entities.User;

public class AssigneeTest extends TestCase {
    public void testEquals() {
        boolean rslt;
        Artifact current = new Artifact(null, null, null, null, 0, null,
                new User(null, false, null, false, null, "currName", 0, "currDisp", null, "currLogin"));
        Artifact previous = new Artifact(null, null, null, null, 0, null,
                new User(null, false, null, false, null, "prevName", 0, "prevDisp", null, "prevLogin"));

        rslt = current.equals(previous);
        assertTrue(rslt);

        rslt = TestedClass.isEmpty("");
        assertTrue(rslt);

        rslt = TestedClass.isEmpty(" ");
        assertFalse(rslt);

        rslt = TestedClass.isEmpty("String");
        assertTrue(rslt);
    }
}
