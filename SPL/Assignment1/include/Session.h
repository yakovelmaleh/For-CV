#ifndef SESSION_H_
#define SESSION_H_

#include <vector>
#include <string>
#include <queue>
#include "Graph.h"



class Agent;

enum TreeType{
  Cycle,
  MaxRank,
  Root
};

class Session{
public:
    Session(const std::string& path);
    virtual  ~Session();
    Session(Session& s);
    Session(Session&& s);
    Session& operator=(Session& s);
    Session& operator=(Session&& s);
    void copy(Session& s);
    void simulate();
    void addAgent(const Agent& agent);
    int getCurr()const;
    const Graph& getGraph()const;
    bool isEnqueue(int node,int x);
    void isolate(int x);
    bool isInfected(int x);
    void infectNode(int x);
    bool checkStop();
    bool isNeiAndNoInf(int node, int x);
    void finish();

    
    void enqueueInfected(int);
    int dequeueInfected();
    TreeType getTreeType() const;
    void setCurr(int x);
    std::vector<std::vector<int>> getEdges();
    
private:
    Graph g;
    TreeType treeType;
    std::vector<Agent*> agents;
    std::queue<int> infected;
    int curr;
};

#endif
