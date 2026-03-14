#include "../src/main/cpp/tfl.h"

void testCommand(vector<string> args) {
    sendMessage("Hello from C++!");
    sendMessage("Your name: " + getName());

    if (args.size() > 0) {
        sendMessage("Args: " + args[0]);
    }
}

REGISTER_COMMAND(test, testCommand);
