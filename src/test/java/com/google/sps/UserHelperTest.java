package com.google.sps.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;



 public  class UserHelperTest {
  
  private final LocalServiceTestHelper helper =
  new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  
  @Mock private HttpServletRequest request;
  @Mock private Principal userPrincipal;
  private UserHelper userHelper; 
  private DatastoreService ds;

  @Before
  public void setUp() {
    helper.setUp();
    ds = DatastoreServiceFactory.getDatastoreService();
    userHelper = new UserHelper(ds);
    MockitoAnnotations.initMocks(this);
  }


  @After
  public void tearDown() {
    helper.tearDown();
  }


  @Test
  public void testUser() {
    addUser();
    assertEquals(true, userHelper.doesUserEmailExist(request));
  }


  @Test
  public void testNullUser() {
    addNullUser();
    assertFalse(userHelper.doesUserEmailExist(request));
  }


  @Test
  public void testNullType() {
    addNullType();
    assertFalse(userHelper.doesUserEmailExist(request));
  }


  private void setUserName() {
    when(userPrincipal.getName()).thenReturn("test@email.com");
    System.out.println(userPrincipal.getName());
    when(request.getUserPrincipal()).thenReturn(userPrincipal);
  }
  

  private void addUser() {
    setUserName();
    Entity testEntity = new Entity("testData");
    testEntity.setProperty("Email", request.getUserPrincipal().getName());
    testEntity.setProperty("Type", "BOTH");
    ds.put(testEntity);
  }


  private void setUserNameNull() {
    when(userPrincipal.getName()).thenReturn(null);
    System.out.println(userPrincipal.getName());
    when(request.getUserPrincipal()).thenReturn(userPrincipal);
  }


  private void addNullUser() {
    setUserNameNull();
    Entity testEntity = new Entity("testData");
    testEntity.setProperty("Email", null);
    testEntity.setProperty("Type", "BOTH");
    ds.put(testEntity);
  }


  private void addNullType() {
    setUserNameNull();
    Entity testEntity = new Entity("testData");
    testEntity.setProperty("Email", request.getUserPrincipal().getName());
    testEntity.setProperty("Type", null);
    ds.put(testEntity);
  }


}
 