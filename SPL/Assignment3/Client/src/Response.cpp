//
// Created by spl211 on 08/01/2021.
//

#include "../Include/Response.h"

Response::Response(ConnectionHandler &c):ch(c) {}
void Response::run() {
    while (1) {
        std::string s;
        ch.getLine(s);
        std::cout << s << std::endl;
        if (s == "ACK 4") {
            ch.flag=true;
            break;
        }
    }
}