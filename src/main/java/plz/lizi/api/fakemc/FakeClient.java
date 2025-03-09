package plz.lizi.api.fakemc;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.main.GameConfig;
import net.minecraft.util.profiling.*;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plz.lizi.api.FakeMC;

import java.util.Set;
import java.util.function.Supplier;

public class FakeClient extends Minecraft {
	public FakeClient(GameConfig p_91084_) {
		super(p_91084_);
	}

	@Nullable
	@Override
	public Overlay getOverlay() {
		return this.overlay;
	}

	@Override
	public @NotNull ProfilerFiller getProfiler() {
		return this.profiler;
	}

	@Override
	public void tick() {
		FakeMC.PutClass(profiler, FakeProfiler.class);
		super.tick();
	}

	private static final class FakeProfiler implements ProfileCollector {

		public void startTick() {
		}

		public void endTick() {
		}

		public void push(@NotNull String p_18559_) {
		}

		public void push(@NotNull Supplier<String> p_18561_) {
		}

		public void markForCharting(@NotNull MetricCategory p_145951_) {
		}

		/**
		 * Ending Minecraft tick()
		 */
		public void pop() {

		}

		public void popPush(@NotNull String p_18564_) {
		}

		public void popPush(@NotNull Supplier<String> p_18566_) {
		}

		public void incrementCounter(@NotNull String p_185253_, int p_185254_) {
		}

		public void incrementCounter(@NotNull Supplier<String> p_185256_, int p_185257_) {
		}

		public @NotNull ProfileResults getResults() {
			return EmptyProfileResults.EMPTY;
		}

		@javax.annotation.Nullable
		public ActiveProfiler.PathEntry getEntry(@NotNull String p_145953_) {
			return null;
		}

		public @NotNull Set<Pair<String, MetricCategory>> getChartedPaths() {
			return ImmutableSet.of();
		}
	}
}
