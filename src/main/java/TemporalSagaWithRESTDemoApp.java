import activity.MoneyTransferActivity;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import workflow.MoneyTransferWorkflow;
import workflow.MoneyTransferWorkflowImpl;


@SpringBootApplication
public class TemporalSagaWithRESTDemoApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringBootApplication.run(TemporalSagaWithRESTDemoApp.class, args);
        WorkerFactory factory = applicationContext.getBean(WorkerFactory.class);
        MoneyTransferActivity moneyTransferActivity = applicationContext.getBean(MoneyTransferActivity.class);
        Worker worker = factory.newWorker(MoneyTransferWorkflowImpl.QUEUE_NAME);
        worker.registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl.class);
        worker.registerActivitiesImplementations(moneyTransferActivity);
        factory.start();

        /*
        WITHOUT SPRINGBOOT

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(MoneyTransferWorkflowImpl.QUEUE_NAME);
        worker.registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl.class);
        worker.registerActivitiesImplementations(moneyTransferActivity);
        factory.start();
        */


    }
}
