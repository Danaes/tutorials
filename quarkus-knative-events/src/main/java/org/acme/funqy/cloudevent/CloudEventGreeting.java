package org.acme.funqy.cloudevent;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;

import org.jboss.logging.Logger;

public class CloudEventGreeting {
    private static final Logger log = Logger.getLogger(CloudEventGreeting.class);

    @Funq
    @CloudEventMapping(trigger = "firstEvent", responseSource = "myFirstFunction", responseType = "secondEvent")
    public String myFirstFunction(String input, @Context CloudEvent cloudEvent) {
        log.info("** myFirstFunction **");
        return input + " - " + cloudEvent.type();
    }

    @Funq
    @CloudEventMapping(trigger = "secondEvent", responseSource = "mySecondFunction", responseType = "lastEvent")
    public String mySecondFunction(String input, @Context CloudEvent cloudEvent) {
        log.info("** mySecondFunction **");
        return input + " - " + cloudEvent.type();
    }

    @Funq
    @CloudEventMapping(trigger = "lastEvent")
    public void myLastFunction(String input, @Context CloudEvent cloudEvent) {
        log.info("** myLastFunction **");
        log.info(input + " - " + cloudEvent.type());
    }
}
