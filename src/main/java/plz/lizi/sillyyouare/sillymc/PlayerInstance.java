package plz.lizi.sillyyouare.sillymc;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerInstance {
	private ServerPlayer serverInstance;
	private LocalPlayer clientInstance;

	public PlayerInstance(){

	}

	public void put(Player plr){
		if (plr instanceof ServerPlayer splr){
			serverInstance = splr;
		} else if (plr instanceof LocalPlayer lplr) {
			clientInstance = lplr;
		}
	}

	public ServerPlayer server(){
		return serverInstance;
	}

	public LocalPlayer client(){
		return clientInstance;
	}

	public void each(Consumer<Player> consumer) {
		List<Player> call = new ArrayList<>();
		call.add(serverInstance);
		call.add(clientInstance);call.forEach(consumer);
	}

	public boolean isEmpty(){
		return serverInstance == null || clientInstance == null;
	}
}
