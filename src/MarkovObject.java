import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/*
    Class by Nick Wille, generates a markov object, step lenth of 1,
    Meant for twitter so length of markov chain is capped at 260 chars
 */
public class MarkovObject {

    private String markovChain;
    private Map markovMap;
    private CoreNLPProtos.Sentiment theSentiment;
    private ArrayList<Integer> sentimentScores;

    /*
            Generates a markov chain from an existing file on my computer, markov step length of 1
     */
    public void analyzeText() throws IOException {
        final int MAX_NUM_CHARS = 260;

        //initialization of variables for future use
        Map<String, ArrayList<String>> theData = new HashMap<String, ArrayList<String>>();
        Path thePath = Paths.get("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/UserTweetData.txt");
        byte[] theBytes = Files.readAllBytes(thePath);

        //splits data file, stores an array of string seperated by spaces
        String[] splitWords = new String(theBytes).trim().split(" ");
        String userName = splitWords[0];

        //loops through array of string
        for (int i = 1; i < splitWords.length - 2; i++) {

            //lines 38-53 put the word in the hashmap, but also check, to comply with
            //twitter guidelines and not be a nuisance, any strings using the '@' char
            //that could be tagging other users are avoided
            String tempString = splitWords[i];

            //nested conditionals the avoid tagging others can be removed with future iterations of main class
            if (!tempString.contains('@' + "")) {
                if (theData.containsKey(tempString)) {
                    if(splitWords[i + 1].contains("@"))
                        theData.get(tempString).add(splitWords[i + 2]);
                    else
                        theData.get(tempString).add(splitWords[i + 1]);
                } else {
                    ArrayList<String> words = new ArrayList<>();
                    if(!splitWords[i + 1].contains("@"))
                        words.add(splitWords[i + 1]);
                    else
                        words.add(splitWords[i + 2]);
                    theData.put(tempString, words);
                }
            }
        }

        //Initializes markov chain
        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.append(" ");
        Random random = new Random();
        int charCount = userName.length() + 1;
        String currentWord = splitWords[random.nextInt(splitWords.length)];

        //regenerates initial word to avoid tagging calling user twice
        while(currentWord.contains("@")){
            currentWord = splitWords[random.nextInt(splitWords.length)];
        }

        //appends the next words in the generated text
        while (charCount < MAX_NUM_CHARS) {
            if (theData.get(currentWord) == null) {
                currentWord = splitWords[random.nextInt(splitWords.length)];
            }
            int num = random.nextInt(theData.get(currentWord).size());
            String nextWord = theData.get(currentWord).get(num);
            sb.append(nextWord);
            sb.append(" ");

            charCount += nextWord.length() + 1;
            currentWord = nextWord;
        }
        markovMap = theData;
       markovChain = sb.toString();

    }

    //getter method for the chain
    public String getMarkovChain(){
        return markovChain;
    }

    //regenerates the chain
    public void regenerateChain() throws IOException{
        analyzeText();
    }

    //initializes the chain and map to any data file
    public MarkovObject() throws IOException {
        try {
            analyzeText();
        } catch (Exception e){
            //do nothing,
        }
    }

    //initializes the chain from a given data file
    public MarkovObject(File file) throws IOException {
        try {
            analyzeText(file);
        } catch (Exception e){
            //do nothing,
        }
    }

    //returns the map for debugging
    public Map debugMap(){
        return markovMap;
    }

