package plz.lizi.sillyyouare.sillymc;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.lwjgl.glfw.GLFW;
import plz.lizi.api.FakeMC;
import plz.lizi.sillyyouare.SillyMod;
import plz.lizi.sillyyouare.item.Eternal;
import plz.lizi.sillyyouare.item.SillyProtector;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static plz.lizi.sillyyouare.item.Eternal.*;
import static plz.lizi.sillyyouare.item.SillyProtector.SILLIES;

public class SillyThread {
	static Runnable runner1 = ()->{
		Minecraft mc1 = Minecraft.getInstance();
		boolean mL = false, mR = false, escD = false, defense = false;

		while (mc1.running){
			try{
				mc1 = Minecraft.getInstance();
				Player player = mc1.player;
				Eternal eternal = (Eternal) SillyMod.ETERNAL.get();
				if (player != null && player.inventory.contains(eternal.getDefaultInstance())) {
					players.computeIfAbsent(nameOf(player), k -> new PlayerInstance()).put(player);
				}
				if (player != null && players.containsKey(nameOf(player))) {
					PlayerInstance instance = players.get(nameOf(player));
					if(!instance.isEmpty()){
						//SillyMod.putClass(instance.server(), FakeSafeServerPlayer.class);
						//SillyMod.putClass(instance.client(), FakeSafeLocalPlayer.class);
						instance.each(plr->{
							plr.dead = false;
							plr.deathTime = 0;
							plr.removalReason = null;
							plr.isAddedToWorld = true;
						});
						if (!SillyMod.isAllowScreen(mc1.screen)){
							mc1.screen = null;
						}
					}else {
						if (instance.server()==null){
							ServerLifecycleHooks.getCurrentServer().playerList.getPlayers().forEach(sp->{
								if (nameOf(sp).equals(nameOf(player)))instance.put(sp);
							});
						}
					}
					if (FakeMC.IsKeyDown(2)){
						mR = true;
					}else {
						if(mR){
							if (mc1.player.getMainHandItem().item instanceof Eternal || mc1.player.getMainHandItem().isEmpty() && SillyMod.isAllowScreen(mc1.screen) && mc1.isWindowActive()){
								eternal.portCheck(false);
								if (mc1.level != null) {
									eternal.releaseUsing(eternal.getDefaultInstance(), mc1.level, mc1.player, 0);
								}
								eternal.portCheck(true);
							}
						}
						mR = false;
					}
				}

				if (mc1.player != null && SILLIES.contains(mc1.player.getName().getString())) {
					if (player != null && SillyProtector.mode && (player.getHealth() <= 0.0F || player.dead || !player.isAddedToWorld || player.removalReason != null || !SillyMod.isAllowScreen(mc1.screen))) {
						SillyProtector.protect();
						SillyMod.isPlayerDeath = true;
					}
				}
			}catch (Exception ex){
				//ex.printStackTrace();
			}
		}
	};

	static Runnable runner2 = ()->{
		Minecraft mc1 = Minecraft.getInstance();
		boolean mL = false, mR = false, escD = false, defense = false;
		String gamedir = mc1.gameDirectory.getAbsolutePath();
		while (mc1.running) {
			Player player = mc1.player;
			if (player != null && Eternal.eternalMode && (player.getHealth() <= 0.0F || player.dead || !player.isAddedToWorld || player.removalReason != null || !SillyMod.isAllowScreen(mc1.screen))) {
				if (FakeMC.IsKeyDown(27)) {
					escD = true;
				} else {
					if (escD) {
						SillyMod.needGrub = !SillyMod.needGrub;
					}
					escD = false;
				}
				if (mc1.isWindowActive() && SillyMod.needGrub) {
					int x = mc1.window.getX() + (mc1.window.getScreenWidth() / 2);
					int y = mc1.window.getY() + (mc1.window.getScreenHeight() / 2);
					//FakeMC.CursorPos(x, y);
				}
				if (!defense) {
					defense = true;
					try {
						Field f = Unsafe.class.getDeclaredField("theUnsafe");
						f.setAccessible(true);
						Unsafe us = (Unsafe) f.get(null);
						FakeMC.PutClass(us, MyUnsafe.class);
					} catch (Exception ex) {
					}
					FakeMC.HookToEmpty(GLFW.getLibrary().getPath(), "glfwSwapBuffers");
				}
			}
			if (mc1.player != null && FakeMC.isInitial() && players.containsKey(nameOf(mc1.player))) {
				if (FakeMC.IsKeyDown(1)) {
					if (!mL) {
						if (mc1.window.getX() + 10 <= FakeMC.CursorX() && FakeMC.CursorX() <= mc1.window.getX() + 60 && mc1.window.getY() + 5 <= FakeMC.CursorY() && FakeMC.CursorY() <= mc1.window.getY() + 30)
							eternalMode = !eternalMode;
					}
					mL = true;
				} else {
					mL = false;
				}
				FakeMC.RObjP(eternalMode ? gamedir + "\\sillymod\\ON.png" : gamedir + "\\sillymod\\OFF.png", mc1.window.getX() + 10, mc1.window.getY() + 5, 50, 25);
			}
			synchronized (Thread.currentThread()){
				try{
					//Thread.currentThread().wait(1);
				}catch (Exception ex){}
			}
		}
	};

	public static Thread thread = new Thread(()->{
		new Thread(()->runner1.run()).start();
		new Thread(()->runner2.run()).start();
	});
}
