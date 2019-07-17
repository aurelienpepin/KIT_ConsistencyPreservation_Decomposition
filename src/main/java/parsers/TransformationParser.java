package parsers;

import metamodels.Metagraph;

public class TransformationParser {
    
    public static void fillGraphWith(Metagraph graph, String qvtrFilePath) {
        if (graph == null)
            throw new RuntimeException("The metagraph cannot be null");
    }
}

// CF. https://github.com/haslab/echo/blob/6c35e510204fc71dcd2996fe1e084ed049408bb3/plugins/pt.uminho.haslab.echo/src/pt/uminho/haslab/mde/EMFParser.java