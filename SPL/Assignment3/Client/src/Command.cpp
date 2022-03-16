//
// Created by spl211 on 08/01/2021.
//
#ifndef ASD
#define ASD
#include "../Include/Command.h"
#include <unistd.h>


Command::Command(ConnectionHandler& c):ch(c){
}
void Command::run() {
    try {
        while (!ch.flag) {
            char c[1024];
            std::cin.getline(c, 1024);
            std::string s(c);
            ch.sendLine(s);
        if((s=="LOGOUT"))
        {
            usleep(1000);
     //       break;
        }
        }
    }
    catch (std::exception& e){}
}
#endif