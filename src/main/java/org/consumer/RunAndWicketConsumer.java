package org.consumer;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.constants.Constants;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RunAndWicketConsumer extends Thread{
    Consumer<String, String> consumer;
    ScoreBoard board;
    public RunAndWicketConsumer() {
        consumer = ConsumerCreator.createConsumer();
        board = new ScoreBoard();
    }

    public void run() {
        System.out.println("Thread started");
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                continue;
            }

            consumerRecords.forEach(record -> {
                System.out.println("Record value " + record.value());
                String message = record.value();
                Object obj = JSONValue.parse(message);
                JSONObject jsonObject = (JSONObject) obj;

                if (jsonObject.containsKey(Constants.WICKET_KEY)) {
                    board.update((String)jsonObject.get(Constants.BATTING_KEY),
                            (String)jsonObject.get(Constants.BOWLING_KEY),
                            0, (double)jsonObject.get(Constants.BALL_KEY),
                            (boolean)jsonObject.get(Constants.WICKET_KEY), true);
                }
                else {
                    long localrun = (long)jsonObject.get(Constants.RUN_KEY);
                    board.update((String)jsonObject.get(Constants.BATTING_KEY),
                            (String)jsonObject.get(Constants.BOWLING_KEY),
                            localrun, (double)jsonObject.get(Constants.BALL_KEY),
                            false, false);
                }
            });

            // Prints the output before commit
            board.commit();
            try {
                consumer.commitSync();
            } catch (CommitFailedException e) {
                System.out.println("commit failed");
            }

        }
        //consumer.close();
    }
}
