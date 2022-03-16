//
// Created by spl211 on 09/11/2020.
//
#ifndef GRAPH
#define GRAPH
#include "Graph.h"
#include <iostream>
#include <vector>


using namespace std;
    Graph::Graph(vector<vector<int>> matrix): edges(matrix), nodes(){
        for (unsigned int i = 0; i < matrix.size(); ++i) {
            nodes.push_back(false);
        }
    }
    Graph::Graph():edges(), nodes(){}
    Graph::~Graph()= default;
    Graph::Graph(const Graph& g):edges(), nodes() {
        copy(g);}
    Graph::Graph(Graph&& g):edges(g.edges), nodes(g.nodes) {
        g.edges.clear();
        g.nodes.clear();}
    Graph& Graph::operator=(const Graph& g){
        copy(g);
        return *this;
    }
    Graph& Graph::operator=(Graph&& g){
        if (this!=&g){
            nodes=g.nodes;
            edges=g.edges;
            g.edges.clear();
            g.nodes.clear();
        }
        return *this;
    }
    void Graph::copy(const Graph &g) {
        for (unsigned int i = 0; i < g.nodes.size(); ++i) {
            nodes.push_back(g.nodes[i]);
        }
        for (unsigned int i = 0; i < g.edges.size(); ++i) {
            vector<int> temp;
            for (unsigned int j = 0; j < g.edges[i].size(); ++j) {
                temp.push_back(g.edges[i][j]);
            }
            edges.push_back(temp);
        }
    }
    void Graph::isolate(int x) {
        for (unsigned int i = 0; i < edges.size(); ++i) {
            edges[x][i]=0;
            edges[i][x]=0;
        }

    }
    void Graph::setEdges(vector<vector<int>> v){
        edges.clear();
        edges=v;
        for (unsigned int i = 0; i < v.size(); ++i) {
            nodes.push_back(false);
        }
    }
std::vector<int> Graph::getInfected(){
        vector<int> v;
    for (unsigned int i = 0; i < nodes.size(); ++i) {
        if (nodes[i])
            v.push_back(i);
    }
    return v;
    }


    std::vector<int> Graph::getNei(int x)  {
        return edges[x];
    }
    void Graph::infectNode(int nodeInd) {
        nodes[nodeInd] = true;
    }
    vector<vector<int>> Graph::getEdges() {return edges;}
    int Graph::getSize() const {return nodes.size();}

    bool Graph::isInfected(int nodeInd){
        return nodes[nodeInd];}
    void Graph::print() const{
        cout<<"the edges is: "<<endl;
        for (unsigned int i = 0; i < edges.size(); ++i) {
            for (unsigned int j = i; j < edges[i].size(); ++j) {
                if (edges[i][j])
                    cout<<"("<<i<<","<<j<<")";
            }
        }
        cout<<endl;

        cout<<"the infected node is"<<endl;
        for (unsigned int j = 0; j < nodes.size(); ++j) {
            if (nodes[j])
                cout<<j<< ",";
        }
        cout<<endl;

    }

#endif