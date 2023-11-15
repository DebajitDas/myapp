package org.main;

import org.consumer.RunAndWicketConsumer;
import org.producer.RunAndWicketProducer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Application Started");

        RunAndWicketProducer producer = new RunAndWicketProducer();
        producer.start();

        RunAndWicketConsumer consumer = new RunAndWicketConsumer();
        consumer.start();
    }
}