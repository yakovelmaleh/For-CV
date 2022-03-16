//
// Created by spl211 on 09/11/2020.
//
#ifndef SESSION
#define SESSION
#include "Session.h"
#include <fstream>
#include <iostream>
#include "json.hpp"
#include "Agent.h"
using namespace std;
using json=nlohmann::json;

    Session::Session(const std::string& path):g(),treeType(),agents(),infected(), curr(0) {

        ifstream ifs(path);
        json j = json::parse(ifs);
        g.setEdges(j["graph"].get<vector<vector<int>>>());
        string s = j["tree"];
        if ((s.at(0) == 'M')) {
            treeType = MaxRank;
        } else if ((s.at(0) == 'C')) {
            treeType = Cycle;
        } else {
            treeType = Root;
        }
        
        for (unsigned int i = 0; i < j["agents"].size(); ++i) {
            Agent *temp;
            if ((j["agents"][i][0].dump().at(1) == 'V')) {
                temp = new Virus(j["agents"][i][1]);
            } else {
                temp = new ContactTracer();
            }
            agents.push_back(temp);
        }

    }
    Session::~Session() {
        for (unsigned int i = 0; i < agents.size(); ++i) {
            delete (agents[i]);
        }
}
    Session::Session(Session &s) : g(s.g), treeType(s.treeType),agents(),infected(), curr(s.curr) {

        copy(s);}
    Session::Session(Session &&s):g(s.g), treeType(s.treeType),agents(s.agents),infected(s.infected), curr(s.curr)  {
        s.agents.clear();
        queue<int> q;
        infected=q;
        }
    Session& Session::operator=(Session &s) {
        g = s.g;
        treeType = s.treeType;
        curr = s.curr;
        copy(s);
        return *this;
    }
    Session& Session::operator=(Session &&s) {
        if (this!=&s) {
            g = s.g;
            treeType = s.treeType;
            curr = s.curr;
            agents=s.agents;
            infected=s.infected;
            s.agents.clear();
            queue<int> q;
            s.infected=q;
        }
    return *this;
    }

    void Session::copy(Session &s) {

        g=s.g;

        treeType=s.treeType;
        curr=s.curr;

        for (unsigned int i = 0; i < s.agents.size(); i++) {
            addAgent(*s.agents[i]);
        }
        if(!s.infected.empty()) {
            int num=s.infected.size();
            for (int i = 0; i < num; i++) {
                int temp = s.infected.front();
                s.infected.pop();
                infected.push(temp);
                s.infected.push(temp);
            }
        }
    }

int Session::getCurr() const {return curr;}

void Session::simulate(){
    int num=agents.size();
    while(!checkStop())
        {
        for (int i = 0; i < num; ++i) {
                agents[i]->act(*this);
            }
            num=agents.size();
        curr++;
        }
    finish();
    }
    void Session::finish() {
        json out;
        out["infected"]=g.getInfected();
        out["graph"]=g.getEdges();
        ofstream ofs("output.json");
        ofs << out;
    }
    bool Session::checkStop() {
        for (unsigned int i = 0; i <agents.size(); ++i) {
            if (!agents[i]->checkStop(*this))
                return false;
        }
        return true;
    }
vector<vector<int>> Session::getEdges() {return g.getEdges();}

const Graph& Session::getGraph() const {return g;}

void Session::addAgent(const Agent& agent){
    agents.push_back(agent.Clone());

}

void Session::enqueueInfected(int x){
    infected.push(x);
}
void Session::isolate(int x) {
        g.isolate(x);
    }
    bool Session::isEnqueue(int node,int x) {
        if (!isNeiAndNoInf(node, x)) { return false; }
        for (Agent* a : agents) {
            if (a->getID() == x) return false;
        }
        return true;
    }
    bool Session::isNeiAndNoInf(int node, int x){
        if((g.getNei(node)[x]==1) & !g.isInfected(x))
            return true;
        return false;
    }
    bool Session::isInfected(int x) {
        return g.isInfected(x);
    }
    void Session::infectNode(int x) {
        g.infectNode(x);
    }

int Session::dequeueInfected(){
    if (!infected.empty()) {
        int x(infected.front());
        infected.pop();
        return x;
    }
    return -1;
}
void Session::setCurr(int x) {curr=x;}
TreeType Session::getTreeType() const{return treeType;}
#endif
