import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class analyzeText {
    public static String analyzeText1() throws IOException {
        final int MAX_NUM_CHARS = 280;
        Map<String, ArrayList<String>> theData = new HashMap<String, ArrayList<String>>();
        Path thePath = Paths.get("/Users/nick/IdeaProjects/TwitterBot/src/main/resources/UserTweetData.txt");
        byte[] theBytes = Files.readAllBytes(thePath);
        String[] splitWords = new String(theBytes).trim().split(" ");
        String userName = splitWords[0];

        for (int i = 1; i < splitWords.length - 2; i++) {
            String tempString = splitWords[i];
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
        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.append(" ");
        Random random = new Random();
        int charCount = userName.length() + 1;
        String currentWord = splitWords[random.nextInt(splitWords.length)];
        while(currentWord.contains("@")){
            currentWord = splitWords[random.nextInt(splitWords.length)];
        }
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
        return sb.toString();

    }





        public static void main(String[] args) throws IOException{
            System.out.println(analyzeText1());
        }
    }

