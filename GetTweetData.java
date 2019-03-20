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
    
    //sets authentication keys
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setOAuthConsumerKey("***");
        config.setOAuthConsumerSecret("***");
        config.setOAuthAccessToken("***-***");
        config.setOAuthAccessTokenSecret("***");

        TwitterFactory twitFac = new TwitterFactory();
        twitter4j.Twitter twit = twitFac.getInstance();
        twit.tweets();
        
        //gets mentions and sends the user information from those mentions to be data mined
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
        
        //prints out the user tweet
        try {
            TwitterBot theBot = new TwitterBot();
            twit.updateStatus(theBot.makeTweet());
        } catch (Exception e){
            e.printStackTrace();
        }

    }


//writes that users tweets and retweets to a file
    public static void writeToFile(User theUser, Twitter twit) {
        try {
            String userName = theUser.getScreenName();
            FileWriter f = new FileWriter("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/userTweetData.txt");
            BufferedWriter bf = new BufferedWriter((f));
            bf.write("@" + userName + "\n");
            int numStatus = theUser.getStatusesCount();
            
            //sleep try-catch block to deal with twitter rate limitor
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
                    
                    //deal with twitter rate limitor, I dont want my twitter bot to be banned
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
