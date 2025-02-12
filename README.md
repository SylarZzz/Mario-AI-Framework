See [here](https://github.com/amidos2006/Mario-AI-Framework) for the original
project's readme.

For the first phase of this project, we implemented a Markov-chain based level
generator (in `src/levelGenerators/jppetitti_gferguson_generator`) that reads in
all levels in a directory, splits them into one-column chunks, builds a Markov
transition table based on the probability that a given chunk follows another,
and then generates a level based on it.

For phase two, we built a decision tree-based AI agent (in the directory
`src/agents/gferguson_jppetitti`) that is supposed to look like a human player
while playing original Mario levels.
