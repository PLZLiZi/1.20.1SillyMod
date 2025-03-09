package plz.lizi.api.fakemc;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import org.jetbrains.annotations.Nullable;

public class FakeDeadLocalPlayer extends LocalPlayer {
	public FakeDeadLocalPlayer(Minecraft p_108621_, ClientLevel p_108622_, ClientPacketListener p_108623_, StatsCounter p_108624_, ClientRecipeBook p_108625_, boolean p_108626_, boolean p_108627_) {
		super(p_108621_, p_108622_, p_108623_, p_108624_, p_108625_, p_108626_, p_108627_);
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
