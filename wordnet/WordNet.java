/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final int n;
    private final ArrayList<String> oriSynsets; // store original synset string
    private final ArrayList<Set<String>> synsets; // store the nouns in a index
    private final Map<String, Set<Integer>> indexTable; // store a noun's indexes
    private final Digraph wordNet; // the wordnet digraph
    private final SAP sapCounter;

    // constructor takes the name of the two input files
    public WordNet(String synsetsPath, String hypernymsPath) {
        if (synsetsPath == null || hypernymsPath == null) {
            throw new IllegalArgumentException("input for constructor can not be null");
        }
        oriSynsets = new ArrayList<>();
        synsets = new ArrayList<>();
        indexTable = new HashMap<>();
        readSynsets(synsetsPath);
        n = synsets.size();
        wordNet = new Digraph(n);
        readHypernyms(hypernymsPath);

        // check validity
        hasCycle();
        hasRoot();
        sapCounter = new SAP(wordNet);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return indexTable.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return indexTable.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in the word net");
        }
        return sapCounter.length(indexTable.get(nounA), indexTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("noun is not in the word net");
        }
        int index = sapCounter.ancestor(indexTable.get(nounA), indexTable.get(nounB));
        return oriSynsets.get(index);
    }

    private void readSynsets(String synsetsPath) {
        In in = new In(synsetsPath);
        String[] fields;
        while (!in.isEmpty()) {
            String line = in.readLine();
            fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            oriSynsets.add(id, fields[1]);
            Set<String> tmp = new HashSet<>();
            for (String noun : fields[1].split(" ")) {
                tmp.add(noun);
                if (!indexTable.containsKey(noun)) {
                    Set<Integer> vals = new HashSet<>();
                    vals.add(id);
                    indexTable.put(noun, vals);
                }
                else {
                    indexTable.get(noun).add(id);
                }
            }
            synsets.add(id, tmp);
        }
    }

    private void readHypernyms(String hypernymsPath) {
        In in = new In(hypernymsPath);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hypernymID = Integer.parseInt(fields[i]);
                wordNet.addEdge(id, hypernymID);
            }
        }
    }

    private void hasRoot() {
        // root's out degree is 0
        int rootNums = 0;
        for (int v = 0; v < n; v++) {
            if (wordNet.outdegree(v) == 0) {
                rootNums++;
            }
            if (rootNums >= 2) {
                throw new IllegalArgumentException("word net should not have more than one root");
            }
        }
    }

    private void hasCycle() {
        DirectedCycle cycle = new DirectedCycle(wordNet);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("word net should not have cylce");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
