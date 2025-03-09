package plz.lizi.api.fakemc;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plz.lizi.api.FakeMCCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class FakeServerLevel extends ServerLevel {
	public FakeServerLevel(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey<Level> p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List<CustomSpawner> p_215008_, boolean p_215009_, @Nullable RandomSequences p_288977_) {
		super(p_214999_, p_215000_, p_215001_, p_215002_, p_215003_, p_215004_, p_215005_, p_215006_, p_215007_, p_215008_, p_215009_, p_288977_);
	}

	@Override
	public @NotNull Iterable<Entity> getAllEntities() {
		List<Entity> call = new ArrayList<>();
		for (Entity entity : super.getAllEntities()){
			FakeMCCallbacks.FakeServerLevel.getAllEntities(this, call, entity);
		}
		return call;
	}

	@Override
	public <T extends Entity> @NotNull List<? extends T> getEntities(@NotNull EntityTypeTest<Entity, T> p_143281_, @NotNull Predicate<? super T> p_143282_) {
		List<T> list = Lists.newArrayList();
		this.getEntities(p_143281_, p_143282_, list);
		return new ArrayList<>(list);
	}

	@Override
	public <T extends Entity> void getEntities(@NotNull EntityTypeTest<Entity, T> p_262152_, @NotNull Predicate<? super T> p_261808_, @NotNull List<? super T> p_261583_) {
		this.getEntities(p_262152_, p_261808_, p_261583_, Integer.MAX_VALUE);
	}

	@Override
	public <T extends Entity> void getEntities(@NotNull EntityTypeTest<Entity, T> p_261842_, @NotNull Predicate<? super T> p_262091_, @NotNull List<? super T> p_261703_, int p_261907_) {
		this.getEntities().get(p_261842_, (p_261428_) -> {
			if (p_262091_.test(p_261428_)) {
				FakeMCCallbacks.FakeServerLevel.getEntities(this, p_261703_, p_261428_);
				if (p_261703_.size() >= p_261907_) {
					return AbortableIterationConsumer.Continuation.ABORT;
				}
			}

			return AbortableIterationConsumer.Continuation.CONTINUE;
		});
	}
}
