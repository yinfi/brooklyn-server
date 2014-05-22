package brooklyn.entity.basic;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import brooklyn.entity.proxying.EntitySpec;
import brooklyn.test.entity.TestApplication;
import brooklyn.test.entity.TestEntity;

public class BrooklynShutdownHooksTest {

    private TestApplication app;
    private TestEntity entity;
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() {
        app = ApplicationBuilder.newManagedApp(TestApplication.class);
        entity = app.createAndManageChild(EntitySpec.create(TestEntity.class));
    }
    
    @AfterMethod(alwaysRun=true)
    public void tearDown() {
        if (app != null) Entities.destroyAll(app.getManagementContext());
    }

    @Test
    public void testInvokeStopEntityOnShutdown() throws Exception {
        BrooklynShutdownHooks.invokeStopOnShutdown(entity);
        BrooklynShutdownHooks.BrooklynShutdownHookJob job = new BrooklynShutdownHooks.BrooklynShutdownHookJob();
        job.run();
        
        assertTrue(entity.getCallHistory().contains("stop"));
    }
}
