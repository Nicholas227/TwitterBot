# TwitterBot
Automated Twitterbot
An automated twitter bot. The bot pulls the user who most recently mentioned you from your timeline. It processes that users tweets and retweets from the last 7 days, and automatically generates a markov chain based approximation of that users twitter activity. It then tweets this back at the user.
The bot is slow, I've included numerous sleep timers to avoid spamming any timelines as well as to avoid clashing with twitter developer guidelines. 
The bot makes use of the twitter4j wrapper library, as well as the natural language processing library StanfordCoreNLP
