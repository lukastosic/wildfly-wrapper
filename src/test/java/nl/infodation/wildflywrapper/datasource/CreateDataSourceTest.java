package nl.infodation.wildflywrapper.datasource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.infodation.wildflywrapper.common.*;
import nl.infodation.wildflywrapper.datasource.DataSourceActions;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateDataSourceTest 
   
{
       
    private WildflyConnector wildfly;
    private DataSourceActions actions;
    
    @Before
    public void PrepareForTest()
    {
    	wildfly = new WildflyConnector("10.4.1.218", 9601, "admin", "password");
        actions = new DataSourceActions("mariaDS", "java:/MariaDBDS", "mysql", "10.4.1.218", "3306", "test", "root", "root");
    }
  
    
    @Test
    public void test_1_AddNewConnectionSuccess()
    {       	
        assertEquals("Expected to create connection", true, actions.createJDBCDataConnection(wildfly));        
    }
    
    @Test
    public void test_2_AddNewConnectionAlreadyExist()
    {
    	assertEquals("Expected to fail on existing connection name", false, actions.createJDBCDataConnection(wildfly));
    }
    
    @Test
    public void test_3_RemoveExistingConnection()
    {
    	assertEquals("Expected to remove existing connection", true, actions.removeJDBCDataSource(wildfly));
    }
}
