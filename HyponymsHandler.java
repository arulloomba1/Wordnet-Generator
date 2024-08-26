package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private Graph graph = new Graph();


    public HyponymsHandler(String wordFile, String countFile, String synsetFile, String hyponymFile) {
        graph.synsDecode(synsetFile);
        graph.hypDecode(hyponymFile);
    }


    @Override
    public String handle(NgordnetQuery q) {
        return Graph.toString(graph.listWords(q.words()));
    }

}

