import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropertiesTest {
	public static void main(String[] args) throws IOException
	{
		Properties p = new Properties();
		File f = new File("properties");
		p.load(new FileReader(f));
		String jdbcDriver = p.getProperty("jdbcDriver");
		System.out.println(jdbcDriver);
	}
}
