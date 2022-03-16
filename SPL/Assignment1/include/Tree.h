#ifndef TREE_H_
#define TREE_H_

#include <vector>

class Session;

class Tree{
public:
    Tree(int rootLabel);
    Tree(const Tree& t);
    Tree(Tree&& t);
    virtual ~Tree();
    Tree& operator=(const Tree& t);
    Tree& operator=(Tree&& t);
    void copy(const Tree& t);
    int numberOfChildren();

    void addChild(const Tree& child);
    void print() const;
    static Tree* createTree(const Session& session, int rootLabel);
    virtual int traceTree()=0;
    int getRoot()const;
    std::vector<Tree*> getChildren() const;
    Tree& getChild(int i);

    virtual Tree* Clone() const=0;
private:
    int node;
    std::vector<Tree*> children;
};

class CycleTree: public Tree{
public:
    CycleTree(int rootLabel, int currCycle);
    virtual int traceTree();
    virtual ~CycleTree();
    CycleTree( CycleTree& c);
    CycleTree( CycleTree&& c);
    CycleTree& operator=( CycleTree& c);
    CycleTree& operator=( CycleTree&& c);
    Tree* Clone()const;
private:
    int currCycle;
};

class MaxRankTree: public Tree{
public:
    MaxRankTree(int rootLabel);
    virtual ~MaxRankTree();
    MaxRankTree(MaxRankTree& m);
    MaxRankTree(MaxRankTree&& m);
    MaxRankTree& operator=(MaxRankTree& m);
    MaxRankTree& operator=(MaxRankTree&& m);
    virtual int traceTree();
    Tree* Clone()const;
};

class RootTree: public Tree{
public:
    RootTree(int rootLabel);
    virtual ~RootTree();
    RootTree(RootTree& r);
    RootTree(RootTree&& r);
    RootTree& operator=(RootTree &t);
    RootTree& operator=(RootTree &&t);
    virtual int traceTree();
    Tree* Clone()const;
};

#endif
