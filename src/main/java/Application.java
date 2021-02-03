import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {

        //TODO логирование между этапами
        //TODO напечатать время выполнения программы разница во времени

        Service service = new Service();
        service.setDao(new DAO());
        service.setXmlService(new XMLService());
        service.configureDAO();
        service.doAllTasks();
    }
}
