package ru.shabashoff;

import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Records;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import ru.shabashoff.decision.Decision;
import ru.shabashoff.decision.DecisionTree;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class PaintTest {

    private int id = 1;

    @Test
    public void paintTree() throws IOException {
        DecisionTree<String> dt = DecisionTree.loadFromFile("testSample.dt");

        /*
        Node node1 = node("node2").with(Records.of(rec("id", "2")));
        node0 = node0.link(node1);*/
        Node node0 = node("node1").with(Records.of(rec("id", "1")));
        node0 = fillLinks(node0, dt.getHead());

        Graph g = graph("example3")
            .directed()
            .graphAttr().with(RankDir.TOP_TO_BOTTOM)
            .with(node0);

        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("x.png"));
    }


    private Node fillLinks(Node node, ru.shabashoff.decision.Node<String> nt) {
        Node n;

        if (id > 200) {
            return node;
        }

        if (nt instanceof Decision) {
            Decision<String> tt = (Decision<String>) nt;
            n = node("" + (id++)).with(Records.of(
                rec("index", "" + tt.getIndexOfParameter()),
                rec("ifstmt", tt.getIfStmt().toString()),
                rec("index", "" + tt.getVal())
            ));
            n = fillLinks(n, tt.getLeft());
            n = fillLinks(n, tt.getRight());
        }
        else {
            n = node("" + (id++)).with(Records.of(rec("id", nt.run(null))));
        }

        return node.link(n);
    }

}
