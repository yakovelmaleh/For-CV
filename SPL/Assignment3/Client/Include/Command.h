//
// Created by spl211 on 08/01/2021.
//

#ifndef BGRSCLIENT_COMMAND_H
#define BGRSCLIENT_COMMAND_H
#include "connectionHandler.h"

class Command {
public:
    ConnectionHandler& ch;
//    Command();
    Command(ConnectionHandler& c);
    void run();
};


#endif //BGRSCLIENT_COMMAND_H
