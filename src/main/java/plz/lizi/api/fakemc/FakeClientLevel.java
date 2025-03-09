package plz.lizi.api.fakemc;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import plz.lizi.api.FakeMCCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FakeClientLevel extends ClientLevel {
	public FakeClientLevel(ClientPacketListener p_205505_, ClientLevelData p_205506_, ResourceKey<Level> p_205507_, Holder<DimensionType> p_205508_, int p_205509_, int p_205510_, Supplier<ProfilerFiller> p_205511_, LevelRenderer p_205512_, boolean p_205513_, long p_205514_) {
		super(p_205505_, p_205506_, p_205507_, p_205508_, p_205509_, p_205510_, p_205511_, p_205512_, p_205513_, p_205514_);
	}

	@Override
	public @NotNull Iterable<Entity> entitiesForRendering() {
		List<Entity> call = new ArrayList<>();
		for (Entity entity : super.entitiesForRendering()){
			FakeMCCallbacks.FakeClientLevel.entitiesForRendering(this, call, entity);
		}
		return call;
	}


}
