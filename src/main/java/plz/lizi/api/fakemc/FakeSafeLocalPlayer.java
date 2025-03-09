package plz.lizi.api.fakemc;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeSafeLocalPlayer extends LocalPlayer {
	public FakeSafeLocalPlayer(Minecraft p_108621_, ClientLevel p_108622_, ClientPacketListener p_108623_, StatsCounter p_108624_, ClientRecipeBook p_108625_, boolean p_108626_, boolean p_108627_) {
		super(p_108621_, p_108622_, p_108623_, p_108624_, p_108625_, p_108626_, p_108627_);
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
