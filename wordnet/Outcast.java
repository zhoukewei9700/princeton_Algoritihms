import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet myNet;

    // private final int[] distanceList;
    public Outcast(WordNet wordnet) {
        myNet = wordnet;
        // distanceList =
    }       // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int maxDist = 0;
        String resNoun = null;
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                dist += myNet.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                resNoun = nouns[i];
            }
        }
        return resNoun;
    } // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] files = new String[] { "outcast5.txt", "outcast8.txt", "outcast11.txt" };
        for (int t = 0; t < files.length; t++) {
            In in = new In(files[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(files[t] + ": " + outcast.outcast(nouns));
        }
    } // see test client below
}
