package plz.lizi.api.fakemc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class FakeDeadServerPlayer extends ServerPlayer {
	public FakeDeadServerPlayer(MinecraftServer p_254143_, ServerLevel p_254435_, GameProfile p_253651_) {
		super(p_254143_, p_254435_, p_253651_);
	}

	@Override
	public float getHealth() {
		return 0.0F;
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Nullable
	@Override
	public RemovalReason getRemovalReason() {
		return RemovalReason.KILLED;
	}

	@Override
	public boolean isAlwaysTicking() {
		return false;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}
}