    /*
            Generates a markov chain from an existing text file, markov step length of 1
     */
    public void analyzeText(File file) throws IOException {
        final int MAX_NUM_CHARS = 260;

        //initialization of variables for future use
        Map<String, ArrayList<String>> theData = new HashMap<String, ArrayList<String>>();
        Path thePath = Paths.get("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/UserTweetData.txt");
        byte[] theBytes = Files.readAllBytes(thePath);

        //splits data file, stores an array of string seperated by spaces
        String[] splitWords = new String(theBytes).trim().split(" ");
        String userName = splitWords[0];

        //loops through array of string
        for (int i = 1; i < splitWords.length - 2; i++) {

            //lines 38-53 put the word in the hashmap, but also check, to comply with
            //twitter guidelines and not be a nuisance, any strings using the '@' char
            //that could be tagging other users are avoided
            String tempString = splitWords[i];

            //nested conditionals the avoid tagging others can be removed with future iterations of main class
            if (!tempString.contains('@' + "")) {
                if (theData.containsKey(tempString)) {
                    if(splitWords[i + 1].contains("@"))
                        theData.get(tempString).add(splitWords[i + 2]);
                    else
                        theData.get(tempString).add(splitWords[i + 1]);
                } else {
                    ArrayList<String> words = new ArrayList<>();
                    if(!splitWords[i + 1].contains("@"))
                        words.add(splitWords[i + 1]);
                    else
                        words.add(splitWords[i + 2]);
                    theData.put(tempString, words);
                }
            }
        }

        //Initializes markov chain
        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.append(" ");
        Random random = new Random();
        int charCount = userName.length() + 1;
        String currentWord = splitWords[random.nextInt(splitWords.length)];

        //regenerates initial word to avoid tagging calling user twice
        while(currentWord.contains("@")){
            currentWord = splitWords[random.nextInt(splitWords.length)];
        }

        //appends the next words in the generated text
        while (charCount < MAX_NUM_CHARS) {
            if (theData.get(currentWord) == null) {
                currentWord = splitWords[random.nextInt(splitWords.length)];
            }
            int num = random.nextInt(theData.get(currentWord).size());
            String nextWord = theData.get(currentWord).get(num);
            sb.append(nextWord);
            sb.append(" ");

            charCount += nextWord.length() + 1;
            currentWord = nextWord;
        }
        markovMap = theData;
        markovChain = sb.toString();
    }

    //gets sentiment of a string
    public String getSentiment(){
        StringBuilder sb = new StringBuilder();
        int num = 0;
        String sentimentOverall = "";
        //cycles through the sentimentScores and determines the overall sentiment,
        //sentiments weight with positive and negative numbers representing positive and negative emotions
        for(int i = 0; i < sentimentScores.size(); i++){
            num += sentimentScores.get(i);
        }
        if(num == 0){
             sentimentOverall = "neutral";
        } else {
            sentimentOverall = num > 0 ? "happy" : "unhappy";
        }
        sb.append(", generally, your tweets are ");
        sb.append(sentimentOverall);
        sb.append(".");
        return sb.toString();
    }

    //Starts the natural language processing, using StanfordCoreNLP
    public void startNLP() throws IOException {
        Properties attributes = new Properties();
        ArrayList<Integer> sentiments = new ArrayList<>();
        attributes.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipe = new StanfordCoreNLP(attributes);
        Map<String, ArrayList<String>> theData = new HashMap<String, ArrayList<String>>();
        Path thePath = Paths.get("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/UserTweetData.txt");
        byte[] theBytes = Files.readAllBytes(thePath);
        String stringOfText = new String(theBytes);
        Annotation annotation = new Annotation(stringOfText);
        pipe.annotate(annotation);
        for(CoreMap s : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree annotatedTree = s.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(annotatedTree);
            sentiments.add(score);
        }
        sentimentScores = sentiments;
    }

    public String getSentimentTrajectory() {
        final int QUADRANT_NUMBER  = 4;
        StringBuilder sb = new StringBuilder();
        sb.append("Your tweets are getting ");
        int[] theScores = new int[QUADRANT_NUMBER];
        for(int i = 0; i < QUADRANT_NUMBER; i++) {
            for(int j = 0; j < sentimentScores.size()/QUADRANT_NUMBER; j++){
                theScores[i] += sentimentScores.get(j + i * QUADRANT_NUMBER);
            }
        }
        if(theScores[1] < theScores[2] && theScores[2] < theScores[3] && theScores[3] < theScores[4){
            sb.append("always happier!");
        } else
        if(theScores[1] > theScores[2] && theScores[2] < theScores[3] && theScores[3] < theScores[4){
            sb.append("recently happier!");
        } else
        if(theScores[1] > theScores[2] && theScores[2] > theScores[3] && theScores[3] > theScores[4){
            sb.append("always less happy...");
        } else
        if(theScores[1] > theScores[2] && theScores[2] < theScores[3] && theScores[3] > theScores[4){
            sb.append("mostly unhappy...");
        } else {
            sb.append("emotionally confusing...");
        }
        return sb.toString();
    }
}
