package com.google.sps.data;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserHelperTest {

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
  public void testCorrectUser() {
    addUser();
    assertTrue(userHelper.doesUserEmailExist(request));
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

  @Test
  public void testDiffUser() {
    addDiffUser();
    assertFalse(userHelper.doesUserEmailExist(request));
  }

  @Test
  public void testNullEmailStored() {
    createNullEmailStored();
    assertFalse(userHelper.doesUserEmailExist(request));
  }

  private void setCurrentUser() {
    when(userPrincipal.getName()).thenReturn("test@email.com");
    when(request.getUserPrincipal()).thenReturn(userPrincipal);
  }

  private void setcurrentEmailNull() {
    when(userPrincipal.getName()).thenReturn(null);
    System.out.println(userPrincipal.getName());
    when(request.getUserPrincipal()).thenReturn(userPrincipal);
  }

  private void addUser() {
    setCurrentUser();
    Entity testEntity = new Entity("UserData");
    testEntity.setProperty("Email", "test@email.com");
    testEntity.setProperty("Type", 2);
    ds.put(testEntity);

    assertEquals(1, ds.prepare(new Query("UserData")).countEntities(withLimit(10)));
  }

  private void addDiffUser() {
    setCurrentUser();
    Entity testEntity = new Entity("UserData");
    testEntity.setProperty("Email", "hi@email.com");
    testEntity.setProperty("Type", 2);
    ds.put(testEntity);

    assertEquals(1, ds.prepare(new Query("UserData")).countEntities(withLimit(10)));
  }

  private void addNullUser() {
    setcurrentEmailNull();
    Entity testEntity = new Entity("UserData");
    testEntity.setProperty("Email", "test@email.com");
    testEntity.setProperty("Type", 3);
    ds.put(testEntity);

    assertEquals(1, ds.prepare(new Query("UserData")).countEntities(withLimit(10)));
  }

  private void addNullType() {
    setcurrentEmailNull();
    Entity testEntity = new Entity("UserData");
    testEntity.setProperty("Email", request.getUserPrincipal().getName());
    testEntity.setProperty("Type", 4);
    ds.put(testEntity);

    assertEquals(1, ds.prepare(new Query("UserData")).countEntities(withLimit(10)));
  }

  private void createNullEmailStored() {
    setCurrentUser();
    Entity testEntity = new Entity("UserData");
    testEntity.setProperty("Email", null);
    testEntity.setProperty("Type", 2);
    ds.put(testEntity);

    assertEquals(1, ds.prepare(new Query("UserData")).countEntities(withLimit(10)));
  }
}
