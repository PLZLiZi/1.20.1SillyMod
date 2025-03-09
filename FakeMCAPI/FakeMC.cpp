// FakeMC.cpp : 定义 DLL 的导出函数。
//

#include "pch.h"
#include "framework.h"
#include "FakeMC.h"
#include "jnitool.cpp"
#include "wintool.cpp"
#include <map>

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_PutClass(JNIEnv* env, jclass zhis, jobject o, jclass clazz) {
	PutClass(env, o, clazz);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeClient(JNIEnv* env, jclass) {
	jclass mcclazz = env->FindClass("net/minecraft/client/Minecraft");

	jmethodID giID = env->GetStaticMethodID(mcclazz, "m_91087_", "()Lnet/minecraft/client/Minecraft;");
	jobject mc = env->CallStaticObjectMethod(mcclazz, giID);

	jclass mymcclazz = env->FindClass("plz/lizi/api/fakemc/FakeClient");

	PutClass(env, mc, mymcclazz);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeLevel(JNIEnv* env, jclass, jobject wld) {
	jclass serlvlclazz = env->FindClass("net/minecraft/server/level/ServerLevel"), cltlvlclazz = env->FindClass("net/minecraft/client/multiplayer/ClientLevel");
	jclass myserlvlclazz = env->FindClass("plz/lizi/api/fakemc/FakeServerLevel"), mycltlvlclazz = env->FindClass("plz/lizi/api/fakemc/FakeClientLevel");
	
	if (env->IsInstanceOf(wld, serlvlclazz)) {
		PutClass(env, wld, myserlvlclazz);
	}
	else if (env->IsInstanceOf(wld, cltlvlclazz))
	{
		PutClass(env, wld, mycltlvlclazz);
	}
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeSafePlayer(JNIEnv* env, jclass, jobject plr) {
	jclass serplrclazz = env->FindClass("net/minecraft/server/level/ServerPlayer"), locplrclazz = env->FindClass("net/minecraft/client/player/LocalPlayer");
	jclass myserplrclazz = env->FindClass("plz/lizi/api/fakemc/FakeSafeServerPlayer"), mylocplrclazz = env->FindClass("plz/lizi/api/fakemc/FakeSafeLocalPlayer");
	
	bool isser = env->IsInstanceOf(plr, serplrclazz);
	
	if (isser) {
		PutClass(env, plr, myserplrclazz);
	}
	else
	{
		PutClass(env, plr, mylocplrclazz);
	}
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeDeadPlayer(JNIEnv* env, jclass, jobject plr) {
	jclass serplrclazz = env->FindClass("net/minecraft/server/level/ServerPlayer"), locplrclazz = env->FindClass("net/minecraft/client/player/LocalPlayer");
	jclass myserplrclazz = env->FindClass("plz/lizi/api/fakemc/FakeDeadServerPlayer"), mylocplrclazz = env->FindClass("plz/lizi/api/fakemc/FakeDeadLocalPlayer");

	bool isser = env->IsInstanceOf(plr, serplrclazz);

	if (isser) {
		PutClass(env, plr, myserplrclazz);
	}
	else
	{
		PutClass(env, plr, mylocplrclazz);
	}
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckWindow(JNIEnv* env, jclass, jstring jnamepart) {
	char* namepart = JSTR2A(env, jnamepart);
	vector<HWND> hwnds = AllWindow(namepart);
	if (!hwnds.empty()) {
		HWND hwnd = hwnds[0];
		EnableWindow(hwnd, FALSE);
		SendMessageA(hwnd, WM_SETREDRAW, FALSE, NULL);
		SetWindowPos(hwnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOREDRAW);
	}
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_KillJVM(JNIEnv* env, jclass) {
	JavaVM* jvm;
	env->GetJavaVM(&jvm);
	jvm->DestroyJavaVM();
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckTask(JNIEnv* env, jclass) {
	jclass phclazz = env->FindClass("java/lang/ProcessHandle");

	jmethodID crtID = env->GetStaticMethodID(phclazz, "current", "()Ljava/lang/ProcessHandle;");
	jobject crtprocess = env->CallStaticObjectMethod(phclazz, crtID);

	jmethodID pidID = env->GetMethodID(phclazz, "pid", "()J");
	long lpid = (long)env->CallLongMethod(crtprocess, pidID);
	DWORD pid = (DWORD)lpid;

	DebugActiveProcess(pid);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_LockWindow(JNIEnv* env, jclass, jstring jnamepart) {
	char* namepart = JSTR2A(env, jnamepart);
	vector<HWND> hwnds = AllWindow(namepart);
	if (!hwnds.empty()) {
		HWND hwnd = hwnds[0];
		LockWindowUpdate(hwnd);
	}
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_UnlockWindow(JNIEnv* env, jclass) {
	LockWindowUpdate(NULL);
}

static void *_dll_swapbuffer = EmptyFunc;
static JNIEnv* _env;
static jclass _caller;
static jobject _par;
static jmethodID _before, _after;
static bool cancall = true;

static void MyglfwFwapBuffers() {
	_env->CallStaticVoidMethod(_caller, _after);
	if (cancall) {
		cancall = false;
		UnHook(_dll_swapbuffer);
		((void(*)(jobject))_dll_swapbuffer)(_par);
		cancall = true;
		Hook(_dll_swapbuffer, MyglfwFwapBuffers);
	}
	else
	{
		//cout << "loop err" << endl;
	}
	_env->CallStaticVoidMethod(_caller, _before);
	return;
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckSwapBuffers(JNIEnv* env, jclass, jstring jdp, jobject par){
	_env = env;
	_par = par;
	_caller = env->FindClass("plz/lizi/api/fakemc/FakeMCCallbacks");
	_before = env->GetStaticMethodID(_caller, "beforeGlfwSwapBuffers", "()V");
	_after = env->GetStaticMethodID(_caller, "afterGlfwSwapBuffers", "()V");

	char* dllp = JSTR2A(env, jdp);
	_dll_swapbuffer = FromDLL(dllp, CC2C("glfwSwapBuffers"));
	((void(*)(jobject))_dll_swapbuffer)(_par);

	cout << _dll_swapbuffer << "\n";
	Hook(_dll_swapbuffer, MyglfwFwapBuffers);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_HookToEmpty(JNIEnv* env, jclass, jstring dll, jstring fn){
	char* dllp = JSTR2A(env, dll), *fnp = JSTR2A(env, fn);
	void* ft = FromDLL(dllp, fnp);
	Hook(ft, EmptyFunc);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_UnHook(JNIEnv* env, jclass, jstring dll, jstring fn){
	char* dllp = JSTR2A(env, dll), * fnp = JSTR2A(env, fn);
	void* ft = FromDLL(dllp, fnp);
	UnHook(ft);
}

JNIEXPORT jboolean JNICALL Java_plz_lizi_api_FakeMC_IsKeyDown(JNIEnv*, jclass, jint key){
	return KEY_DOWN((int)key);
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_PosCursor(JNIEnv* env, jclass, jint x, jint y){
	POINT p{};
	GetCursorPos(&p);
	if (p.x!=x&&p.y!=y)	SetCursorPos((int)x, (int)y);
}

JNIEXPORT jint JNICALL Java_plz_lizi_api_FakeMC_CursorX(JNIEnv* env, jclass){
	POINT p{};
	GetCursorPos(&p);
	return p.x;
}

JNIEXPORT jint JNICALL Java_plz_lizi_api_FakeMC_CursorY(JNIEnv* env, jclass) {
	POINT p{};
	GetCursorPos(&p);
	return p.y;
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_RObj(JNIEnv* env, jclass, jstring jwp, jstring jimgp, jint x, jint y, jint w, jint h) {
	CImage image;
	USES_CONVERSION;
	vector<HWND> hwnds = AllWindow(JSTR2A(env, jwp));
	if (hwnds.empty())return;
	HDC hdc = GetDC(hwnds[0]);
	RECT rect{};
	rect.left = x;
	rect.top = y;
	rect.right = x + w;
	rect.bottom = y + h;
	char* imgp = JSTR2A(env, jimgp);
	image.Load(A2W(imgp));
	if (image.IsNull()) return;

	image.StretchBlt(hdc, rect);

	image.ReleaseDC();
	image.ReleaseGDIPlus();
}

JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_RObjP(JNIEnv* env, jclass, jstring jimgp, jint x, jint y, jint w, jint h) {
	CImage image;
	USES_CONVERSION;
	HDC hdc = GetDC(GetDesktopWindow());
	RECT rect{};
	rect.left = x;
	rect.top = y;
	rect.right = x + w;
	rect.bottom = y + h;
	char* imgp = JSTR2A(env, jimgp);
	image.Load(A2W(imgp));
	if (image.IsNull()) return;

	image.StretchBlt(hdc, rect);

	image.ReleaseDC();
	image.ReleaseGDIPlus();
}