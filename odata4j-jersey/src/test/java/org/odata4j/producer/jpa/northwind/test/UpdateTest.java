package org.odata4j.producer.jpa.northwind.test;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.jersey.consumer.ODataJerseyConsumer;
import org.odata4j.jersey.consumer.behaviors.OClientBehaviors;

public class UpdateTest extends JPAProducerTestBase {

  @BeforeClass
  public static void setUpClass() throws Exception {
    setUpClass(20);
  }

  @Test
  public void tunneledUpdateEntity() {
    ODataConsumer consumer = ODataJerseyConsumer.newBuilder(endpointUri).setClientBehaviors(OClientBehaviors.methodTunneling("PUT")).build();

    updateEntityAndTest(consumer);
  }

  @Test
  public void updateEntity() {
    ODataConsumer consumer = ODataJerseyConsumer.create(endpointUri);

    updateEntityAndTest(consumer);
  }

  protected void updateEntityAndTest(ODataConsumer consumer) {
    OEntity customer = consumer.getEntity("Customers", "ALFKI").execute();

    boolean ret = consumer
        .updateEntity(customer)
        .properties(OProperties.string("ContactName", "Maria Gleich"))
        .execute();
    Assert.assertTrue(ret);

    customer = consumer.getEntity("Customers", "ALFKI").execute();
    Assert.assertEquals("Maria Gleich", customer.getProperty("ContactName").getValue());
    Assert.assertEquals("Alfreds Futterkiste", customer.getProperty("CompanyName").getValue());
  }

  @Test
  public void mergeEntityTest() {
    ODataConsumer consumer = ODataJerseyConsumer.create(endpointUri);

    final long now = System.currentTimeMillis();
    boolean res = consumer
        .mergeEntity("Categories", 1)
        .properties(OProperties.string("Description", "D" + now))
        .execute();

    Assert.assertTrue(res);

    OEntity category = consumer
        .getEntity("Categories", 1)
        .execute();

    Assert.assertEquals("Beverages", category.getProperty("CategoryName").getValue());
    Assert.assertEquals("D" + now, category.getProperty("Description").getValue());
  }

}