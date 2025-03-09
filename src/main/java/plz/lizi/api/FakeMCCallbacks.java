package plz.lizi.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;

import java.util.List;

import static plz.lizi.sillyyouare.item.Eternal.attack;
import static plz.lizi.sillyyouare.item.Eternal.posEntitySet;

public class FakeMCCallbacks {
	public static class FakeClientLevel{
		public static void entitiesForRendering(plz.lizi.api.fakemc.FakeClientLevel zhis, List<Entity> call, Entity entity) {
			boolean add = true;
			if (attack.get()) {
				if (entity instanceof Player) {
				} else {
					add = false;
					zhis.entityStorage.entityStorage.remove(entity);
					LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityStorage.entityGetter;
					getter.visibleEntities.byId.remove(entity.id);
					getter.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
					zhis.entityStorage.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
				}
			}
			if (posEntitySet.contains(entity)) {
				add = false;
				zhis.entityStorage.entityStorage.remove(entity);
				LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityStorage.entityGetter;
				getter.visibleEntities.byId.remove(entity.id);
				getter.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
				zhis.entityStorage.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
			}
			if (add)call.add(entity);
		}

	}

	public static class FakeServerLevel{
		public static void getAllEntities(plz.lizi.api.fakemc.FakeServerLevel zhis, List<Entity> call, Entity entity) {
			boolean add = true;
			if (attack.get()) {
				if (entity instanceof Player) {
				} else {
					add = false;
					zhis.entityManager.visibleEntityStorage.remove(entity);
					LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityManager.entityGetter;
					getter.visibleEntities.byId.remove(entity.id);
					getter.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
					zhis.entityManager.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
				}
			}
			if (posEntitySet.contains(entity)){
				add = false;
				zhis.entityManager.visibleEntityStorage.remove(entity);
				LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityManager.entityGetter;
				getter.visibleEntities.byId.remove(entity.id);
				getter.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
				zhis.entityManager.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
			}
			if (add) call.add(entity);
		}

		public static <T extends Entity> void getEntities(plz.lizi.api.fakemc.FakeServerLevel zhis, List<? super T> call, T entity) {
			boolean add = true;
			if (attack.get()) {
				if (entity instanceof Player) {
				} else {
					add = false;
					zhis.entityManager.visibleEntityStorage.remove(entity);
					LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityManager.entityGetter;
					getter.visibleEntities.byId.remove(entity.id);
					getter.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
					zhis.entityManager.sectionStorage.sections.values().forEach(section -> {
						section.remove(entity);
					});
				}
			}
			if (posEntitySet.contains(entity)){
				add = false;
				zhis.entityManager.visibleEntityStorage.remove(entity);
				LevelEntityGetterAdapter<Entity> getter = (LevelEntityGetterAdapter<Entity>) zhis.entityManager.entityGetter;
				getter.visibleEntities.byId.remove(entity.id);
				getter.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
				zhis.entityManager.sectionStorage.sections.values().forEach(section -> {
					section.remove(entity);
				});
			}
			if (add) call.add(entity);
		}
	}

	public static void beforeGlfwSwapBuffers(){
		Minecraft.getInstance().gameRenderer.renderLevel(1, 0, new PoseStack());
	}

	public static void afterGlfwSwapBuffers(){
		//System.out.println("after");
	}
}
