#ifndef AGENT_H_
#define AGENT_H_
#include "Session.h"
#include <vector>

class Agent{
public:
    Agent();


    virtual void act(Session& session)=0;
    virtual Agent* Clone()const=0;
    virtual ~Agent();
    virtual bool checkStop(Session& session)=0;
    virtual int getID() = 0;

};

class ContactTracer: public Agent{
public:
    ContactTracer();
    virtual ~ContactTracer();
    virtual int getID();


    virtual void act(Session& session) ;
    virtual  Agent* Clone() const;
    virtual bool checkStop(Session& session);

};


class Virus: public Agent{
public:
    Virus(int nodeInd);
    virtual ~Virus();
    Virus(Virus& v);
    Virus(Virus&& v);
    //Virus is immutable so oparator= is not possible.
    virtual bool checkStop(Session& session);
    virtual int getID();



    virtual void act(Session& session) ;
    virtual  Agent* Clone() const;

private:
    const int nodeInd;

};

#endif
