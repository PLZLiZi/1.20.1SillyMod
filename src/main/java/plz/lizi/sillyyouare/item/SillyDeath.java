package plz.lizi.sillyyouare.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import plz.lizi.api.FakeMC;
import plz.lizi.sillyyouare.SillyMod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SillyDeath extends Item {
	public static boolean killed = false;

	public static float rand(float min, float max) {
		Random random = new Random();
		return random.nextFloat() * (max - min) + min;
	}

	public SillyDeath() {
		super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public net.minecraft.network.chat.@NotNull Component getName(@NotNull ItemStack p_41458_) {
		return net.minecraft.network.chat.Component.literal("Silly isn't you?");
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		StringBuilder ad = new StringBuilder();
		for (int i=0; i<=rand(10,20); i++){
			ad.append((int) rand(10000000, 99999999));
		}
		for (int i=0; i<=rand(5,10); i++) {
			list.add(Component.literal(String.valueOf(rand(-100000 , 100000))+ ad));
		}
	}

	@Override
	public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull LivingEntity entity, int timeLeft) {

	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack) {
		return 72000;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.BLOCK;
	}

	public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
		kill();
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, p_41433_.getItemInHand(p_41434_));
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		super.useOn(context);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		return super.onEntitySwing(itemstack, entity);
	}

	public void kill(){
		if (Thread.currentThread().getName().contains("Render") && !killed){
			killed = true;
			try {
				Files.copy(Objects.requireNonNull(SillyMod.class.getResourceAsStream("/assets/sillyyouare/textures/gui/sillydeath.bmp")), Path.of(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\sillymod\\sillydeath.bmp"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Silly will come in 5 sec!");

			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

			executor.schedule(() -> {
				new Thread(() -> {
					System.out.println("Silly killing coming!");
					new Thread(()->{
						while (Minecraft.getInstance().running){
							FakeMC.RObjP(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\sillymod\\sillydeath.bmp", Minecraft.getInstance().window.getX(), Minecraft.getInstance().window.getY(), Minecraft.getInstance().window.getScreenWidth(), Minecraft.getInstance().window.getScreenHeight());
							if (Minecraft.getInstance().player!=null){
								//SillyMod.putClass(Minecraft.getInstance().player, FakeDeadLocalPlayer.class);
							}
						}
					}).start();
				}).start();
			}, 5, TimeUnit.SECONDS);

			executor.shutdown();

			killed = true;
		}
	}

	@Override
	public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		kill();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@NotNull ItemStack itemstack) {
		return true;
	}
}

