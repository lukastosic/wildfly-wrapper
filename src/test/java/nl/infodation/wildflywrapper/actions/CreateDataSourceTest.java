package nl.infodation.wildflywrapper.actions;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.infodation.wildflywrapper.actions.DataSourceActions;
import nl.infodation.wildflywrapper.common.*;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateDataSourceTest 
   
{
       
    private WildflyConnector wildfly;
    private DataSourceActions actions;
    
    @Before
    public void PrepareForTest()
    {
    	// Update with values applicable to your testing environment    	
    	wildfly = new WildflyConnector("localhost", 9990, "admin", "password");
        actions = new DataSourceActions("mariaDS", "java:/MariaDBDS", "mysql", "localhost", "3306", "test", "efap", "efap", wildfly);
    }
  
    
    @Test
    public void test_1_AddNewConnectionSuccess()
    {       	
        assertEquals("Expected to create connection", true, actions.createJDBCDataConnection().resultStatus);        
    }
    
    @Test
    public void test_2_AddNewConnectionAlreadyExist()
    {
    	assertEquals("Expected to fail on existing connection name", false, actions.createJDBCDataConnection().resultStatus);
    }
    
    @Test
    public void test_3_RemoveExistingConnection()
    {
    	assertEquals("Expected to remove existing connection", true, actions.removeJDBCDataSource().resultStatus);
    }
}
