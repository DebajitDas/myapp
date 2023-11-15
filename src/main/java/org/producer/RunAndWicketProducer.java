package org.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.constants.Constants;
import org.consumer.RunAndWicketConsumer;
import org.model.*;

import java.util.Random;

public class RunAndWicketProducer extends Thread {
    Producer<String, String> runMessageProducer;
    Producer<String, String> wicketMessageProducer;

    public RunAndWicketProducer() {
        runMessageProducer = ProducerCreator.createProducer();
        wicketMessageProducer = ProducerCreator.createProducer();
    }

    public void run() {
        System.out.println("Producer started");

        int overs = Constants.total_overs;  // Specify the number of overs
        double start = 0.1;
        double step = 0.1;

        for (int inn = 1; inn <= 2; inn++) {
            String batting = "TeamX";
            String bowling = "TeamY";
            if (inn/2 == 0) {
                batting = "TeamY";
                bowling = "TeamX";
            }

            int count = 0;
            int lastwicketball = 0;
            int wicketCount = 0;
            for (int i = 0; i < overs; i++) {
                for (double j = start; j <= 0.6; j += step) {
                    count++;
                    double over = ((int) ((i + j) * 10)) / 10.0;
                    //System.out.println(over);

                    Random rand = new Random();
                    int run = rand.nextInt(6);
                    run = run == 5 ? 0 : run;
                    //System.out.println("Over: " + over + " Run: " + run);

                    if (count > lastwicketball) {
                        lastwicketball = lastwicketball + rand.nextInt(Constants.total_overs*6 - lastwicketball);
                    }
                    boolean out = false;
                    if (count == lastwicketball) {
                        out = true;
                        wicketCount++;
                        run = 0;
                    }

                    String runMessage = MessageCreator.CreateRunMessage(batting, bowling, over, run);
                    String wicketMessage = MessageCreator.CreateWicketMessage(batting, bowling, over, out);

                    ProducerRecord<String, String> record1 =
                            new ProducerRecord<>(Constants.run_topic, runMessage);
                    ProducerRecord<String, String> record2 =
                            new ProducerRecord<>(Constants.wicket_topic, wicketMessage);

                    try {
                        runMessageProducer.send(record1);
                        //System.out.println("Sent Run Record");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        wicketMessageProducer.send(record2);
                        //System.out.println("Sent Wicket Record");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (wicketCount == 10) {
                        break;
                    }
                }
                if (wicketCount == 10) {
                    break;
                }
            }
        }
    }
}
