import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.File;
/*
    Class by Nick Wille, generates a markov object, step lenth of 1,
    Meant for twitter so length of markov chain is capped at 260 chars
 */
public class MarkovObject {

    private String markovChain;
    private Map markovMap;

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
    public MarkovObject() throws IOException{
        try {
            analyzeText();
        } catch (Exception e){
            //do nothing,
        }
    }

    //initializes the chain from a given data file
    public MarkovObject(File file) throws IOException{
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
}
