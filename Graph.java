package main;

import edu.princeton.cs.algs4.In;
import org.antlr.v4.runtime.tree.Tree;

import java.lang.reflect.Array;
import java.util.*;

public class Graph {
    private ArrayList<TreeSet<Integer>> adjacents;
    private List<Integer> edgeTo;
    private Map<String, TreeSet<Integer>> wordMap;
    private Map<Integer, TreeSet<String>> synset;

    private Map<Integer, TreeSet<Integer>> connections;
    private Map<String, TreeSet<String>> allSynsets;
    public Graph() {
        adjacents = new ArrayList<>();
        edgeTo = new ArrayList<>();
        wordMap = new HashMap<>();
        synset = new HashMap<>();
        connections = new HashMap<>();
    }

    public void synsDecode(String synFile) {
        In in  = new In(synFile);
        while (!in.isEmpty()) {
            String next = in.readLine();
            int index = next.indexOf(",");
            int id = Integer.parseInt(next.substring(0, index));
            int index2 = next.substring(index + 1).indexOf(",");
            String syns = next.substring(index + 1).substring(0, index2);
            synset.put(id, new TreeSet<>());
            adjacents.add(new TreeSet<>());
            connections.put(id, new TreeSet<>());
            for (String i : syns.split(" ")) {
                synset.get(id).add(i);
                if (wordMap.containsKey(i)) {
                    wordMap.get(i).add(id);
                } else {
                    wordMap.put(i, new TreeSet<>());
                    wordMap.get(i).add(id);
                }
            }
            edgeTo.add(-1);
        }
    }
    public void hypDecode(String hypFile) {
        In in  = new In(hypFile);
        while (!in.isEmpty()) {
            String next = in.readLine();
            List<Integer> edges = new ArrayList<>();
            List<String> tokens = Arrays.asList(next.split(","));
            for (String i : tokens) {
                edges.add(Integer.parseInt(i));
            }
            if (edges.size() > 1) {
                for (int i : edges.subList(1, edges.size())) {
                    edgeTo.set(i, edges.get(0));
                    adjacents.get(edges.get(0)).add(i);
                }
            }
        }

    }
//    public boolean isConnected(int high, int low, int initialLow) {
//        if (low == -1 && high != 0) {
//            return false;
//        } else if (low == high) {
//
//            return true;
//        } else if (low == -1 && high == 0) {
//            return true;
//        } else {
//            return isConnected(high, edgeTo.get(low), low);
//        }
//
//    }
    public TreeSet<String> getHyponyms(String word) {
        TreeSet<String> hyponyms1 = new TreeSet<>();
        TreeSet<Integer> temp = new TreeSet<>();
        if (wordMap.containsKey(word)) {
            Set<Integer> y = wordMap.get(word);
            temp.addAll(y);
            for (int i : y) {
                temp.addAll(getAdjacents(i));
            }
        } else {
            return hyponyms1;
        }
        for (int i : temp) {
            hyponyms1.addAll(synset.get(i));
        }
//        Collections.sort(hyponyms1);
//        hyponyms1.sort(Comparator.naturalOrder());
        return hyponyms1;
    }

//    public int getEdgeTo(int i) {
//        return edgeTo.get(i);
//    }

    public TreeSet<Integer> getAdjacents(int i) {
        if (connections.get(i).size() > 0) {
            return connections.get(i);
        }
        TreeSet<Integer> allAdj = new TreeSet<>();
        if (adjacents.get(i) == null) {
            return allAdj;
        }
        for (int j : adjacents.get(i)) {
            allAdj.add(j);
            Set<Integer> temp = getAdjacents(j);
            allAdj.addAll(temp);
            connections.get(j).addAll(temp);
        }
        connections.get(i).addAll(allAdj);
        return allAdj;
    }

    public TreeSet<String> listWords(List<String> words) {
        TreeSet<String> result = new TreeSet<>();
        if (words.size() > 1) {
            if (!wordMap.containsKey(words.get(0)) || !wordMap.containsKey(words.get(0))){
                return new TreeSet<>();
            }
            result = combine(getHyponyms(words.get(0)), getHyponyms(words.get(1)));
            for (String i : words.subList(2, words.size())) {
                if (!wordMap.containsKey(i)) {
                    return new TreeSet<>();
                }
                result = combine(result, getHyponyms(i));
            }
        } else if (words.size() == 1) {
            return getHyponyms(words.get(0));
        }
//        result.sort(Comparator.naturalOrder()); //May not need this later on ***
        return result;
    }

    public TreeSet<String> combine(Set<String> words1, Set<String> words2) {
        TreeSet<String> result = new TreeSet<>();
        if (words1 != null) {
            for (String i : words1) {
                if (words2.contains(i)) {
                    result.add(i);
                }
            }
        }
        return result;
    }
    public static String toString(Set<String> convert) {
        return Arrays.toString(convert.toArray());
//        return toString(convert.toArray())
//        String ret = "[";
//        if (convert != null) {
//            for (String i : convert) {
//                if (ret.endsWith(",")) {
//                    ret = ret + " " + i + ",";
//                }
//                else {
//                    ret = ret + i + ",";
//                }
//            }
//            if (ret.endsWith(",")) {
//                ret = ret.substring(0, ret.lastIndexOf(","));
//            }
//        }
//        ret += "]";
//        return ret;
    }
}
