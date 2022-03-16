#ifndef AAA
#define AAA

#include "../Include/connectionHandler.h"

#include <boost/algorithm/string.hpp>
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_),flag(false){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
short ConnectionHandler::byteToShort(char* byteArr)
{
    short result=(short)((byteArr[0]& 0xff)<<8);
    result+=(short)(byteArr[1]&0xff);
    return result;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    try {
         char c[2];
        getBytes(c, 2);
        short s = byteToShort(c);
        if (s == 12) {
            getBytes(c, 2);
            short s2 = byteToShort(c);
            line="ACK ";
            std::stringstream ss;
            ss<<s2;
            line.append(ss.str());
            if((s2==11) | ((s2>5) & (s2<10))){
                line.append("\n");
            }
            getFrameAscii(line, '\0');
        }else//13
        {
            getBytes(c, 2);
            short s2 = byteToShort(c);
            line = "ERROR ";
            std::stringstream ss;
            ss<<s2;
            line.append(ss.str());

        }
        return true;
    }
    catch (std::exception& e) {
        return false;
    }

}
void  ConnectionHandler::shortToBytes(short num,char* bytesArr){
    bytesArr[0]=((num>>8)& 0xff);
    bytesArr[1]=(num & 0xff);
}

bool ConnectionHandler::sendLine(std::string& line) {
   // boost::char_separator<char> sep(" ","",boost::keep_empty_tokens);
   // boost::tokenizer<boost::char_separator<char>> tok(line,sep);
    std::vector<string> vec;
    boost::split(vec,line,boost::is_any_of(" "));
   // string s=*tok.begin();
    string s=vec[0];
    short opcode;
    if((vec.size()==3) && ((s=="ADMINREG") | (s=="STUDENTREG") | (s=="LOGIN")))
    {
        if(s=="ADMINREG") opcode=1;
        else if(s=="STUDENTREG") opcode=2;
        else opcode=3;
//        auto i=tok.begin();
//        i++;
//        string user=*i;
//        i++;
//        string pass=*i;
        string user(vec[1]);
        string pass(vec[2]);
        char c[2];
        shortToBytes(opcode,c);
        sendBytes(c,2);
        sendFrameAscii(user, '\0');
        sendFrameAscii(pass, '\0');
    }
    else if((vec.size()==1) && ((s=="LOGOUT") | (s=="MYCOURSES")))
    {
        if(s=="LOGOUT") opcode=4;
        else opcode=11;
        char c[2];
        shortToBytes(opcode,c);
        sendBytes(c,2);
    }
    else if((vec.size()==2)&&((s=="KDAMCHECK") | (s=="COURSEREG") | (s=="COURSESTAT") | (s=="ISREGISTERED") | (s=="UNREGISTER")))
    {
        if(s=="COURSEREG") opcode=5;
        else if(s=="KDAMCHECK") opcode=6;
        else if(s=="COURSESTAT") opcode=7;
        else if(s=="ISREGISTERED") opcode=9;
        else opcode=10;
        char c[2];
        shortToBytes(opcode,c);
        sendBytes(c,2);
//        auto i=tok.begin();
//        i++;
//        short o=(short)std::stoi(*i);
        short o=(short)std::stoi(vec[1]);
        shortToBytes(o,c);
        sendBytes(c,2);
    }
    else if((vec.size()==2)&& (s=="STUDENTSTAT"))
    {
        opcode=8;
        char c[2];
        shortToBytes(opcode,c);
        sendBytes(c,2);

//        auto i=tok.begin();
//        i++;
//        string user=*i;
        string user(vec[1]);
        sendFrameAscii(user, '\0');
    }
    else
    {
        std::cout<<"invalid command"<<std::endl;
        return false;
    }
    return true;
 //   else if()


   // return sendFrameAscii(line, '\n');
}
 

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
	do{
		if(!getBytes(&ch, 1))
		{
			return false;
		}
		if(ch!='\0')  
			frame.append(1, ch);
	}while (delimiter != ch);
    } catch (std::exception& e) {
	std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
	return false;
    }
    return true;
}
 
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}
#endif
