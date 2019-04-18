import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
        Main class for TwitterBot sends the tweet
 */
public class GetTweetData {
    public static void main(String[] args) {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setOAuthConsumerKey("DTzbV5XE0IENqeQg1FM3RopDD");
        config.setOAuthConsumerSecret("hvMtr5CUxJtfr73sCxrqfpA74wSMvE4oZ3xedUxxHRWpeqFtGx");
        config.setOAuthAccessToken("1063205806525558784-PVTGKaNqjLUkgypzM0R7eG2DTa9UDu");
        config.setOAuthAccessTokenSecret("skIiGVCMHhyK6hr2yPcpNUoybFryedJnJrC1AD5SRODAS");

        TwitterFactory twitFac = new TwitterFactory();
        twitter4j.Twitter twit = twitFac.getInstance();
        twit.tweets();
        try {
            String test2 = "";
            ResponseList<Status> listOfMentions = twit.getMentionsTimeline();
            for (Status s : listOfMentions) {
                System.out.println("Status " + s);
                User m = s.getUser();
                test2 = m.getScreenName();
                    writeToFile(m, twit);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Unable to continue, see stacktrace");
                }
            }
            System.out.println(test2);
        } catch (TwitterException e) {
            System.out.println("There is something wrong with the twitter object");
            e.printStackTrace();
        }
        try {
            TwitterBot theBot = new TwitterBot();
            MarkovObject temp = new MarkovObject();
            temp.analyzeText();
            temp.startNLP();

//            twit.updateStatus(theBot.makeTweet());
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void writeToFile(User theUser, Twitter twit) {
        try {
            String userName = theUser.getScreenName();
            FileWriter f = new FileWriter("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/userTweetData.txt");
            BufferedWriter bf = new BufferedWriter((f));
            bf.write("@" + userName + "\n");
            int numStatus = theUser.getStatusesCount();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Unable to continue, see stacktrace");
            }
            try {
                TwitterFactory ok = new TwitterFactory();
                twitter4j.Twitter twit2 = ok.getInstance();
                ResponseList<Status> userStatus = twit.getUserTimeline(userName);
                for (Status s : userStatus) {
                   String[] a =  s.getText().trim().split(" ");
                    for(int i = 0; i < a.length; i++){
                        if(!a[i].contains("@")){
                            bf.write(a[i] + " ");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Unable to continue, see stacktrace");
                    }
                }
                bf.close();
                f.close();
            } catch (TwitterException e) {
                //oops
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
