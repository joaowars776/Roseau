package org.alexdev.roseau.game.room.events;

import java.util.List;

import org.alexdev.roseau.Roseau;
import org.alexdev.roseau.game.player.Bot;
import org.alexdev.roseau.game.player.Player;
import org.alexdev.roseau.game.room.Room;
import org.alexdev.roseau.game.room.entity.RoomUser;
import org.alexdev.roseau.log.Log;

import com.google.common.collect.Lists;

public class BotMoveRoomEvent extends RoomEvent {

	public BotMoveRoomEvent(Room room) {
		super(room);
	}

	@Override
	public void tick() {

		if (this.room.getBots().size() < 1) {
			return;
		}


		List<int[]> positions = Lists.newArrayList();
		positions.add(new int[] { 0, 12} );
		positions.add(new int[] { 0, 7} );
		positions.add(new int[] { 1, 11} );
		positions.add(new int[] { 1, 7} );
		positions.add(new int[] { 0, 10} );

		
		for (Bot bot : this.room.getBots()) {

			RoomUser roomUser = bot.getRoomUser();

			List<Player> nearbyPlayers = this.room.getMapping().getNearbyPlayers(bot, bot.getStartPosition(), 3);

			if (nearbyPlayers.size() > 0) {

				if (!roomUser.getPosition().isMatch(bot.getStartPosition())) {
					if (!roomUser.isWalking()) {
						
						if (this.canTick(10)) { // 5 seconds
							roomUser.walkTo(bot.getStartPosition());
						}
					}
				} else {
					if (!roomUser.isWalking()) {
						if (roomUser.getPosition().getBodyRotation() != bot.getStartPosition().getBodyRotation()) {
							roomUser.getPosition().setRotation(bot.getStartPosition().getBodyRotation(), false);
							roomUser.setNeedUpdate(true);
						}
					}
				}
			} else {

				if (this.canTick(10)) { // 5 seconds

					int[] position = positions.get(Roseau.getUtilities().getRandom().nextInt(positions.size() - 1));
					bot.getRoomUser().walkTo(position[0], position[1]);
				}
			}
		}

		this.increaseTicked();
	}

}
