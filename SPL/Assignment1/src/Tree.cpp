//
// Created by spl211 on 09/11/2020.
//
#ifndef TREE
#define TREE

#include <iostream>
#include "Tree.h"
#include "Session.h"

    Tree::Tree(int rootLabel): node(rootLabel), children(){}
    Tree::~Tree() {
    for (unsigned int i = 0; i < children.size(); ++i) {
        delete (children[i]);
    }}
    Tree::Tree(const Tree &t): node(t.node) , children(){copy(t);}
    Tree::Tree(Tree &&t): node(t.node), children(){
    children=t.children;
    t.children.clear();
}
    Tree& Tree::operator=(const Tree &t) {
        node=t.node;
        copy(t);
        return *this;
    }
    Tree& Tree::operator=(Tree &&t) {
    if (&t!=this) {
        node = t.node;
        std::vector<Tree *> v;
        children = t.children;
        t.children.clear();
    }
        return *this;
    }
    void Tree::copy(const Tree& t){
        for (unsigned int i = 0; i < t.children.size(); ++i) {
            addChild(*t.children[i]);
        }
}
    int Tree::numberOfChildren() {return children.size();}
    void Tree::addChild(const Tree &child) {
    Tree* temp=child.Clone();
    children.push_back(temp);
}
    int Tree::getRoot() const {return node;}
    Tree& Tree::getChild(int i) {return *children[i];}
    std::vector<Tree*> Tree::getChildren() const {
    return children;}


    Tree* Tree::createTree(const Session &session, int rootLabel) {
    Tree* tempTree;
    TreeType tt=session.getTreeType();
    if (tt==TreeType::Root)
        tempTree=new RootTree(rootLabel);
    else if (tt==TreeType::MaxRank)
        tempTree=new MaxRankTree(rootLabel);
    else
        tempTree=new CycleTree(rootLabel, session.getCurr());
    Graph g(session.getGraph());
    std::vector<bool> nodes;
    for (int i = 0; i < g.getSize(); ++i) {
        nodes.push_back(false);
    }

    std::queue<Tree*> q;
    q.push(tempTree);
    while (!q.empty())
    {
        Tree* p=q.front();
        q.pop();
        nodes[p->getRoot()]=true;
        std::vector<int> Nei(g.getNei(p->getRoot()));
        for (unsigned int i = 0; i < Nei.size(); ++i) {
            Tree* temp;
            if(Nei[i] & !nodes[i]) {
                if (tt==TreeType::Root)
                    temp=new RootTree(i);
                else if (tt==TreeType::MaxRank)
                    temp=new MaxRankTree(i);
                else
                    temp=new CycleTree(i,session.getCurr());
                nodes[i]=true;
                p->addChild(*temp);

                q.push(p->getChildren()[p->getChildren().size()-1]);
                delete (temp);
            }
        }
    }
    return tempTree;
}


    CycleTree::CycleTree(int rootLabel, int currCycle): Tree(rootLabel), currCycle(currCycle){}
    CycleTree::~CycleTree(){}
    CycleTree::CycleTree(CycleTree &c):Tree(c), currCycle(c.currCycle) {}
    CycleTree::CycleTree(CycleTree &&c):Tree(std::move(c)), currCycle(c.currCycle) {}
    CycleTree& CycleTree::operator=(CycleTree &c) {
    Tree::operator=(c);
    currCycle=c.currCycle;
        return *this;
    }
    CycleTree& CycleTree::operator=(CycleTree &&c) {
    Tree::operator=(std::move(c));
    currCycle=c.currCycle;
        return *this;
}
    int CycleTree::traceTree(){
    int num=currCycle;
    Tree* t(this);

    while ((num>0)& (t->numberOfChildren()>0))
    {
        t=&(t->getChild(0));
        num--;
    }
    int output=t->getRoot();
        return output;
    }

    Tree* CycleTree::Clone()const{
    CycleTree* temp=new CycleTree(getRoot(),currCycle);
    std::vector<Tree*> vec=getChildren();
    for (unsigned int i = 0; i < vec.size(); ++i) {
        temp->addChild(*vec[i]);
    }
    return temp;
}

    MaxRankTree::MaxRankTree(int rootLabel): Tree(rootLabel){}
    MaxRankTree::~MaxRankTree(){}
    MaxRankTree::MaxRankTree(MaxRankTree &m):Tree(m) {}
    MaxRankTree::MaxRankTree(MaxRankTree &&m):Tree(std::move(m)) {}
    MaxRankTree& MaxRankTree::operator=(MaxRankTree &m) {Tree::operator=(m); return *this;}
    MaxRankTree& MaxRankTree::operator=(MaxRankTree &&m) {Tree::operator=(std::move(m)); return *this;}
    int MaxRankTree::traceTree(){
        int max=numberOfChildren();
        int numMax=getRoot();
        Tree* curr = this;
        std::queue<Tree*> q;
        q.push(curr);
        while (!q.empty())
        {
            Tree* t=q.front();
            q.pop();
            if (t->numberOfChildren()>max)
            {
                max=t->numberOfChildren();
                numMax=t->getRoot();
            }
            for (int i = 0; i < t->numberOfChildren(); ++i) {
                q.push(&t->getChild(i));
            }
        }
        return numMax;
    }

    Tree* MaxRankTree::Clone() const {
        MaxRankTree* temp=new MaxRankTree(getRoot());
        std::vector<Tree*> vec=getChildren();
        for (unsigned int i = 0; i < vec.size(); ++i) {
            temp->addChild(*vec[i]);
        }
        return temp;
    }

    RootTree::RootTree(int rootLabel): Tree(rootLabel){}
    RootTree::~RootTree(){}
    RootTree::RootTree(RootTree &r):Tree(r) {}
    RootTree::RootTree(RootTree &&r):Tree(std::move(r)) {}
RootTree& RootTree::operator=(RootTree &r) {Tree::operator=(r); return *this;}
    RootTree& RootTree::operator=(RootTree &&r) {Tree::operator=(std::move(r)); return *this;}
    int RootTree::traceTree(){return getRoot();}
    Tree* RootTree::Clone() const {
    RootTree* temp=new RootTree(getRoot());
    std::vector<Tree*> vec=getChildren();
    for (unsigned int i = 0; i < vec.size(); ++i) {
        temp->addChild(*vec[i]);
    }
    return temp;
}

    void Tree::print() const {
    for (unsigned int i = 0; i < children.size(); ++i) {
        std::cout<< children[i]->getRoot()<<" child of "<< getRoot()<<" who has : "<<children.size()<<" children"<<std::endl;
        std::cout << children[i]->getRoot() << " has " << children[i]->getChildren().size() << " children" << std::endl;
        children[i]->print();
    }

}

#endif
