Joseph Petitti and Grant Ferguson

Mario Turing Test

We created the following classes:

INode.java              - interface for a decision tree node
ActionNode.java         - decision tree leaf node
QuestionNode.java       - decision node with n branches
BinaryQuestionNode.java - decision node with two branches
Agent.java              - agent class containing a decision tree
DecisionTree.java       - class that contains an entire decision tree

All of these files are located in the `gfeguson_jppetitti` directory. To install
this agent, simply move this directory into `src/agents` in the Mario AI
Framework and edit `src/PlayLevel.java` to use the new agent.

Our project uses the A* class from the robinBaumgarten agent to provide some
pathfinding capabilities.

Link to video :
