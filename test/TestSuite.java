import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GroupTest.class, RoomTest.class, UserTest.class, AppointmentTest.class, PrintTest.class })
public class TestSuite {

}
