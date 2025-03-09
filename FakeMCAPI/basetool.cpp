#include "pch.h"

#include <random>
#include <string>
#include <fstream>

using namespace std;

static int GetRand(int min, int max) {
	srand(time(0));
	return (rand() % (max - min + 1)) + min;
}

static bool StrIn(const wchar_t* str, const wchar_t* sub){
	const wchar_t* ptr = std::wcsstr(str, sub);
	return ptr != nullptr;
}

static bool StrIn(const char* str, const char* sub) {
	const char* ptr = std::strstr(str, sub);
	return ptr != nullptr;
}

static char* CC2C(const char* cc) {
    return const_cast<char*>(cc);
}

static char* B2C(bool b) {
    const char* bostr = b ? "true" : "false";
    return CC2C(bostr);
}

static string FileR(string path) {
    ifstream fr(path);
    if (fr.is_open()) {
        string out;
        string line;
        while (getline(fr, line)) {
            out += line + "\n";
        }
        if (out.length() >= 2) {
            out.erase(out.length() - 1);
        }
        fr.close();
        return out;
     }else {
        return "";
    }
}

static bool FileW(string path, string in) {
    ofstream fw(path);
    if (fw.is_open()) {
        fw << in;
        fw.close();
    } else {
        return false;
    }
    return true;
}