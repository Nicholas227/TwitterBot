import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.IOException;
/*
    TwitterBot object, can tweet and debug a markov map,
 */
public class TwitterBot {


    //returns and prints out the map of generated text to check for errors
    public String debugTwitterBot() throws IOException{
        MarkovObject testingObject = new MarkovObject();
        System.out.println(testingObject.debugMap());
        return testingObject.debugMap().toString();
    }

    //Tweets out the markov Chain
    public String makeTweet() throws IOException{
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;
        try {
            MarkovObject markovObject = new MarkovObject();
            status = twitter.updateStatus(markovObject.getMarkovChain());
            System.out.println(status);
            return markovObject.getMarkovChain();
        } catch (TwitterException e) {;
            e.printStackTrace();
        }
        return "";
    }
}