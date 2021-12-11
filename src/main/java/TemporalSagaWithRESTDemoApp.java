import io.temporal.worker.WorkerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class TemporalSagaWithRESTDemoApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringBootApplication.run(TemporalSagaWithRESTDemoApp.class, args);
        WorkerFactory factory = applicationContext.getBean(WorkerFactory.class);
        MoneyTransferActivity moneyTransferActivity = applicationContext.getBean(MoneyTransferActivity.class);
    }
}
