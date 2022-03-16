//
// Created by spl211 on 09/11/2020.
//

#ifndef AGENT
#define AGENT

#include <iostream>
#include "Agent.h"
#include "Tree.h"

    Agent::Agent() = default;
    Agent::~Agent(){}

    ContactTracer::~ContactTracer(){}
    ContactTracer::ContactTracer(): Agent() { };

    int ContactTracer::getID() { return -1; }
    void ContactTracer::act(Session& session){
        int x(session.dequeueInfected());
        if (x != -1) {
            Tree* t = Tree::createTree(session, x);
            x = t->traceTree();
            session.isolate(x);
            delete (t);
        }
    }
    Agent* ContactTracer::Clone() const{
    return new ContactTracer();
    }
    bool ContactTracer::checkStop(Session& session) {return true;}

    Virus::~Virus() {}
    Virus::Virus(Virus &v): nodeInd(v.nodeInd){}
    Virus::Virus(Virus &&v): nodeInd(v.nodeInd){}
Virus::Virus(int nodeInd): Agent(), nodeInd(nodeInd) {}
    bool Virus::checkStop(Session& session) {
        for (int i = 0; i < session.getGraph().getSize(); ++i) {
            if (session.isNeiAndNoInf(nodeInd,i))
                return false;
        }
        if (!session.isInfected(nodeInd)) return false;
        return true;
    }
    int Virus::getID() { return nodeInd; }

    void Virus::act(Session& session) {

        if(!session.isInfected(nodeInd))
        {
            session.infectNode(nodeInd);
            session.enqueueInfected(nodeInd);
        }
        int i=0;
        while (i<session.getGraph().getSize() && !session.isEnqueue(nodeInd, i))
        {
            i++;
        }
        if (i<session.getGraph().getSize()) {
            Virus* v = new Virus(i);
            session.addAgent(*v);
            delete (v);
        }

    }
    Agent* Virus::Clone() const{
    return new Virus(nodeInd);
}



#endif