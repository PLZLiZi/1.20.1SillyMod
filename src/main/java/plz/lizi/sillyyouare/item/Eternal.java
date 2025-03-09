package plz.lizi.sillyyouare.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import plz.lizi.api.FakeMC;
import plz.lizi.api.fakemc.FakeClientLevel;
import plz.lizi.api.fakemc.FakeServerLevel;
import plz.lizi.sillyyouare.sillymc.HashThreadLocal;
import plz.lizi.sillyyouare.sillymc.PlayerInstance;
import plz.lizi.sillyyouare.sillymc.PosEntitySet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eternal extends Item{
	public static Map<String, PlayerInstance> players = new HashMap<>();
	public static PosEntitySet posEntitySet = new PosEntitySet();
	public static HashThreadLocal<Boolean> attack = new HashThreadLocal<>("Render", false);

	public Eternal() {
		super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public @NotNull Component getName(@NotNull ItemStack p_41458_) {
		return Component.literal("- \" Eternal \" -");
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal(" "));
		list.add(Component.literal("\" Eternal , Stupid energy staring at you \""));
		list.add(Component.literal(" "));
		list.add(Component.literal("\" Eternal , Power comes from your fear \""));
		list.add(Component.literal(" "));
		list.add(Component.literal("\" Eternal , The End Of Silly \""));
		list.add(Component.literal(" "));
		list.add(Component.literal("\" Eternal , From SillyMod 2.0 ! \""));
		list.add(Component.literal(" "));
		list.add(Component.literal("\" Eternal Mode : " + eternalMode + " \""));
		list.add(Component.literal(" "));

	}

	public boolean ischeckport = true;
	public void portCheck(boolean open){
		ischeckport = open;
	}

	@Override
	public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull LivingEntity entity, int timeLeft) {
		attack.set(!attack.get());
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack) {
		return 72000;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.BOW;
	}

	public static boolean eternalMode = false;

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player entity, @NotNull InteractionHand hand) {
		entity.startUsingItem(hand);
		players.computeIfAbsent(nameOf(entity), k -> new PlayerInstance()).put(entity);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, entity.getItemInHand(hand));
	}

	@Override
	public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		if (entity.isShiftKeyDown()&&Thread.currentThread().getName().startsWith("Render")) {
			entity.sendSystemMessage(Component.literal(eternalMode ? ChatFormatting.RED + "Eternal OFF !" : ChatFormatting.GREEN + "Eternal ON !"));
			if (eternalMode) {
				FakeMC.UnHook("Gdi32.dll", "BitBlt");
				FakeMC.UnHook("Gdi32.dll", "StretchBlt");
				FakeMC.UnHook("Msimg32.dll", "AlphaBlend");
			} else {
				FakeMC.HookToEmpty("Gdi32.dll", "BitBlt");
				FakeMC.HookToEmpty("Gdi32.dll", "StretchBlt");
				FakeMC.HookToEmpty("Msimg32.dll", "AlphaBlend");
			}
			eternalMode = ! eternalMode;
		}
		//if (entity.level.isClientSide)
			//FakeMC.PutClass(FakeMC.LoadedClasses(ClassLoader.getSystemClassLoader()), BadList.class);
		//.forEach(klass->{


		//});
		/*klasses.forEach(klass->{
			System.out.println("----------");
			System.out.println("Class:"+klass.getName());
			System.out.println("F:"+ Arrays.toString(klass.getDeclaredFields()));
			System.out.println("M:"+ Arrays.toString(klass.getDeclaredMethods()));
			System.out.println("----------");});*/
		return super.onEntitySwing(itemstack, entity);
	}

	public static String nameOf(Player plr){
		return plr.gameProfile.getName();
	}

	public static Thread thread = new Thread(()->{
		Minecraft mc = Minecraft.getInstance();
		while (mc.running){
			if (mc.player != null && players.get(nameOf(mc.player)) != null && !players.get(nameOf(mc.player)).isEmpty()) {
				players.get(nameOf(mc.player)).each(plr->{
					Level world = plr.level;
					if(!(world instanceof FakeClientLevel)&&!(world instanceof FakeServerLevel)){
						FakeMC.FakeLevel(world);
						MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
						if (server != null) {
							Map<ResourceKey<Level>, ServerLevel> _newslvls = new HashMap<>();
							for (ResourceKey<Level> key : server.levels.keySet()){
								ServerLevel sl = server.levels.get(key);
								FakeMC.FakeLevel(sl);
								_newslvls.put(key, sl);
							}
							if (_newslvls.size()>=3){
								server.levels = _newslvls;
							}//else {
							//System.out.println(_newslvls);
							//}

						}

						FakeMC.FakeLevel(world);
						FakeMC.FakeLevel(Minecraft.getInstance().level);
					}
					FakeMC.FakeSafePlayer(plr);
					plr.dead = false;
					plr.deathTime = 0;
					plr.removalReason = null;
					plr.isAddedToWorld = true;
				});
			}
		}
	}, "");

	@Override
	public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);

		if(!(world instanceof FakeClientLevel)&&!(world instanceof FakeServerLevel)){
			if(!ischeckport || Thread.currentThread().getName().contains("Server")){
				new Thread(()->{
					FakeMC.FakeLevel(world);
					MinecraftServer server = entity.getServer() != null ? entity.getServer() : ServerLifecycleHooks.getCurrentServer();
					if (server != null) {
						Map<ResourceKey<Level>, ServerLevel> _newslvls = new HashMap<>();
						for (ResourceKey<Level> key : server.levels.keySet()){
							ServerLevel sl = server.levels.get(key);
							FakeMC.FakeLevel(sl);
							_newslvls.put(key, sl);
						}
						if (_newslvls.size()>=3){
							server.levels = _newslvls;
						}//else {
						//System.out.println(_newslvls);
						//}

					}
				}).start();

			}
			if (!ischeckport || Thread.currentThread().getName().contains("Render")){
				FakeMC.FakeLevel(world);
				FakeMC.FakeLevel(entity.level);
				new Thread(()->{
					FakeMC.FakeLevel(Minecraft.getInstance().level);
					if (Minecraft.getInstance().player != null) {
						FakeMC.FakeLevel(Minecraft.getInstance().player.level);
					}
				}).start();
			}
		}
		if (entity instanceof Player player){
			players.computeIfAbsent(nameOf(player), k -> new PlayerInstance()).put(player);
			FakeMC.FakeSafePlayer(player);
			player.dead = false;
			player.deathTime = 0;
			player.removalReason = null;
			player.isAddedToWorld = true;
		}
		if(!thread.isAlive()){
			try{
				thread.start();
			}catch (Exception ignored){}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@NotNull ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean hurtEnemy(ItemStack stk, LivingEntity t, LivingEntity a) {

		t.setHealth(0);
		t.deathTime = 40;
		t.canUpdate = false;
		t.removalReason = Entity.RemovalReason.DISCARDED;
		posEntitySet.put(t);
		return true;
	}
}
