package agents.gferguson_jppetitti;

/**
 * Interface for nodes of a decision tree, including leaf actions and question
 * nodes
 * @author Joseph Petitti
 */
public interface INode {
	boolean[] execute();
}
