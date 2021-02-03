import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

public class Service {

    public DAO dao;
    public XMLService xmlService;

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public void setXmlService(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    public void printSum(List<Integer> values) {
        System.out.println(values.stream().mapToInt(i -> i).sum());
    }

    public void configureDAO() {
        Properties prop = new Properties();
        try (InputStream inputStream = Service.class.getResourceAsStream("application.properties")) {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dbName = prop.getProperty("dbName");
        dao.setDbName(dbName);
        dao.setHost(prop.getProperty("host"));
        dao.setPort(Integer.parseInt(prop.getProperty("port")));
        dao.setUser(prop.getProperty("user"));
        dao.setPass(prop.getProperty("pass"));
    }

    public void doAllTasks() throws IOException {

        //FILL DB
        //TODO add BatchSize to params?
        dao.fillDB("test", "field", 1_000);


        //CREATE XML
        Properties prop = new Properties();
        try (InputStream inputStream = Service.class.getResourceAsStream("application.properties")) {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File source = new File(prop.getProperty("directory") + "1.xml");
        source.createNewFile();
        xmlService.createXML(source, dao.getAll("test"));


        //TRANSFORM XML
        File target = new File(prop.getProperty("directory") + "2.xml");
        File xslt = null;
        try {
            xslt = new File(this.getClass().getResource("xslt.xsl").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        xmlService.transformXML(source, target, xslt);


        //PRINT SUM
        List<Integer> values = xmlService.extractNodeAttributes(target, "/entries/entry", "field");
        printSum(values);
    }

}

// Source xsltPattern = new StreamSource(new File("C://Users//Mikhail//IdeaProjects//magnit-assignment//src//main//resources//xslt.xsl"));




