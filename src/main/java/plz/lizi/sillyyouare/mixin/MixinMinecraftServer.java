package plz.lizi.sillyyouare.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer extends ReentrantBlockableEventLoop<TickTask> {
	public MinecraftServer zhis = (MinecraftServer) (Object) this;

	public MixinMinecraftServer(String p_18765_) {
		super(p_18765_);
	}

	@Shadow public abstract boolean pollTask();

	@Shadow protected abstract boolean haveTime();

	@Shadow public abstract Iterable<ServerLevel> getAllLevels();

	/**
	 * @author ...
	 * @reason ...
	 */
	//@Overwrite
	private boolean pollTaskInternal() {
		if (super.pollTask()) {
			return true;
		} else {
			if (this.haveTime()) {
				Iterator var1 = this.getAllLevels().iterator();
				while(var1.hasNext()) {
					ServerLevel serverlevel = (ServerLevel)var1.next();
					try {
						if (serverlevel!=null && serverlevel.getChunkSource().pollTask()) {
							return true;
						}
					}catch (Exception ignored){}
				}
			}

			return false;
		}
	}
}
