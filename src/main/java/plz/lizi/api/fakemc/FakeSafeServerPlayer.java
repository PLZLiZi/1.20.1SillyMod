package plz.lizi.api.fakemc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeSafeServerPlayer extends ServerPlayer {
	public FakeSafeServerPlayer(MinecraftServer p_254143_, ServerLevel p_254435_, GameProfile p_253651_) {
		super(p_254143_, p_254435_, p_253651_);
	}

	@Override
	public void setHealth(float p_21154_) {
		this.isAddedToWorld = true;
	}

	@Override
	public void remove(@NotNull RemovalReason p_150097_) {
		this.isAddedToWorld = true;
	}

	@Override
	public float getHealth() {
		this.deathTime = 0;
		this.isAddedToWorld = true;
		return 20.0F;
	}

	@Override
	public boolean isAlive() {
		this.isAddedToWorld = true;
		return true;
	}

	@Nullable
	@Override
	public RemovalReason getRemovalReason() {
		this.isAddedToWorld = true;
		return null;
	}

	@Override
	public boolean isAlwaysTicking() {
		this.isAddedToWorld = true;
		return true;
	}
}
