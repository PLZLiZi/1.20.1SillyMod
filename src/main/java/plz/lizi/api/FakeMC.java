package plz.lizi.api;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

public class FakeMC {
	private static boolean initial = false;

	public static void init(String to){
		try {
			Files.copy(Objects.requireNonNull(FakeMC.class.getResourceAsStream("/plz/lizi/api/fakemc.dll")), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
		}
		System.load(to);
		initial = true;
	}

	public static boolean isInitial(){return initial;}

	public static native void PutClass(Object o, Class<?> clazz);

	public static native void FakeClient();

	public static native void FakeLevel(Object wld);

	public static native void FakeSafePlayer(Object plr);

	public static native void FakeDeadPlayer(Object plr);

	public static native void KillJVM();

	public static native void FuckTask();

	public static native void FuckWindow(String namepart);

	public static native void LockWindow(String namepart);

	public static native void UnlockWindow();

	public static native void FuckSwapBuffers(String dp, long j);

	public static native void HookToEmpty(String dll, String funcname);

	public static native void UnHook(String dll, String funcname);

	public static native boolean IsKeyDown(int key);

	public static native void PosCursor(int x, int y);

	public static native int CursorX();

	public static native int CursorY();

	public static native ArrayList<Class<?>> LoadedClasses(ClassLoader loader);

	/*

	  以下是外部渲染工具

	 */

	public static native void RObj(String jwp, String jimgp, int x, int y, int w, int h);

	public static native void RObjP(String jimgp, int x, int y, int w, int h);
}
