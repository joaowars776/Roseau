package org.alexdev.roseau.messages.outgoing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.alexdev.roseau.game.entity.Entity;
import org.alexdev.roseau.game.room.entity.RoomUserStatus;
import org.alexdev.roseau.game.room.model.Position;
import org.alexdev.roseau.messages.OutgoingMessageComposer;
import org.alexdev.roseau.util.StringUtil;


public class STATUS extends OutgoingMessageComposer {

	private List<Entity> entities;

	public STATUS(Entity entity) {
		this.entities = Collections.singletonList(entity);
	}

	public STATUS(List<Entity> entities) {
		this.entities = entities;
	}

	@Override
	public void write() {
		response.init("STATUS ");

		for (Entity entity : this.entities) {
			response.appendNewArgument(entity.getDetails().getName());

			if (entity.getRoomUser().isWalking()) {
				
				if (entity.getRoomUser().getNext() == null) {
					entity.getRoomUser().stopWalking();
				}
			}
			
			response.appendArgument(String.valueOf(entity.getRoomUser().getPosition().getX()));
			response.appendArgument(String.valueOf(entity.getRoomUser().getPosition().getY()), ',');
			response.appendArgument(StringUtil.format(entity.getRoomUser().getPosition().getZ()), ',');
			response.appendArgument(String.valueOf(entity.getRoomUser().getPosition().getHeadRotation()), ',');
			response.appendArgument(String.valueOf(entity.getRoomUser().getPosition().getRotation()), ',');

			StringBuilder status = new StringBuilder("/");

			for (Entry<String, RoomUserStatus> set : entity.getRoomUser().getStatuses().entrySet()) {
				RoomUserStatus statusEntry = set.getValue();

				status.append(statusEntry.getStatus());
				status.append(statusEntry.getValue());
				status.append("/");
			}

			response.append(status.toString());
		}
	}

}
