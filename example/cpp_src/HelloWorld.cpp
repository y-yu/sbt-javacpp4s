#include <iostream>
#include <string>
#include "HelloWorld.hpp"

using namespace std;

int HelloWorld::printN(int n) {
    for (int i = 0; i < n; i++) {
        cout << "Hello World!\n";
    }

    return n;
}
