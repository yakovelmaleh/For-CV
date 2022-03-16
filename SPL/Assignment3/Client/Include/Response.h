//
// Created by spl211 on 08/01/2021.
//

#ifndef BGRSCLIENT_RESPONSE_H
#define BGRSCLIENT_RESPONSE_H
#include "connectionHandler.h"

class Response {
public:
    ConnectionHandler& ch;
    Response(ConnectionHandler& c);
    void run();
};


#endif //BGRSCLIENT_RESPONSE_H
