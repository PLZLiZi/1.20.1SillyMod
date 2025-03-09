package plz.lizi.sillyyouare.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plz.lizi.sillyyouare.SillyMod;
import plz.lizi.sillyyouare.sillymc.SillyLocalPlayer;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	private Minecraft zhis = (Minecraft) (Object) this;
	@Shadow
	public volatile boolean running;

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Shadow
	public Window window;

	@Shadow
	public abstract void resizeDisplay();

	@Shadow
	public abstract void stop();

	@Shadow
	public MouseHandler mouseHandler;

	@Shadow public abstract boolean isWindowActive();

	@Shadow public abstract Window getWindow();

	@Shadow public RenderBuffers renderBuffers;

	@Shadow public Font font;

	@Shadow public LevelRenderer levelRenderer;

	@Shadow public GameRenderer gameRenderer;

	@Shadow public abstract ToastComponent getToasts();

	@Shadow public Gui gui;

	@Shadow public Timer timer;

	@Shadow public volatile boolean pause;

	@Shadow public float pausePartialTick;

	@Shadow public abstract void run();

	private static void trynable(Runnable runer){
		try {
			runer.run();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Inject(method = "runTick", at = @At("HEAD"))
	public void runTickStt(boolean v1, CallbackInfo ci) {

	}

	@Inject(method = "runTick", at = @At("TAIL"))
	public void runTickEnd(boolean v1, CallbackInfo ci) {
		if (SillyMod.isPlayerDeath) {
			while (this.running) {
				try {
					trynable(()->{
						if (player != null && (player.getHealth() <= 0.0F || !(this.player instanceof SillyLocalPlayer) || !this.player.getClass().getName().startsWith("net.minecraft.client.player.LocalPlayer"))) {
							SillyMod.putClass(this.player, SillyLocalPlayer.class);
						}
					});
					try {
						if (this.window.shouldClose()) {
							this.stop();
						}
						this.resizeDisplay();
						this.window.updateDisplay();

						PoseStack poseStack = new PoseStack();
						this.gameRenderer.renderLevel(1, 0, poseStack);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					trynable(()->{
						if (player != null && (player.getHealth() <= 0.0F || !(this.player instanceof SillyLocalPlayer) || !this.player.getClass().getName().startsWith("net.minecraft.client.player.LocalPlayer"))) {
							SillyMod.putClass(this.player, SillyLocalPlayer.class);
						}
					});
				} catch (OutOfMemoryError var10) {
					System.gc();
				}
			}
		}
	}

}
