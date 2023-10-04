package org.acme.funqy.cloudevent;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import io.quarkus.logging.Log;

import org.jboss.logging.Logger;

public class CloudEventGreeting {
    @Funq
    @CloudEventMapping(trigger = "com.example.order.created", responseSource = "https://example.com/createOrder", responseType = "com.example.order.items.blocked")
    public String createOrder(String input, @Context CloudEvent cloudEvent) {
        Log.info("** createOrder **");
        return input + " - " + cloudEvent.type();
    }

    @Funq
    @CloudEventMapping(trigger = "com.example.order.items.blocked", responseSource = "https://example.com/blockItems", responseType = "com.example.order.payed")
    public String blockItems(String input, @Context CloudEvent cloudEvent) {
        Log.info("** blockItems **");
        return input + " - " + cloudEvent.type();
    }

    @Funq
    @CloudEventMapping(trigger = "com.example.order.payed")
    public void payOrder(String input, @Context CloudEvent cloudEvent) {
        Log.info("** payOrder **");
        Log.infof("%s - %s", input, cloudEvent.type());
    }
}
