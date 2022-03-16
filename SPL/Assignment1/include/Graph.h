#ifndef GRAPH_H_
#define GRAPH_H_

#include <vector>
#include <string>

class Graph{
public:
    Graph();
    virtual ~Graph();
    Graph(const Graph& g);
    Graph(Graph&& g);
    Graph& operator=(const Graph& g);
    Graph& operator=(Graph&& g);
    void copy(const Graph& g);

    Graph(std::vector<std::vector<int>> matrix);

    void infectNode(int nodeInd);
    bool isInfected(int nodeInd);
    std::vector<int> getInfected();
    void setEdges(std::vector<std::vector<int>> v);
    int getSize() const;
    void isolate(int x);
    std::vector<int> getNei(int x);
    void print() const;//debugging function
    std::vector<std::vector<int>> getEdges();
private:
    std::vector<std::vector<int>> edges;
    std::vector<bool> nodes;
};
#endif
