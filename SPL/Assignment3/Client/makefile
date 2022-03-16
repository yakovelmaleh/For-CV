CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: Client
	g++ -o bin/BGRSClient bin/connectionHandler.o bin/Command.o bin/Response.o bin/main.o $(LDFLAGS)

Client: bin/connectionHandler.o bin/Command.o bin/Response.o bin/main.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/Command.o: src/Command.cpp
	g++ $(CFLAGS) -o bin/Command.o src/Command.cpp

bin/Response.o: src/Response.cpp
	g++ $(CFLAGS) -o bin/Response.o src/Response.cpp

bin/main.o: src/main.cpp
	g++ $(CFLAGS) -o bin/main.o src/main.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
