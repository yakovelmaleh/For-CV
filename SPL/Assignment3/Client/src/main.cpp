
#ifndef BBB
#define BBB
#include <stdlib.h>
#include "../Include/connectionHandler.h"
#include "../Include/Command.h"
#include "../Include/Response.h"
#include <thread>
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc,char **argv) {
    ConnectionHandler ch(argv[1], std::stoi(argv[2]));
    if (!ch.connect()) {
        std::cerr << "Cannot connect to" << std::endl;
    }


    Command c(ch);
    Response r(ch);
    std::thread t1(&Command::run,&c);
    std::thread t2(&Response::run,&r);
    t2.join();
    t1.join();

//    while (1) {
//        try {
//            char c[1024];
//            std::cout << "write action" << std::endl;
//            std::cin.getline(c, 1024);
//            std::string s(c);
//            std::cout << "accept action:" << s << std::endl;
//            bool check = true;
//
//            check = ch.sendLine(s);
//
//            if (check) {
//                std::string s2;
//                ch.getLine(s2);
//                std::cout << s2 << std::endl;
//                if (s2 == "ACK 4") {
//                    break;
//                }
//            }
//        }
//        catch(const std::exception& e) {
//            std::cout<<"illigal command "<<std::endl;
//
//        }
//    }

    return 0;
}
#endif
